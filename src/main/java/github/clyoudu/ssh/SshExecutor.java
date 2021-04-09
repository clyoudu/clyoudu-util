/*
 * <p>文件名称: SshExecutor.java</p>
 * <p>项目描述: KOCA 金证云原生平台 v2.1.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C) 2019-2020</p>
 */

package github.clyoudu.ssh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;

import github.clyoudu.ssh.pool.SshConnectionPool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.apache.sshd.sftp.client.fs.SftpFileSystemProvider;

/**
 * ssh客户端.
 *
 * @author leichen
 * @since 2.1.0, 2021/2/9 1:46 下午
 */
@Slf4j
public class SshExecutor {

    private SshConnectionPool sshConnectionPool;

    private SshConfig sshConfig;

    /**
     * 构造方法
     * @param sshConnectionPool ssh session连接池
     * @param sshConfig ansible配置
     */
    public SshExecutor(SshConnectionPool sshConnectionPool, SshConfig sshConfig) {
        this.sshConnectionPool = sshConnectionPool;
        this.sshConfig = sshConfig;
    }

    /**
     * 执行ssh命令
     * @param command 命令
     * @param session session
     * @return 执行结果
     * @throws Exception 异常
     */
    public SshResult exec(String command, ClientSession session) throws Exception {
        log.info("Execute command: {}", command);

        // execute
        try (ChannelExec ec = session.createExecChannel(command)) {
            ByteArrayOutputStream stdOutOs = new ByteArrayOutputStream();
            ByteArrayOutputStream stdErrOs = new ByteArrayOutputStream();
            ec.setOut(stdOutOs);
            ec.setErr(stdErrOs);

            ec.open();
            ec.waitFor(Collections.singletonList(ClientChannelEvent.CLOSED), sshConfig.getExecuteTimeout());

            Integer exitStatus = ec.getExitStatus();
            if (exitStatus == null) {
                log.error("Command execute timeout after {} mill seconds", sshConfig.getExecuteTimeout());
                throw new RuntimeException(
                    "Command execute timeout after " + sshConfig.getExecuteTimeout() + " mill seconds.");
            }

            String stdOut = IOUtils.toString(stdOutOs.toByteArray(), StandardCharsets.UTF_8.name());
            String stdErr = IOUtils.toString(stdErrOs.toByteArray(), StandardCharsets.UTF_8.name());

            return new SshResult(exitStatus, stdOut, stdErr);
        }

    }

    /**
     * 上传文件
     * @param fileDir 文件夹
     * @param fileName 文件名
     * @param inputStream input stream
     * @throws Exception 异常
     */
    public void upload(String fileDir, String fileName, InputStream inputStream) throws Exception {
        ClientSession session = sshConnectionPool.borrowObject();
        SftpFileSystem fs = null;
        try {
            fs = SftpClientFactory.instance().createSftpFileSystem(session);
            Path remoteRoot = fs.getDefaultDir().resolve(fileDir);
            if (!Files.exists(remoteRoot)) {
                Files.createDirectories(remoteRoot);
            }
            Path remoteFile = remoteRoot.resolve(fileName);
            Files.deleteIfExists(remoteFile);
            Files.copy(inputStream, remoteFile);
        } finally {
            try {
                if (fs != null) {
                    SftpFileSystemProvider provider = fs.provider();
                    String fsId = fs.getId();
                    provider.removeFileSystem(fsId);
                }
            } catch (Exception e) {
                log.warn("Remove fs error", e);
            }
            sshConnectionPool.returnObject(session);
        }
    }

    /**
     * 执行ssh命令
     * @param command 命令
     * @return 执行结果
     * @throws Exception 异常
     */
    public SshResult exec(String command) throws Exception {
        ClientSession session = sshConnectionPool.borrowObject();
        try {
            return exec(command, session);
        } finally {
            sshConnectionPool.returnObject(session);
        }
    }

}

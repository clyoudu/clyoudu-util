/*
 * <p>文件名称: SshConnectionFactory.java</p>
 * <p>项目描述: KOCA 金证云原生平台 v2.1.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C) 2019-2020</p>
 */

package github.clyoudu.ssh.pool;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import github.clyoudu.ssh.SshConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.future.ConnectFuture;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;

/**
 * ssh连接工厂类.
 *
 * @author leichen
 * @since 2.1.0, 2021/2/18 9:23 上午
 */
@Slf4j
public class SshConnectionFactory implements PooledObjectFactory<ClientSession> {

    private final SshConfig sshConfig;

    private final SshClient sshClient;

    /**
     * 构造方法
     * @param sshConfig ansible配置
     * @param sshClient ssh client
     */
    public SshConnectionFactory(SshConfig sshConfig, SshClient sshClient) {
        this.sshConfig = sshConfig;
        this.sshClient = sshClient;
    }

    @Override
    public PooledObject<ClientSession> makeObject() throws Exception {
        // session
        ConnectFuture verifySession = sshClient.connect(sshConfig.getUsername(), sshConfig.getIp(), sshConfig.getPort())
            .verify(sshConfig.getConnectTimeout());
        if (!verifySession.isConnected()) {
            log.error("Session connect failed after {} mill seconds", sshConfig.getConnectTimeout());
            throw new RuntimeException(
                "Session connect failed after " + sshConfig.getConnectTimeout() + " mill seconds.");
        }
        ClientSession session = verifySession.getSession();
        if (AuthType.KEYPAIR.name().equalsIgnoreCase(sshConfig.getAuthType())) {
            Path pathPrivate = null;
            Path pathPublic = null;
            if (StringUtils.isNotBlank(sshConfig.getPrivateKey())) {
                pathPrivate = new File(sshConfig.getPrivateKey()).toPath();
            }
            if (StringUtils.isNotBlank(sshConfig.getPublicKey())) {
                pathPublic = new File(sshConfig.getPublicKey()).toPath();
            }
            if (pathPrivate != null || pathPublic != null) {
                session.addPublicKeyIdentity(new FileKeyPairProvider(
                    Stream.of(pathPrivate, pathPublic).filter(Objects::nonNull).collect(Collectors.toList()))
                    .loadKey(session, sshConfig.getKeypairType()));
            }
        } else if (AuthType.PASSWORD.name().equalsIgnoreCase(sshConfig.getAuthType())) {
            session.addPasswordIdentity(sshConfig.getPassword());
        } else {
            throw new RuntimeException("Unknown ssh auth type: " + sshConfig.getAuthType());
        }

        // authentication
        if (!session.auth().verify(sshConfig.getAuthTimeout()).isSuccess()) {
            log.error("Authentication failed after {} mill seconds", sshConfig.getAuthTimeout());
            session.close();
            throw new RuntimeException(
                "Ansible control machine authentication failed after " + sshConfig.getAuthTimeout() + " mill seconds.");
        }

        return new DefaultPooledObject<>(session);
    }

    @Override
    public void destroyObject(PooledObject<ClientSession> pooledObject) throws Exception {
        ClientSession session = pooledObject.getObject();
        if (session != null && session.isAuthenticated()) {
            session.close();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ClientSession> pooledObject) {
        ClientSession session = pooledObject.getObject();
        try (ChannelExec channel = session.createExecChannel(sshConfig.getValidateCommand())) {
            channel.open();
            channel.waitFor(Collections.singletonList(ClientChannelEvent.CLOSED), sshConfig.getValidateTimeout());
            return channel.getExitStatus() == 0;
        } catch (Exception e) {
            log.error("Ssh session validate error", e);
            pooledObject.markAbandoned();
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<ClientSession> pooledObject) throws Exception {
        // do nothing
        // session归还时不需要做任何初始化操作，命令执行不受session切换影响(无上下文)
    }

    @Override
    public void passivateObject(PooledObject<ClientSession> pooledObject) throws Exception {
        // do nothing
        // session归还时不需要做任何清理操作(钝化)，命令执行不受session切换影响(无上下文)
    }

    private enum AuthType {
        /**
         * 密码认证
         */
        PASSWORD,
        /**
         * 密钥对认证
         */
        KEYPAIR;
    }
}

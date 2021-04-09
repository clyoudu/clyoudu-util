package github.clyoudu.ssh;

import java.time.Duration;
import java.util.Scanner;

import github.clyoudu.ssh.pool.SshConnectionFactory;
import github.clyoudu.ssh.pool.SshConnectionPool;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.core.CoreModuleProperties;

public class SshTest {

    public static void main(String[] args) throws Exception {
        // ssh client
        SshClient client = ClientBuilder.builder().build();
        CoreModuleProperties.IDLE_TIMEOUT.set(client, Duration.ZERO);
        client.start();

        // ssh config
        SshConfig sshConfig = new SshConfig();
        sshConfig.setIp("127.0.0.1");
        sshConfig.setAuthType("PASSWORD");
        sshConfig.setPort(22);
        sshConfig.setUsername("root");
        sshConfig.setPassword("root");

        // ssh connection factory
        SshConnectionFactory sshConnectionFactory = new SshConnectionFactory(sshConfig, client);

        // ssh connection pool
        GenericObjectPoolConfig<ClientSession> config = new GenericObjectPoolConfig<>();
        config.setMaxIdle(sshConfig.getMaxIdle());
        config.setMaxTotal(sshConfig.getMaxTotal());
        config.setMinIdle(sshConfig.getMinIdle());
        config.setTestWhileIdle(sshConfig.getTestWhileIdle());
        config.setTestOnCreate(sshConfig.getTestOnCreate());
        config.setTestOnBorrow(sshConfig.getTestOnBorrow());
        config.setTestOnReturn(sshConfig.getTestOnReturn());
        config.setMinEvictableIdleTimeMillis(sshConfig.getMinEvictableIdleTimeMillis());
        config.setTimeBetweenEvictionRunsMillis(sshConfig.getTimeBetweenEvictionRunsMillis());
        config.setBlockWhenExhausted(sshConfig.getBlockWhenExhausted());
        config.setMaxWaitMillis(sshConfig.getMaxWaitMillis());
        config.setJmxEnabled(false);

        AbandonedConfig abandonedConfig = new AbandonedConfig();
        abandonedConfig.setLogAbandoned(true);

        SshConnectionPool sshConnectionPool = new SshConnectionPool(sshConnectionFactory, config, abandonedConfig);

        SshExecutor sshExecutor = new SshExecutor(sshConnectionPool, sshConfig);

        Scanner scanner = new Scanner(System.in);
        String line;
        while (true) {
            line = scanner.nextLine();
            if (StringUtils.isNotBlank(line) && !line.equalsIgnoreCase("exit")) {
                System.out.println(sshExecutor.exec(line));
            } else {
                sshConnectionPool.close();
                client.close();
                break;
            }
        }
    }

}

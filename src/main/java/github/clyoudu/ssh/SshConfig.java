package github.clyoudu.ssh;

import lombok.Data;
import org.apache.commons.io.FilenameUtils;

/**
 * @author leichen
 */
@Data
public class SshConfig {

    /**
     * 认证类型：PASSWORD/KEYPAIR
     */
    private String authType = "PASSWORD";

    /**
     * ip
     */
    private String ip = "127.0.0.1";

    /**
     * ssh 端口
     */
    private Integer port = 22;

    /**
     * ssh用户
     */
    private String username = "root";

    /**
     * ssh密码
     */
    private String password;

    /**
     * 密钥对类型， 默认ssh-rsa
     */
    private String keypairType = "ssh-rsa";

    /**
     * 公钥路径，默认~/.ssh/id_rsa.pub
     */
    private String publicKey = FilenameUtils.concat(System.getProperty("user.home"), ".ssh/id_rsa.pub");

    /**
     * 私玥路径， 默认~/.ssh/id_rsa
     */
    private String privateKey = FilenameUtils.concat(System.getProperty("user.home"), ".ssh/id_rsa");

    /**
     * session open超时时间
     */
    private Long connectTimeout = 10000L;

    /**
     * 认证超时时间
     */
    private Long authTimeout = 10000L;

    /**
     * 命令执行超时时间
     */
    private Long executeTimeout = 60000L;

    /**
     * ssh连接池最大空闲session个数
     */
    private Integer maxIdle = 8;

    /**
     * ssh连接池最大活跃session个数
     */
    private Integer maxTotal = 15;

    /**
     * ssh连接池最少空闲session个数
     */
    private Integer minIdle = 2;

    /**
     * 是否在空闲状态测试session
     */
    private Boolean testWhileIdle = true;

    /**
     * 是否在创建session时测试
     */
    private Boolean testOnCreate = false;

    /**
     * 是否在获取session时测试
     */
    private Boolean testOnBorrow = false;

    /**
     * 是否在归还session时测试
     */
    private Boolean testOnReturn = false;

    /**
     * session最小生存时间
     */
    private Long minEvictableIdleTimeMillis = 300000L;

    /**
     * session测试间隔
     */
    private Long timeBetweenEvictionRunsMillis = 30000L;

    /**
     * session池活跃session到达最大值时，其它获取session的操作是否阻塞
     */
    private Boolean blockWhenExhausted = true;

    /**
     * 获取session阻塞时间
     */
    private Long maxWaitMillis = 30000L;

    /**
     * session检查命令
     */
    private String validateCommand = "echo '1'";

    /**
     * session检查命令超时时间
     */
    private Long validateTimeout = 10000L;

}

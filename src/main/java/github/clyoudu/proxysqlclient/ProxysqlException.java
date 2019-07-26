package github.clyoudu.proxysqlclient;

/**
 * Create by IntelliJ IDEA
 *
 * @author chenlei
 * @dateTime 2019/7/26 17:46
 * @description ProxysqlException
 */
public class ProxysqlException extends RuntimeException {

    public ProxysqlException(String errMsg) {
        super(errMsg);
    }

    public ProxysqlException(Throwable cause) {
        super(cause);
    }

    public ProxysqlException(String errMsg, Throwable cause) {
        super(errMsg, cause);
    }

}

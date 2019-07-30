package github.clyoudu.proxysqlclient;

import com.alibaba.fastjson.JSON;
import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.ResultSet;
import com.github.jasync.sql.db.RowData;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Create by IntelliJ IDEA
 *
 * @author chenlei
 * @dateTime 2019/7/26 17:39
 * @description ProxysqlClient
 */
public class ProxysqlClient {

    private final static Logger log = LoggerFactory.getLogger(ProxysqlClient.class);

    /**
     * mysql 默认连接的schema
     * proxysql 虽然没有这个schema，但并不会报错
     */
    public static final String DEFAULT_SCHEMA = "information_schema";
    private static final String EXECUTE_SQL_ERROR = "执行sql[%s]异常";
    private static final String EXECUTE_SQL_TIMEOUT = "执行sql[%s]超时";

    /**
     * 禁止用new实例化
     */
    private ProxysqlClient() {
    }

    /**
     * 执行proxysql语句，但不需要执行结果
     * 可以多条语句顺序执行，所有语句在同一个事务中执行
     * 注意：proxysql不支持事务，但并不会报错
     * 测试通过：mysql、proxysql
     *
     * @param ip          必填，服务IP
     * @param port        必填，服务端口
     * @param database    选填，数据库名称，默认 information_schema
     * @param user        必填，用户名
     * @param pass        必填，密码
     * @param sqlList     必填，待执行的sql列表
     * @param timeout     选填，超时时间，单位s，默认不设超时时间
     * @param transaction 是否开启事务
     * @return 成功or失败
     * @throws ProxysqlException 所有本方法可处理的异常都包装成 github.clyoudu.proxysqlclient.ProxysqlException，外层可单独catch此异常做处理
     */
    public static boolean execute(String ip, Integer port, String database, String user, String pass, List<String> sqlList, Integer timeout, boolean transaction) {
        //参数检查
        if (StringUtils.isBlank(ip) || port == null || StringUtils.isBlank(user) || StringUtils.isBlank(pass)) {
            throw new ProxysqlException("参数错误：ip/port/user/pass不能为空");
        }

        //默认连接information_schema
        if (StringUtils.isBlank(database)) {
            database = DEFAULT_SCHEMA;
        }

        if (sqlList == null || sqlList.isEmpty()) {
            throw new ProxysqlException("执行sql为空");
        }

        Connection connection = MySQLConnectionBuilder.createConnectionPool(
                String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s", ip, port, database, user, pass)
        );

        CompletableFuture<QueryResult> result = null;
        if (transaction) {
            result = connection.inTransaction(c -> {
                CompletableFuture<QueryResult> future = null;
                for (String sql : sqlList) {
                    if (future == null) {
                        future = c.sendQuery(sql);
                    } else {
                        future = future.thenCompose(r -> c.sendQuery(sql));
                    }
                }
                return future;
            });
        } else {
            for (String sql : sqlList) {
                if (result == null) {
                    result = connection.sendQuery(sql);
                } else {
                    result = result.thenCompose(r -> connection.sendQuery(sql));
                }
            }
        }

        if (result == null) {
            log.error("执行sql失败！");
            return false;
        }

        try {
            if (timeout != null && timeout > 0) {
                result.get(timeout, TimeUnit.SECONDS);
            } else {
                result.get();
            }
        } catch (InterruptedException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, StringUtils.join(sqlList, ";")), e);
            Thread.currentThread().interrupt();
            throw new ProxysqlException(String.format(EXECUTE_SQL_ERROR, StringUtils.join(sqlList, ";")) + ": InterruptedException");
        } catch (ExecutionException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, StringUtils.join(sqlList, ";")), e);
            throw new ProxysqlException(String.format(EXECUTE_SQL_ERROR, StringUtils.join(sqlList, ";")) + ": " + e.getMessage());
        } catch (TimeoutException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, StringUtils.join(sqlList, ";")), e);
            throw new ProxysqlException(String.format(EXECUTE_SQL_TIMEOUT, StringUtils.join(sqlList, ";")) + "：" + timeout + "s");
        }

        connection.disconnect();

        return true;
    }

    /**
     * 执行proxysql语句，返回执行结果
     *
     * @param ip        必填，服务IP
     * @param port      必填，服务端口
     * @param database  选填，数据库名称，默认 information_schema
     * @param user      必填，用户名
     * @param pass      必填，密码
     * @param sqlString 必填，待执行的sql
     * @param timeout   选填，超时时间，单位s，默认不设超时时间
     * @return List<Map> 所有字段名均为数据库原始字段名
     * @throws ProxysqlException 所有本方法可处理的异常都包装成 github.clyoudu.proxysqlclient.ProxysqlException，外层可单独catch此异常做处理
     */
    public static List<Map<String, Object>> executeQuery(String ip, Integer port, String database, String user, String pass, String sqlString, Integer timeout) {
        //参数检查
        if (StringUtils.isBlank(ip) || port == null || StringUtils.isBlank(user) || StringUtils.isBlank(pass)) {
            throw new ProxysqlException("参数错误：ip/port/user/pass不能为空");
        }

        //默认连接information_schema
        if (StringUtils.isBlank(database)) {
            database = DEFAULT_SCHEMA;
        }

        if (StringUtils.isBlank(sqlString)) {
            throw new ProxysqlException("执行sql为空");
        }

        Connection connection = MySQLConnectionBuilder.createConnectionPool(
                String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s", ip, port, database, user, pass)
        );

        CompletableFuture<QueryResult> future = connection.sendQuery(sqlString);

        QueryResult queryResult;
        try {
            if (timeout == null || timeout <= 0) {
                queryResult = future.get();
            } else {
                queryResult = future.get(timeout, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, sqlString), e);
            Thread.currentThread().interrupt();
            throw new ProxysqlException(String.format(EXECUTE_SQL_ERROR, sqlString) + ": InterruptedException");
        } catch (ExecutionException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, sqlString), e);
            throw new ProxysqlException(String.format(EXECUTE_SQL_ERROR, sqlString) + ": " + e.getMessage());
        } catch (TimeoutException e) {
            log.error(String.format(EXECUTE_SQL_ERROR, sqlString), e);
            throw new ProxysqlException(String.format(EXECUTE_SQL_TIMEOUT, sqlString) + "：" + timeout + "s");
        }

        ResultSet resultSet = queryResult.getRows();
        List<String> names = resultSet.columnNames();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (RowData rowData : resultSet) {
            Map<String, Object> map = new HashMap<>(16);
            for (String name : names) {
                map.put(name, rowData.get(name));
            }
            resultList.add(map);
        }

        connection.disconnect();

        return resultList;
    }

    public static void main(String[] args) {
        //select
        log.info(JSON.toJSONString(executeQuery("localhost", 6032, "main", "root", "root",
                "select * from mysql_users",
                null)));
        //ddl & batch execute
        log.info(JSON.toJSONString(execute("localhost", 6032, "main", "root", "root",
                Arrays.asList("INSERT INTO mysql_users(username,password,default_hostgroup) VALUES ('app','pass',2)", "load mysql user to runtime", "save mysql user to disk"),
                null, false)));
    }

}
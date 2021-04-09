/*
 * <p>文件名称: SshConnectionPool.java</p>
 * <p>项目描述: KOCA 金证云原生平台 v2.1.0</p>
 * <p>公司名称: 深圳市金证科技股份有限公司</p>
 * <p>版权所有: (C) 2019-2020</p>
 */

package github.clyoudu.ssh.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.sshd.client.session.ClientSession;

/**
 * ssh连接池.
 *
 * @author leichen
 * @since 2.1.0, 2021/2/18 9:11 上午
 */
public class SshConnectionPool extends GenericObjectPool<ClientSession> {

    public SshConnectionPool(PooledObjectFactory<ClientSession> factory, GenericObjectPoolConfig<ClientSession> config,
        AbandonedConfig abandonedConfig) {
        super(factory, config, abandonedConfig);
    }
}

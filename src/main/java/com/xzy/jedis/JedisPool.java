package com.xzy.jedis;

import com.xzy.jedis.util.JedisConnectionException;
import com.xzy.jedis.util.JedisConstant;
import com.xzy.jedis.util.JedisException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xiazhengyue
 * @since 2021-03-01
 */
@Slf4j
public class JedisPool {

    protected GenericObjectPool<Jedis> internalPool;

    public JedisPool(String host) {
        this(new GenericObjectPoolConfig<Jedis>(), host, JedisConstant.DEFAULT_PORT, JedisConstant.DEFAULT_TIMEOUT);
    }

    public JedisPool(String host, int port) {
        this(new GenericObjectPoolConfig<Jedis>(), host, port, JedisConstant.DEFAULT_TIMEOUT);
    }

    public JedisPool(final GenericObjectPoolConfig<Jedis> config, final String host) {
        this(config, host, JedisConstant.DEFAULT_PORT, JedisConstant.DEFAULT_TIMEOUT);
    }

    public JedisPool(final GenericObjectPoolConfig<Jedis> poolConfig, final String host, int port,
                     final int connectionTimeout) {
        this.internalPool = new GenericObjectPool<Jedis>(new JedisFactory(host, port, connectionTimeout), poolConfig);
    }

    public Jedis getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new JedisConnectionException(
                    "Could not get a resource from the pool", e);
        }
    }

    public void returnResourceObject(final Jedis resource) {
        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new JedisException(
                    "Could not return the resource to the pool", e);
        }
    }

    /**
     * Poolable Object Factory custom impl.
     */
    private static class JedisFactory implements PooledObjectFactory<Jedis> {
        private final String host;
        private final int port;
        private final int timeout;

        public JedisFactory(final String host, final int port, final int timeout) {
            super();
            this.host = host;
            this.port = port;
            this.timeout = timeout;
        }

        @Override
        public void destroyObject(PooledObject<Jedis> obj) throws Exception {
            Jedis jedis = obj.getObject();
            if (jedis.isConnected()) {
                try {
                    jedis.disconnect();
                    log.debug("destroy one jedis client");
                } catch (Exception e) {
                    log.warn("Error while disconnect", e);
                }
            }
        }

        @Override
        public boolean validateObject(PooledObject<Jedis> obj) {
            Jedis jedis = obj.getObject();
            try {
                return jedis.isConnected() && jedis.ping().equals(JedisConstant.PONG);
            } catch (final Exception e) {
                return false;
            }
        }

        @Override
        public void activateObject(PooledObject<Jedis> obj) throws Exception {
            Jedis jedis = obj.getObject();
            if (!jedis.isConnected()) {
                jedis.connect();
            }
        }

        @Override
        public void passivateObject(PooledObject<Jedis> p) throws Exception {
            // TODO: 2021/3/1  jedis此处也没有实现，jedis考虑的是切换到db 0,我们本来就没有select,忽略即可
        }

        @Override
        public PooledObject<Jedis> makeObject() throws Exception {
            final Jedis jedis;
            if (timeout > 0) {
                jedis = new Jedis(this.host, this.port, this.timeout);
            } else {
                jedis = new Jedis(this.host, this.port);
            }

            jedis.connect();
            log.debug("make one jedis client");
            return new DefaultPooledObject<>(jedis);
        }
    }
}

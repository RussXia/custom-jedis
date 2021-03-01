import com.xzy.jedis.Jedis;
import com.xzy.jedis.JedisPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author xiazhengyue
 * @since 2021-03-01
 */
public class JedisPoolDemo {

    public static void main(String[] args) {
        GenericObjectPoolConfig<Jedis> config = new GenericObjectPoolConfig<>();
        config.setTestOnBorrow(true);
        config.setMinEvictableIdleTimeMillis(6000);
        config.setTimeBetweenEvictionRunsMillis(3000);
//        config.setMaxWaitMillis(1000 * 10);

        JedisPool jedisPool = new JedisPool(config, "localhost");

        //最大连接数量是8，所以第9次获取连接时会发生阻塞,然后10s后超时，然后抛出JedisConnectionException
        for (int i = 0; i < 9; i++) {
            Jedis jedis = jedisPool.getResource();
//            System.out.println(jedis.ping());
            jedis.set("key" + i, "value" + i);
            System.out.println(jedis.get("key" + i));
//            jedisPool.returnResourceObject(jedis);
        }
    }
}

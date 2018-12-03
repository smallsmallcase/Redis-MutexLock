package lock.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * package: lock.redis
 * date: 2018/12/3 11:30
 *
 * @author smallcase
 * @since JDK 1.8
 */
public class SintonRedisPool {

    private volatile static JedisPool instance;

    private SintonRedisPool() {

    }

    public static JedisPool getInstance() {
        if (instance == null) {
            synchronized (SintonRedisPool.class) {
                if (instance == null) {

                    instance = new JedisPool("hostname",6379);
                }
            }
        }
        return instance;
    }
}

package lock.redis;

import redis.clients.jedis.Jedis;

/**
 * package: lock.redis
 * date: 2018/12/2 22:20
 *
 * @author smallcase
 * @since JDK 1.8
 */

@Deprecated
public class SintonRedisCli {

    private volatile static Jedis instance;

    private SintonRedisCli() {

    }

    public static Jedis getRedisCli() {
        if (instance == null) {
            synchronized (Jedis.class) {
                if (instance == null) {
                    instance = new Jedis("39.108.131.39", 6379);
                }
            }
        }
        return instance;
    }

}

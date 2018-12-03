package lock.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * package: lock.redis
 * date: 2018/12/2 20:09
 *
 * @author smallcase
 * @since JDK 1.8
 */
public class MutexKey implements Runnable{

    private JedisPool pool;

    /**
     * 通过构造函数，获取连接池
     */
    public MutexKey() {
        this.pool = SintonRedisPool.getInstance();

    }


    /**
     * 通过连接池，获取Redis实例
     * @return
     */
    public Jedis getJedis() {
        return pool.getResource();
    }

    @Override
    public void run() {
        Thread thread = Thread.currentThread();
        String ww = null;
        try {
            System.out.println(thread.getName() + "来拿东西了");
            ww = this.get(thread, "ww");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (ww == null) {

        }

        System.out.println(thread.getName() + "成功获取到了数据，获取到的值为：" + ww);
    }

    /**
     * 模拟互斥锁，防止缓存击穿的一种方案，setnx
     * @param key
     * @return
     */
    public String get(Thread currentThread,String key) throws InterruptedException {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        try {
            //如果redis中没有取到缓存的数据
            if (value == null) {
//                System.out.println(currentThread.getName() + "说: 缓存里面没有value");
                if (jedis.setnx("mutexKey", "1") == 1) {
                    System.out.println(currentThread.getName() + "拿到了互斥锁，别的线程别进来");
                    /*
                    设置mutexKey过期时间，防止del mutexKey失败，value的缓存却过期了，永远进入拿不到互斥锁
                     */
                    jedis.expire("mutexKey", 5 * 60);
                    value = "hello"; //"Hello表示数据库中获取到的内容"
                    System.out.println(currentThread.getName() + "去数据库里拿到了" + value);

                    jedis.set(key, value);
                    System.out.println(value + "被缓存了");
                    jedis.del("mutexKey");
                } else {
                    //其他线程等待50ms,被互斥，很快就会有一个线程去DB获取数据，放入缓存中
                    System.out.println(currentThread.getName()+"没拿到，等一会继续拿");
                    Thread.sleep(50);
                    get(currentThread, key);

                }
            }
        }finally {
            jedis.close();   // redis资源回收


        }

        return value;
    }


    /**
     * 模拟100个进程去redis中获取东西
     * @param args
     */
    public static void main(String[] args) {
        MutexKey target = new MutexKey();
        for (int i = 0; i < 100; i++) {
            new Thread(target).start();

        }
    }

}

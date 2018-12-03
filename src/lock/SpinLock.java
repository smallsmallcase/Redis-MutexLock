package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * package: lock
 * date: 2018/11/25 14:01
 * 自旋锁，非可重入
 *
 * @author smallcase
 * @since JDK 1.8
 */
public class SpinLock implements BaseLock {

    private AtomicReference<Thread> cas = new AtomicReference<>();



    @Override
    public void lock() {
        Thread current = Thread.currentThread();
        //利用CAS,自旋,如果没有获取锁，就是null,cas.compareAndSet 就是true
        while (!cas.compareAndSet(null, current)) {
            //DO nothing
        }
    }

    @Override
    public void unlock() {
        Thread current = Thread.currentThread();
        if (cas.get() == current) {
            cas.compareAndSet(null, current);
        }
    }
}

package lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * package: lock
 * date: 2018/11/25 14:16
 *可重入的自旋锁
 * @author smallcase
 * @since JDK 1.8
 */
public class MyReentrantSpinLock implements BaseLock {

    private AtomicReference<Thread> cas = new AtomicReference<>();
    private int count;

    @Override
    public void lock() {
        Thread currentThread = Thread.currentThread();
        if (cas.get() == currentThread) {
            count++;
            return;
        }

        while (!cas.compareAndSet(null, currentThread)) {
            //DO Nothing
        }

    }

    @Override
    public void unlock() {
        Thread currentThread = Thread.currentThread();
        if (currentThread == cas.get()) {
            if (count > 0) {
                count--;
            } else {
                cas.compareAndSet(currentThread, null);
            }
        }

    }
}

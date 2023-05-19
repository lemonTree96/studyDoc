
public class ReentrantReadWriteLock
implements ReadWriteLock, java.io.Serializable {
    private final Sync sync;  //内部类 实现类AbstractQueuedSynchronized抽象类
    //默认实现是非公平锁
    public ReentrantLock() {
        sync = new NonfairSync();
    }
    //true  公平锁 false 非公平锁
    public ReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }
    //获取锁 
    public void lock() {
        sync.lock();
    }

    static final class NonfairSync extends Sync {
        
    }
}

//中间态变量，通过更改state的值判断是否占有锁  state=0 代表无线程占用 
//state >= 1 代表有锁 state =1  代表有一个线程占用锁 state=5假如是重入锁那么代表这个线程获取了5次锁，相应的需要释放5次锁使得state=0从而达到释放的目的。
private volatile int state;
//当前占有锁的线程
private transient Thread exclusiveOwnerThread;
//尝试获取锁
final void lock() {
    if (compareAndSetState(0, 1))
        setExclusiveOwnerThread(Thread.currentThread());
    else
        acquire(1);
}
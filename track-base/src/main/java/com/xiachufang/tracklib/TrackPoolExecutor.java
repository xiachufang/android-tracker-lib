package com.xiachufang.tracklib;



import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class TrackPoolExecutor extends ThreadPoolExecutor {

    private static  final  int MAX_THREAD_COUNT=Runtime.getRuntime().availableProcessors()+1;
    private static final int INIT_THREAD_COUNT = 2;
    private static final long SURPLUS_THREAD_LIFE = 30L;



    private static TrackPoolExecutor instance=getInstance();



    /**
     * 设置参数 see https://www.cnblogs.com/waytobestcoder/p/5323130.html
     * 为了减少开支, 让核心线程为2, 当需要的时候 重新创建线程 //当线程空闲时间达到keepAliveTime时，线程会退出，直到线程数量=corePoolSize
     * @return
     */
    public static TrackPoolExecutor getInstance() {
        if (null == instance) {
            synchronized (TrackPoolExecutor.class) {
                if (null == instance) {

                    instance = new TrackPoolExecutor(
                            INIT_THREAD_COUNT,
                            MAX_THREAD_COUNT,
                            SURPLUS_THREAD_LIFE,
                            TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(),
                            new TrackThreadFactory());
                    //allowCoreThreadTimeout=true，则会直到线程数量=0
                    instance.allowCoreThreadTimeOut(true);

                }
            }
        }
        return instance;
    }


    private TrackPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                executor.execute(r);
            }
        });
    }




}

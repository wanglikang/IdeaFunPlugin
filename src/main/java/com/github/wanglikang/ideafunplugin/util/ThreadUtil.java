package com.github.wanglikang.ideafunplugin.util;

import java.util.concurrent.*;

public class ThreadUtil {

    private static ThreadGroup myThreadGroup = new ThreadGroup("插件的后台线程组");
    private volatile static ThreadPoolExecutor pool = null;

    public static ThreadPoolExecutor getBackgroundThreadPool(){
         if(pool==null){
             synchronized (ThreadUtil.class){
                 if(pool==null){
                 pool = new ThreadPoolExecutor(1, 4, 60,
                         TimeUnit.SECONDS,new LinkedBlockingQueue<>(),
                         (r)->{
                             Thread t = new Thread(myThreadGroup, r, "后台线程");
                             //避免被线程池吃掉抛出的异常
                             t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                                 @Override
                                 public void uncaughtException(Thread t, Throwable e) {
                                     System.out.println(this+"happend a Throwable "+e.getCause());
                                     e.printStackTrace();
                                 }
                             });
                        return t;
                     },
                         new ThreadPoolExecutor.CallerRunsPolicy());
                 }
             }
         }
        return pool;
    }
}

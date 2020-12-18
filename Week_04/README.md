学习笔记

ExecutorService:

​	Future-submit(return Future)

​	void-execute(extend executor)

可不可以在主线程catch到异常，callable可以，runnable不行![image-20201105202453392](C:\Users\Master_PeiXU\AppData\Roaming\Typora\typora-user-images\image-20201105202453392.png)



分两段创建线程，就是为了进行一定的缓冲，core满了以后，尽量不去创建线程，而是先在队列中等待。同时在缓冲期间，core线程中可能任务已经跑完了。

BlockingQueue-{

Array:规定大小的BlockingQueue,构造必须要指定大小，对象FIFO

Linked：大小不固定，FIFO

PriorityBlockingQueue: 类似Linked,Comparator来进行比较排序

Synchronized：只有一个空间，生产消费，只有一个货物空间。

}



默认四种拒绝策略：

AbortPolicy:丢弃任务并抛出RejectedExecutionException（默认，因为这样程序员可以知道发生了什么，并作出处理的决策）

DiscardPolicy:丢弃任务，但不抛异常

DiscardOldestPolicy:丢弃队列最前面的任务，然后重新提交被拒绝的任务

CallerRunsPolicy:由调用线程（提交任务的线程）处理该任务，让往线程池中丢任务的线程，执行任务，这时候线程就停止丢任务，执行它丢的任务去了。（老师常用）

一和四最常见





ThreadFactory：

只需要覆盖一个方法，方法提供Runnable，去new一个线程，
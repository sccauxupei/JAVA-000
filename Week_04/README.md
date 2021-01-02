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

# 2021年1月1日11点27分：

开始补作业和心得了，欠了好多作业

看到老师在ppt里用了CompleteFuture，突然想到java函数式编程中的几大接口：

Supplier<T>：提供者，无输入，提供一个T类型的返回值

Consumer<T>：消费者，对传入的T类型进行消费，无返回

Future<T,R>：对一个输入T,提供一个输出R

Predicated<T>：断言，对于输入类型T,返回true/false

CompleteFuturede的用法和API：

```java
public static CompletableFuture<Void>   runAsync(Runnable runnable)
public static CompletableFuture<Void>   runAsync(Runnable runnable, Executor executor)
public static <U> CompletableFuture<U>  supplyAsync(Supplier<U> supplier)
public static <U> CompletableFuture<U>  supplyAsync(Supplier<U> supplier, Executor executor)
```

List/Set->AbstractCollection->Collection->Iterator

HashedMap->AbstractMap<>->Map

Arrays的asList返回的是它的内部类ArrayList,而不是java.util中的ArrayList，这个ArrayList是一个数据结构，不允许增加，只允许替换和get，原因是它里面的数组定义是：

![image-20210101134114988](C:\Users\Master_PeiXU\AppData\Roaming\Typora\typora-user-images\image-20210101134114988.png)

是一个final的数组，set方法需要index和value,用于替换指定index上的value

ForkJoinPool的commonParallelism参数设置：

```java
static{
...
common = java.security.AccessController.doPrivileged
(
    new java.security.PrivilegedAction<ForkJoinPool>() {
        public ForkJoinPool run() { return makeCommonPool(); }
    }
);
int par = common.config & SMASK; 
commonParallelism = par > 0 ? par : 1;
...
}
//调用static方法时构造的ForkJoinPPool
private static ForkJoinPool makeCommonPool() {
     try {  // ignore exceptions in accessing/parsing properties
            String pp = System.getProperty
                ("java.util.concurrent.ForkJoinPool.common.parallelism");
            String fp = System.getProperty
                ("java.util.concurrent.ForkJoinPool.common.threadFactory");
            String hp = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
            if (pp != null)
                parallelism = Integer.parseInt(pp);
            if (fp != null)
                factory = ((ForkJoinWorkerThreadFactory)ClassLoader.
                           getSystemClassLoader().loadClass(fp).newInstance());
            if (hp != null)
                handler = ((UncaughtExceptionHandler)ClassLoader.
                           getSystemClassLoader().loadClass(hp).newInstance());
        } catch (Exception ignore) {
        }
        if (factory == null) {
            if (System.getSecurityManager() == null)
                factory = defaultForkJoinWorkerThreadFactory;
            else // use security-managed default
                factory = new InnocuousForkJoinWorkerThreadFactory();
        }
        if (parallelism < 0 && // default 1 less than #cores 为parallelism赋值
            (parallelism = Runtime.getRuntime().availableProcessors() - 1) <= 0)
            parallelism = 1;
        if (parallelism > MAX_CAP)
            parallelism = MAX_CAP;
        return new ForkJoinPool(parallelism, factory, handler, LIFO_QUEUE,
                                "ForkJoinPool.commonPool-worker-");
    
}

    private ForkJoinPool(int parallelism,
                         ForkJoinWorkerThreadFactory factory,
                         UncaughtExceptionHandler handler,
                         int mode,
                         String workerNamePrefix) {
        this.workerNamePrefix = workerNamePrefix;
        this.factory = factory;
        this.ueh = handler;
        this.config = (parallelism & SMASK) | mode;//config的赋值
        long np = (long)(-parallelism); // offset ctl counts
        this.ctl = ((np << AC_SHIFT) & AC_MASK) | ((np << TC_SHIFT) & TC_MASK);
    }
    // Mode bits for ForkJoinPool.config and WorkQueue.config
    static final int MODE_MASK    = 0xffff << 16;  // top half of int
    static final int LIFO_QUEUE   = 0;
    static final int FIFO_QUEUE   = 1 << 16;
    static final int SHARED_QUEUE = 1 << 31;       // must be negative
```

其中CompletableFuture默认采用线程池有两种：

一种是：

new ForkJoinPool(parallelism, factory, handler, LIFO_QUEUE,"ForkJoinPool.commonPool-worker-");

第二种是：

来一个Runnable新起一个线程，进行任务处理

根据ForkJoinPool的parallelism参数是否>1来判断



## CopyOnWriteArrayList：

不同的GC分区有不同的实现方式，Yong区使用的是直接复制，Old区使用的是区域移动的方式。

## LinkedHashMap

是在HashMap的基础上增强了了HashMap的Entry结构，并新增了一个Header节点链表，通过遍历这个节点链表来保证Map的Iterator顺序。

## ConcurrentHashMap

### 1.7 Segment数组（继承ReentrantLock）+HashEntry数组 + 链表/红黑树

### 1.8 Node数组 + synchronize/cas + 红黑树

put：

数组位置为空采用cas方法

数组位置不为空采用同步代码块的方式

get:

不加锁

头节点hash值>0为链表；头节点hash值<0为红黑树，并根据数据结构不同，采用不同的插入方法。

### Stream.parallel()

底层是用ForkJoin让任务进行分片处理的

## 表单重复提交问题

1. 为每张表单生成一个表单号，提交的时候进行判断是否重复提交（服务端）
2. 点击后按钮不可用（客户端）

## 找资料时看到的杂七杂八的知识

		//找资料时学到的杂知识：Spring5里面可以通过
		//set BeanDefinition#setInstanceSupplier(目标类::创建目标类的静态方法)来创建类的实例 而不用 静态工厂方法的反射
		//位置在org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBeanInstance
	//        Supplier<?> instanceSupplier = mbd.getInstanceSupplier();
	//		  if (instanceSupplier != null) {
	//			  return obtainFromSupplier(instanceSupplier, beanName);
	//		  }
	//		Supplier<Object> function1 = Object::new;


## Context设计模式

Context内容存放类

```java
public class Context {

    private String name;
    private String cardId;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }
}
```

Context类

```java
public final class ActionContext {

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<Context>() {
        @Override
        protected Context initialValue() {
            return new Context();
        }
    };

    private static class ContextHolder {
        private final static ActionContext actionContext = new ActionContext();
    }

    public static ActionContext getActionContext() {
        return ContextHolder.actionContext;
    }

    public Context getContext() {
        return threadLocal.get();
    }

    private ActionContext(){

    }
}
```

Context存内容实际例子A

```java
public class QueryFromDBAction {

    public void execute() {
        try {
            Thread.sleep(1000L);
            String name = "Alex " + Thread.currentThread().getName();
            ActionContext.getActionContext().getContext().setName(name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

Context存内容实际例子B

```java
public class QueryFromHttpAction {

    public void execute() {
        Context context = ActionContext.getActionContext().getContext();
        String name = context.getName();
        String cardId = getCardId(name);
        context.setCardId(cardId);
    }

    private String getCardId(String name) {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "435467523543" + Thread.currentThread().getId();
    }
}
```

线程构间 && main函数使用

```java
public class ExecutionTask implements Runnable {

    private QueryFromDBAction queryAction = new QueryFromDBAction();

    private QueryFromHttpAction httpAction = new QueryFromHttpAction();

    @Override
    public void run() {

        queryAction.execute();
        System.out.println("The name query successful");
        httpAction.execute();
        System.out.println("The card id query successful");

        Context context = ActionContext.getActionContext().getContext();
        System.out.println("The Name is " + context.getName() + " and CardId " + context.getCardId());
    }
}

public class ContextTest {
    public static void main(String[] args) {
        //1为步长，i为1-5
        IntStream.range(1, 5)
                .forEach(i ->
                        new Thread(new ExecutionTask()).start()
                );
    }
}
```



# 人不可能总是活在过去的，只有往前才会有希望；为了家人，为了将来的爱人。希望新的一年万事顺意，平安无事。
package cn.zs.mstpxu;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @aothor Master_PXu 
 * @date 时间 2021年1月1日下午5:51:50
 * @project_name 项目名 Week004
 * @type_name 类名 HomeWork
 * @function 功能 TODO
 * 思考有多少种方式，在 main 函数启动一个新线程，运行一个方法，拿到这个方法的返回值后，退出主线程
 */
public class HomeWork004 {
	volatile static int shared;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int result;
		//main函数是用户线程非守护线程，main可以先退出。
		//基于callable
		//1:		
		FutureTask<Integer> function1 = new FutureTask<>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				return functionA();
			}
		});
		function1.run();
		
		//2:
//		ExecutorService pool = Executors.newFixedThreadPool(1);//1
//		Future<Integer> function1 = pool.submit(new Callable<Integer>() {
//			@Override
//			public Integer call() throws Exception {
//				return functionA();
//			}
//		});
//		pool.shutdown();
		
		//3:
//		CompletableFuture<Integer> function1 = CompletableFuture.supplyAsync(HomeWork004::functionA);
		try {
			result = function1.get();
			System.out.println("Callable result" + result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//基于共享成员变量
		//4
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				new HomeWork004().shared(1);
			}
		});
		thread.start();
		while(shared == 0) {}
		System.out.println("Shared:" + shared);
		//基于锁
		//5
		CountDownLatch latch = new CountDownLatch(1);
		Thread threadCountDown = new Thread(new Runnable() {
			@Override
			public void run() {
				new HomeWork004().countdown(latch,2);
			}
		});
		threadCountDown.start();
		try {
			latch.await();
			System.out.println("countdown：" + shared);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//6
		int snapShoot = shared;
		Lock lock = new ReentrantLock(true);
		Thread lockThread = new Thread(new Runnable() {
			@Override
			public void run() {
				lock.lock();
				new HomeWork004().lockThread(3);
				lock.unlock();
			}
		});
		lockThread.start();
		try {
			//join的原理是在join方法中调用了wait(),但我们没有见到显示的notify，所以notify也是在JDK源码中的
			//线程运行完了会执行清理逻辑源码(c)JavaThread::exit(bool destroy_vm, ExitType exit_type)
			//中的  ensure_join(this); 方法进行清理工作
			//调用了 lock.notify_all(thread);
			//后续方法如果依赖于thread的结果，则可以使用thread.join
			lockThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
//		while(snapShoot == shared) {}
		System.out.println("lockThread：" + shared);
		//主线程须进行旧值的保存；或者在Thread操作完成前，主线程需要阻塞等待Thread结果返回。否则由于运行时间片抢占等不可人为控制的原因，会出现最终结果并不是预期结果的的情况
		//类似的操作还有很多，不过基本都是这样千篇一律的写法，这里多写一些为了加强学习API的使用。
	}
	
	public void lockThread(final int value) {
		HomeWork004.shared = value;
	}
	
	public void countdown(final CountDownLatch countDownLatch,final int value) {
		countDownLatch.countDown();
		HomeWork004.shared = value;
	}
	public void shared(final int value) {
		synchronized(HomeWork004.class) {
			HomeWork004.shared = value;
		}
	}
	public static int functionA() {
		return 1;
	}
}

package com.iclick.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestRunnable {
	public static void main(String[] args) {
		ExecutorService pool = Executors.newCachedThreadPool();
		Count c1 = new Count();
		Count c2 = new Count();
		Count c3 = new Count();
		Count c4 = new Count();
		Count c5 = new Count();
		Count c7 = new Count();
		Count c8 = new Count();
		Count c9 = new Count();
		pool.execute(c1);
		pool.execute(c2);
		pool.execute(c3);
		pool.execute(c4);
		pool.execute(c5);
		pool.execute(c7);
		pool.execute(c8);
		pool.execute(c9);

		// 关闭线程池
		pool.shutdown();

	}

}

class Count implements Runnable {

	static Businesss bs = new Businesss();

	public void run() {

		while (bs.getN() <= 3000) {
			try {
				bs.count();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

class Businesss {
	private int n = 1;
	private Lock lock = new ReentrantLock();
	private Condition _draw=lock.newCondition();

	public  void count() {
		lock.lock();
		try {
		  if(n>3000){
			  _draw.await();
		  } else{	System.out.println(Thread.currentThread().getName() + "正在计算：" + n);
			n++;
		  }
			_draw.signalAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

	}

	public int getN() {
		return n;
	}


}
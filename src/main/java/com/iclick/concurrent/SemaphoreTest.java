package com.iclick.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	public static void main(String[] args) {
		  MyPool myPool = new MyPool(20); 
          //创建线程池 
          ExecutorService threadPool = Executors.newFixedThreadPool(2); 
          MyThread1 t1 = new MyThread1("任务A", myPool, 3); 
          MyThread1 t2 = new MyThread1("任务B", myPool, 12); 
          MyThread1 t3 = new MyThread1("任务C", myPool, 7); 
          //在线程池中执行任务 
          threadPool.execute(t1); 
          threadPool.execute(t2); 
          threadPool.execute(t3); 
          //关闭池 
          threadPool.shutdown(); 
	}

}
class MyPool{
	private Semaphore  sp;  //池相关的信号量
	public MyPool(int size){
		this.sp=new Semaphore(size);
	}
	
	public Semaphore  getsp(){
		return sp;
	}

    public void setSp(Semaphore sp) { 
            this.sp = sp; 
    }

}

class MyThread1  extends Thread{
	private  String threadname;
	private MyPool  pool;
	private int x;
	public  MyThread1(String threadname,MyPool pool,int x){
		this.threadname=threadname;
		this.pool=pool;
		this.x=x;
	}
	@Override
	public void run(){
		try{
			  //从此信号量获取给定数目的许可 
			pool.getsp().acquire(x);
            //todo：也许这里可以做更复杂的业务 
			System.out.println(threadname+"成功获取了"+x+"个许可");
		}catch(InterruptedException e){

			e.printStackTrace();
		}finally{
		    //释放给定数目的许可，将其返回到信号量。
			pool.getsp().release(x);
			System.out.println(threadname+"释放了"+x+"个许可");
		}
	}
	
	
	
	
}

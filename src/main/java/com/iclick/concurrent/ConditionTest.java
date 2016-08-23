package com.iclick.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ConditionTest {
	public static void main(String[] args) {
		  //创建并发访问的账户 
        MyCount1 myCount = new MyCount1("95599200901215522", 10000); 
        //创建一个线程池 
        ExecutorService pool = Executors.newFixedThreadPool(2); 
        Thread t1 = new SaveThread("张三", myCount, 2000); 
        Thread t2 = new SaveThread("李四", myCount, 3600); 
        Thread t3 = new DrawThread("王五", myCount, 2700); 
        Thread t4 = new SaveThread("老张", myCount, 600); 
        Thread t5 = new DrawThread("老牛", myCount, 1300); 
        Thread t6 = new DrawThread("胖子", myCount, 800); 
        //执行各个线程 
        pool.execute(t1); 
        pool.execute(t2); 
        pool.execute(t3); 
        pool.execute(t4); 
        pool.execute(t5); 
        pool.execute(t6); 
        //关闭线程池 
        pool.shutdown(); 
	}

}

class MyCount1{
	private String id;//账号
	private int cash;//账户余额
	private Lock  lock=new ReentrantLock(); //账户锁
	private Condition _save=lock.newCondition();//存款条件
	private Condition _draw=lock.newCondition();//取款条件
	public MyCount1(String id,int cash){
		this.id=id;
		this.cash=cash;
	}
	  /** 
     * 存款 
     * 
     * @param x        操作金额 
     * @param name 操作人 
     */ 
	public void saving(int x,String name){
		lock.lock();
		if(x>0){
			cash+=x;
			System.out.println(name+"存款"+x+",当前余额为:"+cash);
			
		}
		_draw.signalAll();
		lock.unlock();
	}
	
	  /** 
     * 取款 
     * 
     * @param x        操作金额 
     * @param name 操作人 
     */ 
	
	public void drawing(int x,String name){
		lock.lock();
		try{
			if(cash-x<0){
				_draw.await();
			}else{
				cash-=x;
				System.out.println(name + "取款" + x + "，当前余额为:" + cash); 
			}
			_save.signalAll();
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
}

//存款线程类
class SaveThread extends Thread{
	private String name;
	private MyCount1 mycout;
	private int x;
	public SaveThread(String name,MyCount1 mycount,int x){
		this.name=name;
		this.mycout=mycount;
		this.x=x;
	}
	
	public void run(){
		mycout.saving(x, name);
	}
	
}

//取款线程类
class  DrawThread extends Thread{
	private String name;                //操作人 
    private MyCount1 myCount;        //账户 
    private int x;                            //存款金额 

    DrawThread(String name, MyCount1 myCount, int x) { 
            this.name = name; 
            this.myCount = myCount; 
            this.x = x; 
    } 

    public void run() { 
            myCount.drawing(x, name); 
    } 
}

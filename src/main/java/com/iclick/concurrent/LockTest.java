package com.iclick.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.netlib.util.booleanW;

public class LockTest {
	
	public static void main(String[] args) {
		MyCount  myCount=new MyCount("95599200901215522", 10000); 
		System.out.println("初始值是："+myCount.toString());
		ReadWriteLock  lock=new ReentrantReadWriteLock(false);
		//有多少个线程就会创建多少个线程，如下图会创建四个线程
		ExecutorService pool=Executors.newCachedThreadPool();
		 //创建一些并发访问用户，一个信用卡，存的存，取的取，好热闹啊 
        User u1 = new User("美旭", myCount, -4000, lock,false); 
        User u2 = new User("梦陵", myCount, 6000, lock,true); 
        User u3 = new User("妈", myCount, -8000, lock,true); 
        User u4 = new User("爸", myCount, 800, lock,false); 
        User u5 = new User("美根", myCount, 800, lock,true); 
        pool.execute(u1); 
        pool.execute(u2); 
        pool.execute(u3); 
        pool.execute(u4); 
        pool.execute(u5);
        //关闭线程池 
        pool.shutdown(); 
	}

}

class  MyCount{
	private String id;  //账号
	private  int cash;  //账户余额 
	public  MyCount(String id,int cash){
		this.id=id;
		this.cash=cash;
	}
	public String getid(){
		return id;
	}
	public void setid(String id){
		this.id=id;
	}
	public int getcash(){
		return cash;
	}
	public  void setcash(int cash){
		this.cash=cash;
	}
	
	@Override
	public String toString(){
	  return "MyCount{" + 
                "oid='" + id + '\'' + 
                ", cash=" + cash + 
                '}'; 
				
	}
}

class User  implements Runnable{
	private String name;
	private MyCount myCount;
	private  int iocash;
	private ReadWriteLock myLock;
	private boolean ischeck; //是否查询
	public User(String name,MyCount myCount,int iocash,ReadWriteLock  myLock,boolean ischeck){
		this.name=name;
		this.myCount=myCount;
		this.iocash=iocash;
		this.myLock=myLock;
		this.ischeck=ischeck;
	}
	@Override
	public void run(){
		if(ischeck){
			//获取读锁 
			myLock.readLock().lock();
			System.out.println("读："+name+"正在查询"+myCount+"账户余额:"+myCount.getcash());
	      //释放读锁 
			myLock.readLock().unlock();
		}else{
			myLock.writeLock().lock();
		     //执行现金业务 
            System.out.println("写：" + name + "正在操作" + myCount + "账户，金额为" + iocash + "，当前金额为" + myCount.getcash()); 
            myCount.setcash(myCount.getcash() + iocash); 
            System.out.println("写：" + name + "操作" + myCount + "账户成功，金额为" + iocash + "，当前金额为" + myCount.getcash()); 
            //释放写锁 
            myLock.writeLock().unlock(); 
			
			
			
		}
		
	}
	
	
}



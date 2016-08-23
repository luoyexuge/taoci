package com.iclick.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CallableTest {

	
  public static void main(String[] args) throws InterruptedException, ExecutionException {
	ExecutorService  pool=Executors.newFixedThreadPool(2);
	Callable c1=new MyCallable("meixu");
	Callable c2=new MyCallable("mengling");
	Future f1=pool.submit(c1);
	Future f2=pool.submit(c2);
	System.out.println(">>>>>"+f1.get().toString());
	System.out.println(">>>>>"+f2.get().toString());
	
}
}

class MyCallable implements Callable{
	private String old;
	public MyCallable(String old){
		this.old=old;
	}
	@Override
	public  Object  call() throws Exception{
		return old+":任务返回的内容";
	}
	
}
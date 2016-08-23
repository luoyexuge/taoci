package com.iclick.concurrent;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;



public class BlockingQueueTest {
	public static void main(String[] args) throws InterruptedException {
		BlockingDeque<Integer>  bDeque=new  LinkedBlockingDeque<Integer>(10);
		for (int i = 0; i < 30; i++) {
			bDeque.put(i);
			System.out.println("向阻塞栈中添加了元素:" + i);
		}
		System.out.println("程序到此运行结束，即将退出----");
	}
}

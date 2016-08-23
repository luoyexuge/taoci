package com.iclick.concurrent;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class BlockingQueueConsumer {
	public static void main(String[] args) {
	final Random random = new Random();
	final BlockingQueue<Integer> queue = new LinkedBlockingDeque<Integer>(3);

	class Producer implements Runnable {
		public void run() {
			while (true) {
				try {
					int i = random.nextInt(100);
					
					queue.put(i);
					if (queue.size() == 3) {
						System.out.println("full");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	class  Consumer implements  Runnable {
		public  void  run(){
			while(true){
				try{
					int result=queue.take();
					System.out.println("消费的数据是："+result);
					Thread.sleep(1000);
					
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
	

		
		new Thread(new Producer()).start();
		new Thread(new Consumer()).start();
		
	}

}

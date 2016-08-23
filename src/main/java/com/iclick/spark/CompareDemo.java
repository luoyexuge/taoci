package com.iclick.spark;
public class CompareDemo {
	
	public static void main(String args[]){
		new   Thread(new Runnable() {
			public void run() {
				for(int i=0;i<10;i++){
					try {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						
							e.printStackTrace();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("进程A");
					
				}
				
			}
		}).start();
		new   Thread(new Runnable() {
			public void run() {
				
				for(int i=0;i<10;i++){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					
						e.printStackTrace();
					}
					System.out.println("进程B");
					
				}
				
			}
		}).start();
	}

}

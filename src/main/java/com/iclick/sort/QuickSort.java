package com.iclick.sort;
public class QuickSort {
	
	//产生随机生成数
	public static int[] randomarray(int arrayLength,int maxNum){
		int[] array=new int[arrayLength];
		for(int i=0;i<array.length;i++){
			array[i]=(int)(Math.random()*maxNum);
		}
		
		return array;
	}
	
	//数据交换
	public  static void swap(int[] data,int i,int j){
		int temp=data[i];
		data[i]=data[j];
		data[j]=temp;
		
	}
	//打印数据
	public static void printArray(int[] array){
		for(int element:array){
		    System.out.print(element+" ");
		}
		System.out.println();
	}
	
	//快速排序
	
	private static int getMiddle(int[] array,int low,int hight){
		int  temp=array[low];
		while(low<hight){
			while(low<hight && array[hight]>=temp){
				hight--;
			}
			array[low]=array[hight];
			
			while(low<hight && array[low]<=temp){
				low++;
			}
			array[hight]=array[low];
		}
		array[low]=temp;
		return  low;
		
	}
	
	
	private static void quickSort(int[] array,int low ,int hight){
		if(array.length>0){
			if(low<hight){
				int middle=getMiddle(array, low, hight);
				quickSort(array, 0, middle-1);
				quickSort(array, middle+1, hight);
				
				
			}
		}
		
		
	}
	
	public static void main(String[] args) {
		int[] test=randomarray(50,100);
		System.out.println("快速排序前的结果");
		printArray(test);
		quickSort(test, 0, test.length-1);
		System.out.println("快速排序后的结果");
		printArray(test);
	}
	
	
	

}

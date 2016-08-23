package com.iclick.featureselect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;


public  class Test {
	public static void main(String[] args) {

	System.out.println("声明一个数据");
	
	String[] aArray=new String[5];
	String[] bArray={"a","b","c","d","e"};
	String[] cArray=new String[]{"a","b","c","d"};
	
	System.out.println("输出一个数组");
	int[] intarray={1,2,3,4,5};
	String intarrayString=Arrays.toString(intarray);
	System.out.println(intarrayString);
	
	
	System.out.println("从一个数组创建数组列表 ");
	ArrayList<String> arrayList=new ArrayList<String>(Arrays.asList(bArray));
	System.out.println(arrayList);
	
	
	System.out.println("检查一个数组是否包含某个值 ");
	boolean  b=Arrays.asList(bArray).contains("a");
	System.out.println(b);

	System.out.println("连接两个数组 ");
	int[]  intArray={1,2,3,4,5};
	int[]  intArray2={6,7,8,9};
	int[] combinedIntArray=ArrayUtils.addAll(intArray, intArray2);
	System.out.println(Arrays.toString(combinedIntArray));

	System.out.println("把提供的数组元素放入一个字符串 ,类似于python中join操作");
	String j=StringUtils.join(new String[]{"a","b","c"},":");
	System.out.println("链接后的结果是："+j);
	
	
	System.out.println("列表转化为数组的操作");
	String[] stringArray={"a","b","a","b","e"};
	ArrayList<String> arrayList1=new ArrayList<String>(Arrays.asList(stringArray));
	System.out.println(Arrays.toString(arrayList1.toArray()));
	
	System.out.println("将一个数组转换为集（set） ");
	HashSet<String> hashSet=new HashSet<String>(Arrays.asList(stringArray));
	System.out.println("转化为set的的集合是："+hashSet);
	
	System.out.println("逆向一个数组 ");
	int[]  intarray1={1,9,3,4,5};
	ArrayUtils.reverse(intarray1);
	System.out.println(Arrays.toString(intarray1));
	
	System.out.println("移除数组中的元素 ");
	int[]  intarray3={1,9,3,4,5};
	int[] removed=ArrayUtils.removeElement(intarray3,9);
	System.out.println(Arrays.toString(removed));
	
	
	}

}

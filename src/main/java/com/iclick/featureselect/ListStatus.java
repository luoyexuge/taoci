package com.iclick.featureselect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;
import org.apache.spark.ml.ann.SoftmaxFunction;;

public class ListStatus {
	public static double[]  softmax(double[]  x){
		double max=0.0;
		double  sum=0.0;
//		double result[]=new double[x.length];
		for(double element:x){
			if(max<element){
				max=element;
			}
		}
		for(int i=0;i<x.length;i++){
			x[i]=Math.exp(x[i]-max);
			sum+=x[i];
		}
		for(int i=0;i<x.length;i++){
			x[i]=x[i]/sum;
		}
		return x;
		
	}
  
	public static void main(String[] args)  {
		double[] x={1.0,1.5,1.49,0.2};
		System.out.println(Arrays.toString(softmax(x)));
		
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("a", 1);
		
		System.out.println(map.get("a").toString());
		System.out.println(map.get("b"));
	}
}

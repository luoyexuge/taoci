package com.iclick.featureselect;
import  org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import  org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import  org.apache.hadoop.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

import kafka.utils.immutable;
public class HaoopFilSystemTest {
	//删除文件
	     public static void delete(String filePath) throws IOException{
	        Configuration conf = new Configuration();
	         FileSystem fs = FileSystem.get(conf);
	         Path path = new Path(filePath);
	        boolean isok = fs.deleteOnExit(path);
	         if(isok){
	             System.out.println("delete ok!");
	        }else{
	             System.out.println("delete failure");
	        }
	          fs.close();
	    }
	
	//读取文件从hdfs中
	public  static void readFile(String filePath) throws IOException{
		Configuration  conf=new Configuration();
		conf.setBoolean("dfs.support.append",true);
		FileSystem  fs=FileSystem.get(conf);
		Path  srcPath=new Path(filePath);
		InputStream in=null;
		try{
			in=fs.open(srcPath);
			
			BufferedReader	buff = new BufferedReader(new InputStreamReader(in,"GBK"));
			String str=buff.readLine();
			 while (str != null) {
			      System.out.println(str);
			        str = buff.readLine();
			      }
			
			
		}finally{	
			IOUtils.closeStream(in);
		}
	}
		
	
	//写文件到hdfs上的文件里面
	
	public static  void  writefile() throws IOException{
		Configuration  conf=new Configuration();
		conf.setBoolean("dfs.support.append",true);
		FileSystem  fs=FileSystem.get(conf);
		Path path=new Path("d:/tt.txt");
		
		if(fs.exists(path)){
			System.out.println("file  alreay exist");
		return ;	
		}
		FSDataOutputStream  out=fs.create(path);
		InputStream in=   new  BufferedInputStream(new FileInputStream(new File("d:/tt1.txt")));
		byte[]  by=new byte[in.available()];
		in.read(by);
		out.write(by);
		
		
		in.close();
		out.close();
		fs.close();
	}
	
	
	
	
	
	
    public static void main(String[] args) throws IOException {
//		String  hdfs_pathString="d:/tt.txt";
//		  readFile(hdfs_pathString);
//		  writefile();
//		 
    	readFile("d:/tt.txt");
//    	writefile();
		String test="中国人民";
    	byte[]  b=test.getBytes();
    	System.out.println(Arrays.toString(b));
    	
       String  test1=new String(b);
       System.out.println(test1);
	}
    
}

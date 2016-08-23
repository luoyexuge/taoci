package com.iclick.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
public class PutMeger {
	public static void main(String[] args) {
		Configuration conf=new Configuration();
		try{
			FileSystem  hdfs=FileSystem.get(conf);
			FileSystem  local=FileSystem.getLocal(conf);
			Path   inputDir=new Path("D:\\机器学习");
			Path  hdfsFile=new Path("D:\\tmp");
			
			
			//FileStatus的listStatus()方法获得一个目录中的文件列表
			FileStatus[]   inputFile=local.listStatus(inputDir);
			for(FileStatus  file:inputFile){
				System.out.println(file);
			}
			//生成HDFS输出流
			FSDataOutputStream out=hdfs.create(hdfsFile);
			for(int i=0;i<inputFile.length;i++){
				System.out.println(inputFile[i].getPath().getName());
				FSDataInputStream in=local.open(inputFile[i].getPath());
				byte[] buffer=new  byte[256];
				int bytesRead=0;
				while((bytesRead=in.read(buffer))>0){
					out.write(buffer,0,bytesRead);
				}
				in.close();
			}
			
		}catch(Exception  e){
			e.printStackTrace();
		}
	}
}

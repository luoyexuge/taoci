package com.iclick.spark;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.mapred.FileOutputCommitter;

public class SerializableDemo1 {
	
	public static void main(String args[]) throws Exception{
		
		
		
	  Integer  in=	new  Double(23.6).intValue();
	  System.out.println(in);
		User user=new User();
		user.setName("hillis");
		user.setAge(23);
		System.out.println(user);
		ObjectOutputStream  _oos=null;
		_oos=new ObjectOutputStream(new FileOutputStream("d:\\wilson.zhou\\Desktop\\filwrite.txt"));
		_oos.writeObject(user);
		_oos.close();
	
	   
	   ObjectInputStream ois=null;
	   ois=new ObjectInputStream(new FileInputStream("d:\\wilson.zhou\\Desktop\\filwrite.txt"));
     User user1=(User)ois.readObject();
     System.out.println(user1);
	}
	
	
	
	

}

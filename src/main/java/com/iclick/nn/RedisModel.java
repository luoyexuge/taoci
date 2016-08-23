package com.iclick.nn;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.neuroph.nnet.Perceptron;
import  redis.clients.jedis.Jedis;
public class RedisModel {
	private  static  Jedis   jedis=null;
	public static   byte[]  modeltoBytes(Object  object){
		ObjectOutputStream  oos=null;
		ByteArrayOutputStream  baos=null;
		try{
			baos=new ByteArrayOutputStream();
			oos =new ObjectOutputStream(baos);
			oos.writeObject(object);
			byte[] bytes=baos.toByteArray();
			return bytes;
		}catch(Exception  e){
			e.printStackTrace();
		}
		return  null;
		
	}
	
	public static     Object bytestoModel(byte[]  bytes){
		ByteArrayInputStream  bais=null;
		try{
			bais=new  ByteArrayInputStream(bytes);
			ObjectInputStream  ois=new  ObjectInputStream(bais);
			return  ois.readObject();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	} 
	
	public static  void   before()throws  Exception{
		jedis=new Jedis("127.0.0.1",6379);
	}
	public static  void  setmodeltoredis(String modelkey,Object object) throws Exception{
		before();
	jedis.set(modelkey.getBytes(), modeltoBytes(object));
	}
	
	public   static  Object  getmodelfromredis(String modelkey){
		byte[]  bpModel=jedis.get(modelkey.getBytes());
		Object  model= bytestoModel(bpModel);
		return   model;
		
	}
	
	public static void main(String[] args) throws Exception {
		setmodeltoredis("test","aaaa");
		String result=(String)getmodelfromredis("test");
		System.out.println(result);
		
	}

}

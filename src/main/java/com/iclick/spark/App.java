package com.iclick.spark;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class App {
	public  static void main(String args[]){
		
		System.out.println("hello world");
		SimpleDateFormat  sdf=new SimpleDateFormat("yy-MM-dd");
		
		try {
			System.out.println(getBeforday(sdf, "16-03-30", 10));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		

		
	}

	
	public  static String  getBeforday(SimpleDateFormat sdf,String  date,int day) throws ParseException{
		
		  Date time=sdf.parse(date);
		  time.setTime(time.getTime()-day*24*60*60*1000);
		  
		  return sdf.format(time);				 
	}
	
}

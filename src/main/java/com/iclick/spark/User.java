package com.iclick.spark;

import java.io.Externalizable;
import java.io.Serializable;

public class User implements  Externalizable{
	
	private  transient  String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
 public void writeExternal(java.io.ObjectOutput out) throws java.io.IOException {
		
	out.writeObject(name);
	out.writeInt(age);
	}
	
	public void readExternal(java.io.ObjectInput in) throws java.io.IOException ,ClassNotFoundException {
		name=(String) in.readObject();
		age=in.readInt();
	};
	
	@Override
	public  String toString(){
		 return "User{" +
	                "name='" + name + '\'' +
	                ", age=" + age +
	                '}';
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}

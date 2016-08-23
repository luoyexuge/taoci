package com.iclick.nn;
import java.io.Serializable;
import org.neuroph.nnet.Perceptron;
public class Model  implements  Serializable{
//	private static final long serialVersionUID = -6034659347477968158L;
	private  Perceptron    network;
	
	public Model(){	
	}
	public  Model(Perceptron  network){
		this.network=network;
	}
	public Perceptron getNetwork() {
		return network;
	}
	public void setNetwork(Perceptron network) {
		this.network = network;
	}
    public static void main(String[] args) {
		int  arr[]=new int[]{8,2,1,0,3};
		int[] index=new int[]{2,0,3,2,4,0,1,3,2,3,3};
		String tel="";
		for(int i:index){
			tel+=arr[i];
		}
		System.out.println(tel);
	}
	

}

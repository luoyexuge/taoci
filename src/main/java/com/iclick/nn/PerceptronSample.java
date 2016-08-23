package com.iclick.nn;

import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.Perceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
public class PerceptronSample{
	public  static DataSet  read_iris(String file) throws IOException{
		BufferedReader  buff=new  BufferedReader(new FileReader(file));
		String line=null;
		DataSet trainingSet = new DataSet(4, 1);
		while((line=buff.readLine())!=null){
			String[] str=line.split(" ");
			int  label= str[str.length-1].equals("\"setosa\"") ? 1 :0;
			
			
			trainingSet.addRow(new DataSetRow(new double[] {Double.parseDouble(str[1]), Double.parseDouble(str[2]),Double.parseDouble(str[3])
					,Double.parseDouble(str[4])},	new double[] { label }));
			
		}
		buff.close();
		System.out.println("data has getted");
		return trainingSet;
		
		
	}
	
	
	public static void main(String args[]) throws IOException {
		DataSet  trainingSet=read_iris("d:\\wilson.zhou\\Desktop\\iris.txt");
	    System.out.println("data has   getted");
/*
		trainingSet.addRow(new DataSetRow(new double[] { 0, 0 },
				new double[] { 0 }));
		trainingSet.addRow(new DataSetRow(new double[] { 0, 1 },
				new double[] { 0 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 0 },
				new double[] { 0 }));
		trainingSet.addRow(new DataSetRow(new double[] { 1, 1 },
				new double[] { 1 }));*/
		// create perceptron neural network
	/*	NeuralNetwork myPerceptron = new Perceptron(4, 1);

		// learn the training set
		myPerceptron.learn(trainingSet);
		// test perceptron
		System.out.println("Testing trained perceptron");
		testNeuralNetwork(myPerceptron, trainingSet);
		// save trained perceptron
		myPerceptron.save("mySamplePerceptron.nnet");
		// load saved neural network
		NeuralNetwork loadedPerceptron = NeuralNetwork
				.createFromFile("mySamplePerceptron.nnet");

		// test loaded neural network
		System.out.println("Testing loaded perceptron");
		testNeuralNetwork(loadedPerceptron, trainingSet);*/
	}

	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet tset) {
		for (DataSetRow dataRow : tset.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()));
			System.out.println(" Output: " + Arrays.toString(networkOutput));
		}
	}
}
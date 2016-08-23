package com.jingde.go
//package excercise;
/**
  * Created by wilson.zhou on 2016/5/15.
  */

//import org.apache.log4j.{Level, Logger}
//import org.apache.spark.{SparkContext, SparkConf}
//import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS
//import org.apache.spark.mllib.linalg.{Vector, Vectors}
//import org.apache.spark.mllib.regression.LabeledPoint
//import org.apache.spark.sql.SQLContext
object ML_Logisttic {
  def main(args: Array[String]) {
    val list=List(1,3,5)
    
    val list1=List("a","b","c")
    
    println(list.fold(0)((a,b)=>a+b))
    
    
    //利用foldLeft反转list
    val  li=list.foldLeft(List.empty[Int])((a,b)=>b::a)
    println(li)   //List(5, 3, 1)
  
  //字符串测试  
   val  result= list1.fold("X"){
      (a,b)=>
        val  tt=b  match {
          case "a"=>"A"
          case "b"=>"B"
          case _=>"蛋疼"
        }
      a+tt
      
    }
    
    println(result)
    
  }
  //累加
   println((1 to 100).foldLeft(50)((x,y)=>x+y))
  

}

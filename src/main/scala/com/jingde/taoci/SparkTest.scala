package com.jingde.taoci
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
import java.sql.DriverManager
import org.apache.spark.rdd.JdbcRDD

object SparkTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "mysql")
    val test = sc.parallelize(Array(("a","A","1"), ("a","B","2"), ("b","C","4")), 2)
   val  df=sc.wholeTextFiles("D:\\temp\\ks\\")
   df.foreach(println)    //结果 (file:/D:/temp/ks/jac.txt,javac++sdssdajava)
   /*(file:/D:/temp/ks/test1.txt,javac++) (file:/D:/temp/ks/test2.txt,javac++)*/
   df.map{
       case(k,v)=>
         v}.map(x=>(x,1)).reduceByKey(_+_).foreach(println)    ///结果(javac++,1)(java c++ sds sda java,1)(javac++,1)
         
   val df1=sc.textFile("D:\\temp\\ks\\")
   
   df1.map(x=>(x,1)).reduceByKey(_+_).foreach(println)  ///结果(sda,1)(sds,1)(java,4)(c++,3)
    
  }
}
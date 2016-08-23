package com.jingde.taoci
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
object AggregaByKey {
  def  seq(a:Int,b:Int)={
    Math.max(a,b)  
  }
  def comb(a:Int,b:Int):Int={
     a+b
  } 
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf = new SparkConf().setMaster("local[*]").setAppName("itemBase")
    val sc = new SparkContext(conf)
    val data = sc.parallelize(List((1,3),(1,2),(1, 4),(2,3)))
    println("开始计算")
    data.aggregateByKey(-1)(seq,comb).values.collect().foreach(println)
  }
}
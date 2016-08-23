package com.jingde.sparkstream
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import  org.elasticsearch.spark._
import org.apache.spark.streaming.{StreamingContext,Seconds}
import scala.collection.mutable.Map
object ElasticSearchTest {
  def main(args: Array[String]): Unit = {
      Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf=new  SparkConf().setMaster("local[2]").setAppName("newworkwordcont").
    set("es.index.auto.create", "true").set("es.nodes", "192.168.255.128").set("es.port","9200")
    val sc=new SparkContext(conf)
   // 创建 StreamingContext，5秒一个批次
    val ssc = new StreamingContext(sc, Seconds(5))
    val lines = ssc.textFileStream("d:\\temp")
    // 对每一行数据执行 Split 操作
    
    
    // 统计 word 的数量
    val pairs = lines.map(word => (word, 1))
    // 输出结果
    pairs.foreachRDD{
      x =>
        x.saveToEs("spark/docs")
    }
    
    ssc.start()
    ssc.awaitTermination()

  }
  
}
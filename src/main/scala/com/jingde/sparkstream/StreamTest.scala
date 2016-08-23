package com.jingde.sparkstream
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.streaming.{Seconds,StreamingContext}
import  org.apache.spark.streaming.StreamingContext._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
object StreamTest {
           // 定义更新状态方法，参数values为当前批次单词频度，state为以往批次单词频度
  def main(args: Array[String]): Unit = {
     Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf=new  SparkConf().setMaster("local[2]").setAppName("newworkwordcont")
     val sc = new SparkContext(conf)
    val ssc=new StreamingContext(sc,Seconds(5))
      val sqc = new SQLContext(sc)
     import  sqc.implicits._
    val lines=ssc.textFileStream("D:\\temp\\ks\\")
  
//    lines.repartition(1).
//    val  words=lines.map(x=>(x,1)).reduceByKey(_+_)
   lines.transform( rdd => {
        
            rdd.toDF("word").registerTempTable("logstash")
        val sqlreport = sqc.sql("SELECT word, COUNT(word) FROM logstash  group by word")
        sqlreport.map(r => ( r(0).toString,r(1).toString.toInt))
      
    }).print()
//    words.print()
    

    ssc.start()
    ssc.awaitTermination()
    
  }
}
package com.jingde.taoci
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.streaming._;
import org.apache.spark.{ SparkConf, SparkContext }

import org.apache.spark.streaming.StreamingContext;

object StreamingWordCount{
  def main(args: Array[String]): Unit = {
     Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
     val conf = new SparkConf().setAppName("test").setMaster("local")
      val ssc = new StreamingContext(conf,Seconds(5))
   val  lines=ssc.textFileStream("d:\\wilson.zhou\\Desktop\\wordcount")
   val words = lines.flatMap(_.split(" "))
   val word= words.map(x => (x , 1)).reduceByKey(_+_)
    word.print()
    ssc.start()              // Start the computation
    ssc.awaitTermination()  // W
  }
  
}
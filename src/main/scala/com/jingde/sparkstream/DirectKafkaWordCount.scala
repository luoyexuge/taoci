package com.jingde.sparkstream
import kafka.serializer.StringDecoder
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.kafka.cluster.KafkaManager


object DirectKafkaWordCount {

  def processRdd(rdd: RDD[(String, String)]): Unit = {
    val lines = rdd.map(_._2)
    val wordCounts = lines.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.foreach(println)
  }

  def main(args: Array[String]) {
   

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DirectKafkaWordCount")
    sparkConf.setMaster("local[*]")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition", "5")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    val ssc = new StreamingContext(sparkConf, Seconds(2))

    // Create direct kafka stream with brokers and topics
    val topics = Set("mytopic")
    val groupId="group"
    val kafkaParams =Map("metadata.broker.list"->"localhost:9092" ,
        "auto.offset.reset" -> "smallest" ,"group.id"->"mytopic")


    val km = new KafkaManager(kafkaParams)

    val messages = km.createDirectStream[String, String, StringDecoder, StringDecoder](
      ssc, kafkaParams, topics)

    messages.foreachRDD(rdd => {
      if (!rdd.isEmpty()) {
        // 先处理消息
//        processRdd(rdd)
        // 再更新offsets
        km.updateZKOffsets(rdd)
      }
    })
    
    val  clickHashKey="sparktest"
    
    val messages1=messages.map(_._2).map(x=>(x,1)).reduceByKey(_+_)
      messages1.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair => {
         
          // Redis configurations
          val maxTotal = 10
          val maxIdle = 10
          val minIdle = 1
          val redisHost = "127.0.0.1"
          val redisPort = 6379
          val redisTimeout = 30000
          //          val dbIndex = 1
          InternalRedisClient.makePool(redisHost, redisPort, redisTimeout, maxTotal, maxIdle, minIdle)
         
          val uid = pair._1
          val clickCount = pair._2
          val jedis =InternalRedisClient.getPool.getResource
//          jedis.select(dbIndex)
       
          jedis.hincrBy(clickHashKey, uid, clickCount)
          InternalRedisClient.getPool.returnResource(jedis)
        })
      })
    })
    
    
    

    ssc.start()
    ssc.awaitTermination()
  }
}
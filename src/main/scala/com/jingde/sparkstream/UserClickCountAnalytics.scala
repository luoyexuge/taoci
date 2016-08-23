package com.jingde.sparkstream
import  org.apache.spark.{SparkConf,SparkContext}
import kafka.serializer.StringDecoder 
import org.apache.spark.streaming.{Seconds,StreamingContext}
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.storage.StorageLevel
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.JSON
object UserClickCountAnalytics {
  def main(args: Array[String]): Unit = {
    var masterUrl = "local[1]"

    if (args.length > 0) {

      masterUrl = args(0)

    }

    // Create a StreamingContext with the given master URL

    val conf = new SparkConf().setMaster(masterUrl).setAppName("UserClickCountStat")

    val ssc = new StreamingContext(conf, Seconds(5))


    // Kafka configurations

    val topics = Set("davidtopic")

    val brokers = "192.168.118.20:9092"

    val kafkaParams = Map[String, String](

      "metadata.broker.list" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder",
       "auto.offset.reset"->"smallest"  
    )


    val dbIndex = 1

    val clickHashKey = "app::users::click"


    // Create a direct stream

    val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics)
//    KafkaUtils.createDirectStream(jssc, keyClass, valueClass, keyDecoderClass, valueDecoderClass, recordClass, kafkaParams, fromOffsets, messageHandler)
    val events = kafkaStream.flatMap(line => {
  
      val data = JSON.parseObject(line._2)

      Some(data)

    })

    events.print()

    // Compute user click times

    val userClicks = events.map(x => (x.getString("uid"), x.getString("click_count").toInt)).reduceByKey(_ + _)

/*    userClicks.foreachRDD(rdd => {
      rdd.foreachPartition(partitionOfRecords => {
        partitionOfRecords.foreach(pair => {
          val uid = pair._1
          val clickCount = pair._2
          val jedis = RedisClient.pool.getResource
          jedis.select(dbIndex)
          jedis.hincrBy(clickHashKey, uid, clickCount)
          RedisClient.pool.returnResource(jedis)

        })

      })

    })*/
    ssc.start()
    ssc.awaitTermination()

  }
}
package com.jingde.sparkstream
import java.util.Properties
import kafka.javaapi.producer.Producer
import kafka.producer.KeyedMessage
import kafka.producer.ProducerConfig
import scala.util.Random
import kafka.producer.KeyedMessage
import scala.io.Source
import com.alibaba.fastjson.JSON
import  com.alibaba.fastjson.JSONObject
import kafka.producer.KeyedMessage

object KafkaEventProducer_imageBj {

  def main(args: Array[String]): Unit = {
    val topic = "mytopic"
    val brokers = "localhost:9092"
    val props = new Properties()
    props.put("metadata.broker.list", brokers)
    props.put("serializer.class", "kafka.serializer.StringEncoder")    //没有此行会爆java.lang.String cannot be cast to [B 错误
 
    val kafkaConfig = new ProducerConfig(props)
    val producer = new Producer[String, String](kafkaConfig)
    val sdf = new java.text.SimpleDateFormat("yyyy-MM-dd")
    while (true) {
    val data = Source.fromFile("d:\\wilson.zhou\\Desktop\\image.BJ.2016051500_5.log").getLines()
    for (line <- data) {
      val line_split = line.split("\t")

      if (line_split.length > 1) {
        val result = JSON.parseObject(line_split.apply(2))
        val event = new JSONObject()
        event.put("campaign", result.getJSONObject("query_hash").getString("opxcreativeid"))
        event.put("date", sdf.format(System.currentTimeMillis()))
        event.put("opxip", result.getJSONObject("query_hash").getString("opxip"))
        event.put("type", result.getJSONObject("query_hash").getString("opxtype"))
        producer.send(new KeyedMessage[String,String](topic,event.toJSONString))
        println("message is :"+event)
        }
      }

      Thread.sleep(10000)

    }

  }

}
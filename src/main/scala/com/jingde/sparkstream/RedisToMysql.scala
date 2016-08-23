package com.jingde.sparkstream
import scala.util.Properties
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import redis.clients.jedis.JedisPool
import redis.clients.jedis.Jedis
import com.mysql.jdbc.Driver
import java.sql.Connection
import java.sql.DriverManager
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map
object RedisToMysql{
   val driver: String = "com.mysql.jdbc.Driver";
    val url: String = "jdbc:mysql://10.1.1.130:3306/test"
    val username: String = "usr_dba";
    val password: String = "4rfv%TGB^YHN"
  def main(args: Array[String]): Unit = {

    val jedis1 = new Jedis("localhost");
    val result = jedis1.hgetAll("sparktest").asScala
     val list=getsql(result)
         println(list)
     while(true){
 
     if(list.size>0){
       for(sql<-list.toList){
         println(sql)
         excutesql(sql)
        
       }
     }
     Thread.sleep(2000)
     }
  }

  def getsql(map:Map[String,String])={
      val list=ListBuffer[String]()
      for ((k, v) <- map) {
      if (k.length > 10) {
        val k_parse = JSON.parseObject(k)
        val sql = if (k_parse.getString("type") == "I") {
          s"""replace into  spark_test(date,opxip,campaign,impressions,clicks) values( '${k_parse.getString("date")}',
           
         '${k_parse.getString("opxip")}',${k_parse.getString("campaign")},0,${v})"""

        } else {
          s"""replace into  spark_test(date,opxip,campaign,impressions,clicks) values( '${k_parse.getString("date")}',
           
         '${k_parse.getString("opxip")}',${k_parse.getString("campaign")},${v},0)"""

        }

       list+=sql

      }

    }
      list
  }
  
  def excutesql(sql: String) = {
    try {
      Class.forName(driver)
      var connection: Connection = DriverManager.getConnection(url, username, password)
      val statment = connection.createStatement()
      statment.executeUpdate(sql)
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      
    }

  }

}
package com.jingde.go
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
import org.postgresql.Driver
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import scala.collection.mutable.Map
import org.postgresql.Driver
import com.mysql.jdbc.Driver

object GpLoad {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf=new  SparkConf().setMaster("local[2]").setAppName("newworkwordcont")
    val sc = new SparkContext(conf)    
   val  sqlContext=new SQLContext(sc)
    val  url="jdbc:postgresql://10.1.1.230:5432/xmo_dw"
    import sqlContext.implicits._
 
     val pro=new java.util.Properties
     pro.setProperty("user","david_xu")
     pro.setProperty("password","w7dtfxHD")
    pro.setProperty("url",url)
    pro.setProperty("numPartitions","5")
     pro.setProperty("dbtable","(select antispam_tagid from xmo_dw.bshare_blacklist_tagid) as aa") 
    val  imageview=sqlContext.read.format("jdbc").options(pro).load() 
    imageview.show(20)
    
  }
}
package com.jingde.taoci
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FSDataInputStream
import org.apache.hadoop.fs.FSDataOutputStream
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import java.net.URI

import java.text.SimpleDateFormat
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.FileOutputStream
import java.io.FileInputStream
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object app {
  def main(args: Array[String]): Unit = {
    
    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local[5]") //关键
    val sc = new SparkContext(conf)
    val sqlContext=new SQLContext(sc)
    sqlContext.setConf("spark.sql.warehouse.dir", "file:///D:/warehouse")
    val rdd=sc.parallelize(Seq("java","java","C++","sas"),2)
    import  sqlContext.implicits._
    rdd.toDF("col").registerTempTable("table1")
    val sd=sqlContext.sql("select col from table1 limit 1 ")
    
    sd.foreach(println)
    
    
    
  }

}
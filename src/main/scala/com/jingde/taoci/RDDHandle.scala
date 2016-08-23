package com.jingde.taoci
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext

object RDDHandle{
  def main(args:Array[String]){
     Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
     val conf = new SparkConf().setAppName("test").setMaster("local")
      val sc = new SparkContext(conf)
     val  sqlContext=new SQLContext(sc)  
   val df=sqlContext.createDataFrame(Seq((1,2),(3,4))).toDF("A","B")
   df.select("A").show() 
   
   import  sqlContext.implicits._
   
   val rdd=sc.parallelize(Seq(("A",1),("B",3),("C",15),("A",1),("A",1))).toDF("A","B")
   rdd.groupBy("A").count().toDF("group","count").show()
//   rdd.write.format("parquet").save("d:\\wilson.zhou\\Desktop\\lib\\partion")
   
   val movies=sc.textFile("D:\\SPARKCONFALL\\Spark机器学习数据\\ml-100k\\u.item")
  println(movies.first)
  val genres=sc.textFile("D:\\SPARKCONFALL\\Spark机器学习数据\\ml-100k\\u.genre")
  genres.take(5).foreach(println)
  val genreMap=genres.filter(!_.isEmpty).map(line=>line.split("\\|")).map(array=>(array(0),array(1))).collectAsMap   
  println(genreMap)
   
     val titlesAndGenres=movies.map(_.split("\\|")).map{
       array=> val genres=array.toSeq.slice(5,array.size)
       val genresAssigned=genres.zipWithIndex.filter{
         case(g,idx)=>g=="1"
       }.map{
         case(g,index)=>genreMap(index.toString)
       }
       (array(0).toInt,(array(1),genresAssigned))
       
     }
     println(titlesAndGenres.first)
     
     
     
     
     
     
     
     
     
     
  
  }
  
  
  
}
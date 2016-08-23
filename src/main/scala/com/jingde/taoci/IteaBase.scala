package com.jingde.taoci

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import scala.collection.mutable.ArrayBuffer
object IteaBase {

  /*  user,itemcode,ref
1,101,5.0
1,102,3.0*/
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("itemBase")
    val sc = new SparkContext(conf)
    //数据清洗
    val dataClean = sc.textFile("d:\\wilson.zhou\\Desktop\\recommend.txt").map {
      line =>
        val tokens = line.split(",")
        (tokens(0).toLong, (tokens(1).toLong, if (tokens.length > 2) tokens(2).toFloat else 0f))

    }.aggregateByKey(Array[(Long, Float)]())(_ :+ _, _ ++ _).filter(_._2.size > 2).values.persist(StorageLevel.MEMORY_ONLY_SER)
    
    
    dataClean.take(10).foreach(x=>println(x.toList))

    //全局计算模
    val norms = dataClean.flatMap(_.map(x => (x._1, x._2 * x._2))).reduceByKey(_ + _)

    //广播数据
    val normsMap = sc.broadcast(norms.collectAsMap())

    //共生矩阵
    val matrix = dataClean.map(list => list.sortWith(_._1 > _._1)).flatMap(occMatrix).reduceByKey(_ + _)

    //计算相似度
    val similarity = matrix.map(a => (a._1._1, (a._1._2, 1 / (1 + Math.sqrt(normsMap.value.get(a._1._1).get
      + normsMap.value.get(a._1._2).get - 2 * a._2)))))  //在这里用的是的欧式距离的余弦的计算公式

    similarity.collect().foreach(println)
    sc.stop()

  }

  def occMatrix(a: Array[(Long, Float)]): ArrayBuffer[((Long, Long), Float)] = {
    val array = ArrayBuffer[((Long, Long), Float)]()
    for (i <- 0 to (a.size - 1); j <- (i + 1) to (a.size - 1)) {
      array += (((a(i)._1, a(j)._1), a(i)._2 * a(j)._2))
    }
    array
  }

}
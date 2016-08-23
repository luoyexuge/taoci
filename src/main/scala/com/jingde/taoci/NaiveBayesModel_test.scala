package com.jingde.taoci
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.mllib.regression.LabeledPoint 
import org.apache.log4j.{Logger,Level}
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import  scala.collection.mutable.Map
object NaiveBayesModel_test {
  def main(args: Array[String]): Unit = {
    
Logger.getLogger( "org.apache.spark" ).setLevel(Level.WARN)
Logger.getLogger( "org.eclipse.jetty.server" ).setLevel(Level.OFF)
val  conf=new SparkConf().setMaster("local[*]")
val sc=new SparkContext(conf)
val dim = sc.textFile("hdfs://bigdata-h2:8020/user/hive/warehouse/dm.db/dm_userprofile_ad_appinfo/")
val dimrecords = dim.map(_.split('|')).map(r => r(2)).collect.zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
val dimsize = dimrecords.size


val preData = sc.textFile("hdfs://bigdata-h2:8020/user/hive/warehouse/dm.db/dwd_unipay_user_hobby_day/dt=20160810").cache() 

val nbModelSex = NaiveBayesModel.load(sc,"hdfs://bigdata-h2:8020/user/hive/warehouse/dm.db/sexnb2")

val predictResult = preData.map(_.split('|')).map { r => 
    val uid = r(0)
    val categoryFeatures = Array.ofDim[Double](dimsize)

    if (r(1) != "") {r(1).split(',').foreach((arg: String) => categoryFeatures(dimrecords(arg)) = 1)}

    val sex = nbModelSex.predict(Vectors.dense(categoryFeatures))
    uid + '|' + sex
}

predictResult.saveAsTextFile("hdfs://bigdata-h2:8020/user/hive/warehouse/dm.db/dwd_unipay_user_predit_sex_day/dt=20160810")
  }
}
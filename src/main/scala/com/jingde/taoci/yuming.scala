package com.jingde.taoci
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.OneHotEncoder
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.Pipeline
import org.apache.spark.SparkConf
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
object yuming {
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf = new SparkConf().setMaster("local").setAppName("sdsd")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val df = sqlContext.sql("select * from tmp.ctr_temp limit 2000")

    //val categoricals = Array("domain","_c1","os","browser","device","tagid","hour","week","wh","ap","aet","city","province")
    val categoricals = Array("domain", "_c1")
    val indexer = categoricals.map(c => new StringIndexer().setInputCol(c).setOutputCol(s"${c}_idx").setHandleInvalid("skip"))
    val encoder = categoricals.map(c => new OneHotEncoder().setInputCol(s"${c}_idx").setOutputCol(s"${c}_enc").setDropLast(false))

    //    val assembler =  Array(new VectorAssembler().setInputCols(categoricals.map(c => s"${c}_enc")).setOutputCol("features"))

    //    val assembler = categoricals.map(c => new VectorAssembler().setInputCols(Array(s"${c}_enc")).setOutputCol(s"${c}_features"))

    val pipeline = new Pipeline().setStages(indexer ++ encoder)
    val tranformed = pipeline.fit(df).transform(df)
    tranformed.show()
    val arr_last = categoricals.map(x => x + "_enc")
    val vectorAssembler = new VectorAssembler().setInputCols(arr_last).setOutputCol("feature")
    val result = new Pipeline().setStages(Array(vectorAssembler)).fit(tranformed).transform { tranformed }
    result.show()
  }
}
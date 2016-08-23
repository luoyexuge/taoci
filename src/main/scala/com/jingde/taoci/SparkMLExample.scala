package com.jingde.taoci
import org.apache.spark.ml.feature.Word2Vec
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.feature.{StringIndexer,OneHotEncoder,VectorAssembler}
import org.apache.spark.SparkConf
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.functions._

object SparkMLExample {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local[*]").setAppName("sdsd")
    val sc=new SparkContext(conf)
    val sqlContext=new SQLContext(sc)
    
//    sqlContext.createDataFrame(data, beanClass)
    import sqlContext.implicits._
     val df=sc.parallelize(Seq((1, "foo","dd"), (2, "bar","aa"),(4,"ban","bb"),(3,"bar","cc"))
         ).toDF("id", "x","y")
     val categoricals=df.dtypes.filter(_._2=="StringType").map(_._1) //获得字符串变量的列名称
     val categorical_int=df.dtypes.filter(_._2=="IntegerType").map(_._1)
     val indexer=categoricals.map(c=>new StringIndexer().setInputCol(c).setOutputCol(s"${c}_idx").setHandleInvalid("skip"))
     val encoder=categoricals.map(c=>new OneHotEncoder().setInputCol(s"${c}_idx").setOutputCol(s"${c}_enc").setDropLast(false))
//     val assembler = categoricals.map(c=>new VectorAssembler().setInputCols(Array(s"${c}_enc")).setOutputCol(s"${c}_feature"))
     val pipeline=new Pipeline().setStages(indexer++encoder)
     val tranformed=pipeline.fit(df).transform(df)
//       tranformed.show()  
       
       val arr_last=categoricals.map(x=>x+"_enc")
       println(( arr_last.toList:::categorical_int.toList))
        val vectorAssembler = new VectorAssembler().setInputCols(( arr_last.toList:::categorical_int.toList).toArray).setOutputCol("feature")
       val result=new Pipeline().setStages(Array(vectorAssembler)).fit(tranformed).transform(tranformed)   
        result.select("feature").foreach(println)
     
  }
}



/*import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.OneHotEncoder
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.Pipeline

val df = sqlContext.sql("select * from tmp.ctr_temp limit 10")

//val categoricals = Array("domain","_c1","os","browser","device","tagid","hour","week","wh","ap","aet","city","province")
val categoricals = Array("domain","_c1")
val indexer = categoricals.map(c => new StringIndexer().setInputCol(c).setOutputCol(s"${c}_idx").setHandleInvalid("skip"))
val encoder = categoricals.map(c => new OneHotEncoder().setInputCol(s"${c}_idx").setOutputCol(s"${c}_enc").setDropLast(false))
val assembler = categoricals.map(c=>new VectorAssembler().setInputCols(Array(s"${c}_enc")).setOutputCol(s"${c}_features"))
val pipeline = new Pipeline().setStages(indexer++encoder++assembler)
val tranformed = pipeline.fit(df).transform(df)
tranformed.show()

  val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("x_feature","y_feature"))
      .setOutputCol("feature")
       val result=new Pipeline().setStages(Array(vectorAssembler)).fit(tranformed).transform { tranformed }
  result.show()

*/



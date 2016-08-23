package com.jingde.taoci
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext
import org.apache.spark.mllib.linalg.SparseVector
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.linalg.Vectors
import scala.collection.mutable.ArrayBuffer
import scala.Array
import org.apache.spark.sql.functions._
import org.apache.spark.ml.feature.StringIndexer
import org.apache.spark.ml.feature.OneHotEncoder
import org.apache.spark.ml.feature.IndexToString
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.GBTRegressor
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.GBTRegressionModel
import org.apache.spark.ml.classification.{ GBTClassificationModel, GBTClassifier }
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.classification.{ RandomForestClassificationModel, RandomForestClassifier }
import org.apache.spark.ml.classification.{LogisticRegressionModel,LogisticRegression}
import org.apache.spark.ml.classification.RandomForestClassificationModel
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

import org.apache.spark.ml.param.ParamMap
object mlTest {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[*]").setAppName("VectorIndexerExample")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    var df = sqlContext.createDataFrame(Seq(
      (1, 1, "a"),
      (1, 3, "b"),
      (0, 2, "c"),
      (1, 2, "d"),
      (0, 1, "c"),
      (0, 1, "f"),
      (1, 2, "d"),
      (0, 1, "c"),
      (1, 1, "f"),
      (0, 1, "f"))).toDF("label", "at", "et")

    val labelIndexer = new StringIndexer().setInputCol("label").setOutputCol("indexedLabel").fit(df)

    val categoricals = Array("at", "et")
    val indexer = categoricals.map(c => new StringIndexer().setInputCol(c).setOutputCol(s"${c}_idx").setHandleInvalid("skip"))
    val encoder = categoricals.map(c => new OneHotEncoder().setInputCol(s"${c}_idx").setOutputCol(s"${c}_enc").setDropLast(false))
    val assembler = Array(new VectorAssembler().setInputCols(categoricals.map(c => s"${c}_enc")).setOutputCol("features"))
    val Array(trainingData, testData) = df.randomSplit(Array(0.6, 0.4))

    // Train a GBT model.

    val gbt = new LogisticRegression()
      .setLabelCol("indexedLabel")
      .setFeaturesCol("features")
      .setMaxIter(10)

    val labelConverter = new IndexToString()
      .setInputCol("prediction")
      .setOutputCol("predictedLabel")
      .setLabels(labelIndexer.labels)

    val pipeline = new Pipeline().setStages(Array(labelIndexer) ++  indexer ++ encoder ++ assembler ++ Array(gbt)++Array(labelConverter))
    val model = pipeline.fit(trainingData)
    val predictions = model.transform(testData)
    predictions.show()
    
      val evaluator = new MulticlassClassificationEvaluator()
      .setLabelCol("indexedLabel")
      .setPredictionCol("prediction")
      .setMetricName("precision")
    val accuracy = evaluator.evaluate(predictions)

    val rddauc=predictions.select("indexedLabel","prediction").rdd.map(x=>(x.apply(0).toString.toDouble,x.apply(1).toString.toDouble))
    val metrics = new BinaryClassificationMetrics(rddauc)
    println("auc:"+metrics.areaUnderROC())
    println("Test Error = " + (1.0 - accuracy))

    
//    val gbtModel = model.stages(6).asInstanceOf[GBTClassificationModel]
//
//    println("Learned regression GBT model:\n" + gbtModel.toDebugString)

  }
}

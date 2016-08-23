package com.jingde.taoci

import org.apache.log4j.{ Level, Logger }
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.feature.ChiSqSelector
import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.classification.LogisticRegressionModel
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.SQLContext
object LogisticRegressionWithSGDTest {
  def main(args: Array[String]) {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    val conf = new SparkConf().setAppName("Spark Pi").setMaster("local[5]") //关键
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    
    

    //读取数据
    val rawData = sc.textFile("D:\\SPARKCONFALL\\spark-1.6.0-bin-hadoop2.6\\data\\mllib\\train_no_head.tsv")
    val records = rawData.map(_.split('\t'))
    
    
    

    //对于分类变量转化为OneHotEncoder
    val category = records.map(r => r(3)).distinct().collect().zipWithIndex.toMap
    //与之前不同的是添加了一个向量categoryFeatures用于标识类别
    val data = records.map { point =>
      val replaceData = point.map(_.replaceAll("\"", ""))
      val label = replaceData(replaceData.size - 1).toInt
      val categoriesIndex = category(point(3))
      val categoryFeatures = Array.ofDim[Double](category.size)
      categoryFeatures(categoriesIndex) = 1.0
      val otherfeatures = replaceData.slice(4, replaceData.size - 1).map(x => if (x == "?") 0.0 else x.toDouble)
      //RDD之间的加运算使用"++"，构建添加了类别标识以后的特征向量
      val features = otherfeatures ++ categoryFeatures
      LabeledPoint(label, Vectors.dense(features))
    }
    
    

    //对数据进行标准化
    val vectors = data.map(p => p.features)
    //调用初始化一个StandardScaler对象，具体使用方法查看Spark api
    val scaler = new StandardScaler(withMean = true, withStd = true).fit(vectors)
    //重新标准化特征变量
    val scalerData = data.map(point =>
      LabeledPoint(point.label, scaler.transform(point.features)))
      

    //训练模型
    val lrModel = LogisticRegressionWithSGD.train(scalerData, 20) //迭代60次
    
    
    

    //模型评估
    val scoreAndlabels = scalerData.map(data => (lrModel.predict(data.features), data.label))
    val metrics = new BinaryClassificationMetrics(scoreAndlabels)
    println("model auc is " + metrics.areaUnderROC())

   /* val predictureData = scalerData.map {
      point => if (lrModel.predict(point.features) == point.label) 1 else 0
    }.sum()
    val accuarcy = predictureData / scalerData.count()
    println("model accuarcy is " + metrics.areaUnderROC())*/
    
    
    
    //模型保存
//    lrModel.save(sc,"D:\\tmp\\logistics\\")
    
    //模型读取
    val model= LogisticRegressionModel.load(sc,"D:\\tmp\\logistics\\")
     val scoreAndlabels1 = scalerData.map(data => (model.predict(data.features), data.label))
    val metrics1 = new BinaryClassificationMetrics(scoreAndlabels)
    println("model auc is " + metrics1.areaUnderROC())
       
  }
  
  
  
}
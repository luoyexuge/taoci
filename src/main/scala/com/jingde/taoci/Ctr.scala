package com.jingde.taoci
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.mllib.classification.LogisticRegressionWithSGD
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.sql.hive.HiveContext

/*nohup /usr/lib/spark/bin/spark-submit --master spark://srv-buzz-bosk1:7077    
--executor-memory 100g --driver-memory 30g --conf spark.akka.frameSize=2000 --class  com.jingde.taoci.Ctr  /home/wilson/taoci-0.0.1-SNAPSHOT.jar   >>/home/wilson/ctr.log 2>&1 &
*/

object Ctr {
  def main(args: Array[String]): Unit = {
    
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
    Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
    
     val conf = new SparkConf()
        if (System.getProperty("local") != null) {
          conf.setMaster("local").setAppName("tagid price prediction")
        }
    val sc = new SparkContext(conf)
    val hiveContext = new HiveContext(sc)  
    val ctr_tmp=hiveContext.sql("""select domain,`_c1`,os,browser,device,tagid,hour,week,wh,ap,aet,city,province,is_click from tmp.ctr_temp""").rdd.cache()
    
    println("hive load sucesss")
   
    val category_domian=ctr_tmp.map(x=>x.apply(0).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_c1=ctr_tmp.map(x=>x.apply(1).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_os=ctr_tmp.map(x=>x.apply(2).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_browser=ctr_tmp.map(x=>x.apply(3).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_device=ctr_tmp.map(x=>x.apply(4).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_tagid=ctr_tmp.map(x=>x.apply(5).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_hour=ctr_tmp.map(x=>x.apply(6).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_week=ctr_tmp.map(x=>x.apply(7).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_wh=ctr_tmp.map(x=>x.apply(8).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_ap=ctr_tmp.map(x=>x.apply(9).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
   val category_aet=ctr_tmp.map(x=>x.apply(10).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_city=ctr_tmp.map(x=>x.apply(11).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    val category_province=ctr_tmp.map(x=>x.apply(12).toString.trim).distinct.collect().zipWithIndex.toMap.asInstanceOf[Map[String, java.lang.Integer]]
    
    println("feature hand will start")
    val data=ctr_tmp.map{x=>
      val label=x.apply(13).toString.toInt
      
       val domianIndex = category_domian(x.apply(0).toString.trim())
       val category_domian_arr = Array.ofDim[Double](category_domian.size)
           category_domian_arr(domianIndex) = 1.0
           
           
       val   c1Index=category_c1(x.apply(1).toString().trim()) 
       val  category_c1_arr=Array.ofDim[Double](category_c1.size)
             category_c1_arr(c1Index)=1.0
             
        
       val  osIndex=category_os(x.apply(2).toString().trim()) 
       val  category_os_arr=Array.ofDim[Double](category_os.size)
             category_os_arr(osIndex)=1.0
             
       val   browserIndex=category_browser(x.apply(3).toString().trim()) 
       val  category_browser_arr=Array.ofDim[Double](category_browser.size)
             category_browser_arr(browserIndex)=1.0
             
       val   deviceIndex=category_device(x.apply(4).toString().trim()) 
       val  category_device_arr=Array.ofDim[Double](category_device.size)
             category_device_arr(deviceIndex)=1.0
             
             
       val   tagidIndex=category_tagid(x.apply(5).toString().trim()) 
       val  category_tagid_arr=Array.ofDim[Double](category_tagid.size)
             category_tagid_arr(tagidIndex)=1.0
             
             
              val   hourIndex=category_hour(x.apply(6).toString().trim()) 
       val  category_hour_arr=Array.ofDim[Double](category_hour.size)
             category_hour_arr(hourIndex)=1.0
             
             
       val   weekIndex=category_week(x.apply(7).toString().trim()) 
       val  category_week_arr=Array.ofDim[Double](category_week.size)
             category_week_arr(weekIndex)=1.0
             
             
       val   whIndex=category_wh(x.apply(8).toString().trim()) 
       val  category_wh_arr=Array.ofDim[Double](category_wh.size)
             category_wh_arr(whIndex)=1.0
             
             
       val   apIndex=category_ap(x.apply(9).toString().trim()) 
       val  category_ap_arr=Array.ofDim[Double](category_ap.size)
             category_ap_arr(apIndex)=1.0
             
             
       val   aetIndex=category_aet(x.apply(10).toString().trim()) 
       val  category_aet_arr=Array.ofDim[Double](category_aet.size)
             category_aet_arr(aetIndex)=1.0
             
       val  cityIndex=category_city(x.apply(11).toString().trim()) 
       val  category_city_arr=Array.ofDim[Double](category_city.size)
             category_city_arr(cityIndex)=1.0
             
             
       val  provinceIndex=category_province(x.apply(12).toString().trim()) 
       val  category_province_arr=Array.ofDim[Double](category_province.size)
      category_province_arr(provinceIndex) = 1.0
          
      val feature=category_domian_arr++ category_c1_arr++ category_os_arr++category_browser_arr++category_device_arr++ category_tagid_arr++  category_hour_arr++
          category_week_arr++category_wh_arr++category_ap_arr++category_aet_arr++category_city_arr++category_province_arr 
        LabeledPoint(label,Vectors.dense(feature))  
      
      
    }
   
    val splits = data.randomSplit(Array(0.9, 0.1), seed = 11L)
    val (trainingData, testData) = (splits(0), splits(1))
    println("model  will start")
    trainingData.cache()
    testData.cache()
     val lrModel = LogisticRegressionWithSGD.train(trainingData, 14) //迭代14次
     
      println("model  will has been")
    
       val scoreAndlabels = testData.map(x => ( if(lrModel.predict(x.features)>0.5) 1.0 else 0, x.label))
       scoreAndlabels.filter(_._1>0).take(100).foreach(println)
    val metrics = new BinaryClassificationMetrics(scoreAndlabels)
    
   val  count_pos=scoreAndlabels.map{x=>
          val  predict=if(x._1>=0.5) 1.0 else 0.0  
             (predict,x._2)
               }.filter(x => x._1 == x._2).count()
    
    val count_all=scoreAndlabels.count()
    println("count_pos is :"+count_pos+"----->"+"count_all is:"+count_all)
    println("Correct prediction  is :"+count_pos.toDouble/count_all)
    println("model test  auc is " + metrics.areaUnderROC())
    
    
//      lrModel.save(sc,"/tmp/wilson/logistic")
      println("model  save  sucess")

  }
}





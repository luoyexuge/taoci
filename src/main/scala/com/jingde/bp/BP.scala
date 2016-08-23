package com.jingde.bp
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.{ SparkConf, SparkContext }
import org.apache.spark.sql.SQLContext
import org.neuroph.core.NeuralNetwork
import org.neuroph.nnet.Perceptron
import org.neuroph.core.data.DataSet
import org.neuroph.core.data.DataSetRow
import com.alibaba.fastjson.JSON
import  com.iclick.nn.RedisModel
import   com.alibaba.fastjson.JSON
import  com.iclick.nn.PerceptronSample
import  com.iclick.nn.Model
object BP{
  def main(args: Array[String]): Unit = {
      Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
      Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
     val conf = new SparkConf().setAppName("test").setMaster("local")
      val sc = new SparkContext(conf)
       val  traindata=PerceptronSample.read_iris("d:\\wilson.zhou\\Desktop\\iris.txt").asInstanceOf[DataSet]
       val myPerceptron = new Perceptron(4, 1)
         myPerceptron.learn(traindata)
         println(myPerceptron.getLabel)
        val  bpModel=new Model()
        bpModel.setNetwork(myPerceptron)
      /*  val model_string=JSON.toJSON(bpModel).toString()
        println(model_string)*/
        RedisModel.setmodeltoredis("BP",bpModel)
        println("model has  insertted into redis")
        val redisresult=RedisModel.getmodelfromredis("BP").asInstanceOf[Model].getNetwork
        println(redisresult.getClass)
      PerceptronSample.testNeuralNetwork(redisresult,traindata)
      println("sucess")
      
  }
  
}
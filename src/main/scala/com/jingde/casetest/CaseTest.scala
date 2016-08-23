package com.jingde.casetest
 import scala.collection.JavaConversions._
object CaseTest {
  def main(args: Array[String]): Unit = {
  val    f:PartialFunction[Char,Int]={
    case '+'=>1
    case '_'=> -1 
    case _ =>0
  }
  
  println(f(20))
  println(f('+'))
  
  
    val  df = new java.text.DecimalFormat("#.00");
  println(df.format(2.12323))
  }
}
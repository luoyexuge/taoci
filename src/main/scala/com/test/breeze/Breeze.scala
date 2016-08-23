package com.test.breeze
import breeze.linalg.{DenseMatrix => BDM, DenseVector => BDV, linspace}
import breeze.plot._
object Breeze {
  def main(args: Array[String]): Unit = {
    val  a=new BDV[Int](1 to 3 toArray)
    val b= new BDM[Int](3,3,1 to 9 toArray)
    
    
    val f = Figure()
   
  
     val p2 = f.subplot(0)
    val g = breeze.stats.distributions.Gaussian(0, 1)
    p2 += hist(g.sample(100000), 1000)
    p2.title = "A normal distribution"
    p2.xlabel="x轴"
    p2.ylabel="y轴"
    f.saveas("d:\\subplots.png")
    
     val f2 = Figure()
    f2.subplot(0) += image(BDM.rand(200, 200))
    f2.saveas("d:\\image.png")
    
  }
}
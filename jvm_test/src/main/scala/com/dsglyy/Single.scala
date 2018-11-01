package com.dsglyy

/**
  *
  * Created on 2018/7/16  Mon AM 10:51
  * mian-component
  *
  * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
  * @Version: Single V 0.0, Jul 16, 2018 DSG Exp$$
  * @Since 1.8
  * @Description :
  *
  *
  */
object Single {
//  private val markers: Map[String, Single] = Map(
//    "red" -> new Single("red"),
//    "blue" -> new Single("blue"),
//    "green" -> new Single("green")
//  )

  var instance : Single =  _

  def apply(color:String) = {

    if (instance == null   ) instance = new Single(color)
    instance
  }


//  def getMarker(color:String) = {
//    if(markers.contains(color)) markers(color) else null
//  }

}

class  Single private(color:String ){
  println("Creating " + this)

  override def toString(): String = "Single color "+ color
}

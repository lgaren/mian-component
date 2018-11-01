package com.dsglyy

import java.text.SimpleDateFormat
import java.util.Calendar

/**
  *
  * Created on 2018/7/16  Mon AM 11:04
  * mian-component
  *
  * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
  * @Version: TestSingle V 0.0, Jul 16, 2018 DSG Exp$$
  * @Since 1.8
  * @Description :
  *
  *
  */
object TestSingle {
  def main(args: Array[String]) {
    // 方法糖 apply
    //单例对象在第一次被访问时才会被初始化。
    println(Single("red"))

    var period:String=""
    var cal:Calendar =Calendar.getInstance();
//    var df:SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    cal.set(Calendar.DATE, 1)
//    period=df.format(cal.getTime())//本月第一天
    println(period)
//    println()

    // 单例函数调用，省略了.(点)符号
//    println(Single getMarker "blue")
  }
}

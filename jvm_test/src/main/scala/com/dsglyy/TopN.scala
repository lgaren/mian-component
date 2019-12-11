//package com.dsglyy

//import org.apache.spark.SparkConf
//import org.apache.spark.SparkContext
/**
  * Created by admin on 2017/8/21.
  */
object TopN {
//
//    def main(args:Array[String]){
//      val conf = new SparkConf() //创建SparkConf对象
//      conf.setAppName("Wow,TopNGroup App!") //设置应用程序的名称，在程序运行的监控界面可以看到名称
//      conf.setMaster("local") //此时，程序在本地运行，不需要安装Spark集群
//
//      val sc = new SparkContext(conf) //创建SparkContext对象，通过传入SparkConf实例来定制Spark运行的具体参数和配置信息
//      sc.setLogLevel("WARN")
//      val lines = sc.textFile("G://IMFBigDataSpark2016//tesdata//topNGroup1.txt", 1) //读取本地文件并设置为一个Partion
//
//      val pairs=lines.map { line => (line.split(",")(0),line.split(",")(1).toInt) }
//      val grouped=pairs.groupByKey
//      val groupedTop5=grouped.map(grouped=>
//      {
//        (grouped._1,grouped._2.toList.sortWith(_<_).take(5))
//      })
//      val groupedKeySorted=groupedTop5.sortByKey()
//
//      groupedKeySorted.collect().foreach(pair=>{
//        println(pair._1+":")
//        pair._2.foreach { println }
//      })
//      sc.stop()
//
//    }
}
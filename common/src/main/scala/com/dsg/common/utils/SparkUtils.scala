package com.lvmama

import com.dsg.common.conf.DSGConfig
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkUtils {
  var sparkConf: SparkConf = {
    val sparkConf = new SparkConf()
      .set("itemcf.similarity.function","improvecosine")
      .set("itemcf.rate.tak","100")
      .set("itemcf.similarity.tak","50")
      .set("itemcf.similarity.regularizedCorrelation.priorCorrelation","1.0")
      .set("itemcf.similarity.regularizedCorrelation.virtualCount","10.0")
    //        .set("spark.sql.warehouse.dir", "hdfs://nameservice1/user/hive/warehouse")
    val conf = DSGConfig.getConf
    val it = conf.iterator
    while (it.hasNext) {
      val ev = it.next
      sparkConf.set(ev.getKey, ev.getValue)
    }
    sparkConf
  }

  def createSession(appName: String) = {
    sparkConf.setAppName(appName)
    if (sparkConf.getBoolean("itemcf.istest",false)) sparkConf.setMaster("local[1]")
    var spark: SparkSession = null
    spark = SparkSession.builder
      .config(sparkConf)
      .appName(appName)
      .enableHiveSupport()
      .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")
    spark
  }

}

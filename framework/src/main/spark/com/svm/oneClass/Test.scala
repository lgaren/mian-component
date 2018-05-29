//package com.svm.oneClass
//
//import org.apache.hadoop.hbase.client.Put
//import org.apache.spark.sql.DataFrame
//
//object Test {
//
//  def exportHiveData(dbname: String, tbname: String, data: DataFrame): Unit = {
//
//    try {
//      val tbPuts = new util.ArrayList[Put]
//      val columns = data.schema.fieldNames
//      data.foreachPartition(x => {
//
//        while (x.hasNext) {
//          val row = x.next
//          for (i <- 0 to columns.size - 1) {
//            val column = columns(i)
//            val obj = row.getAs(column).toString
//            val value = if (obj == null) ""
//            else obj.toString
//
//            tbPuts.add(new Put((java.util.UUID.randomUUID().toString).getBytes).addColumn(Bytes.toBytes("cf1"), Bytes.toBytes(column), Bytes.toBytes(value)))
//            if (tbPuts.size == 1000) {
//              println("hbase表数据count：" + tbPuts.size())
//              //数据批量插入hbase
//              hbaseUtil.putByHTable(tbname, tbPuts)
//              tbPuts.clear
//              println("hbase表数据count：" + tbPuts.size())
//            }
//          }
//        }
//      })
//      logger.info("数据导入完毕！")
//    } catch {
//      case ex: IOException => println(ex)
//      case ex: InstantiationException => println(ex)
//      case ex: IllegalAccessException => println(ex)
//    }
//  }
//
//}

package com.itemcf

import java.text.SimpleDateFormat
import java.util.Date

import com.common.SimilarityMeasures
import com.lvmama.SparkUtils

/**
  *
  * Created on 2018/9/27  Thu PM 15:14
  * mian-component
  *
  * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
  * @Version: ItemCF V 0.0, Sep 27, 2018 DSG Exp$$
  * @Since 1.8
  * @Description :
  *
  *
  */
object ItemCF {
  def main(args: Array[String]): Unit = {

    val spark = SparkUtils.createSession("ItemCF")
    //    测试环境
    val sql = s"SELECT cast(user_id as int) as userId, brand_name, rate FROM dml.dml_hotel_brand_rate"

    val oriRatings = spark.sql(sql).rdd.map(row => {
      val user_id = row.getInt(0)
      val brand_name = row.getString(1)
      ((user_id, brand_name), (user_id, brand_name, row.getInt(2)))
    })

    //    oriRatings.cache()

    //  ratings now contains the following fields:  brand -> (brand, (user_id, brand, rate))
    val ratings = oriRatings.groupByKey().map(k => (k._1._1, (
      k._1._1, k._1._2, {
      var rate = 0
      k._2.foreach(sd => rate = rate + sd._3)
      rate
    }
    ))).groupByKey.flatMap(x => (x._2.toList.sortWith((x, y) => x._3 > y._3)
      .take(100)
      .map(useritem => (useritem._2, useritem))))

    ratings.cache()
    //
    //    //  item2manyUser now contains the following fields:  brand -> List((user_id, brand, rate), (user_id, brand, rate) ,(user_id, brand, rate)......)
    val item2manyUser = ratings.groupByKey
    //
    //    //  numRatersPerItem now contains the following fields:    brand -> numRaters
    val numRatersPerItem = item2manyUser.map(grouped => (grouped._1, grouped._2.size))
    //
    //    //  user_id -> (user_id, brand, rate, numRaters)
    val ratings2 = item2manyUser.join(numRatersPerItem).flatMap(joined => {
      joined._2._1.map(f => (f._1, (f._1, f._2, f._3, joined._2._2)))
    })
    ratings2.cache()
    //
    //    //  Create Co-occurrence Matrix
    //    //  ratingPairs now contains the following fields:  user_id -> List((user_id, brand1, rate1, numRaters), (user_id, brand2, rate2, numRaters))
    val ratingPairs = ratings2.join(ratings2).filter(f => f._2._1._2 < f._2._2._2)
    //
    //    //  tempVectorCalcs now contains the following fields:    (brand1, brand2) -> (rating1 * rating2, rating1, rating2, √rating1, √rating2, rater1number. rater2number)
    val tempVectorCalcs = ratingPairs.map(data =>
      ((data._2._1._2, data._2._2._2),
        (data._2._1._3 * data._2._2._3, // rating 1 * rating 2
          data._2._1._3, // rating item 1
          data._2._2._3, // rating item 2
          math.pow(data._2._1._3, 2), // √rating1  item 1
          math.pow(data._2._2._3, 2), // √rating2  item 2
          data._2._1._4, // number of raters item 1
          data._2._2._4)) // number of raters item 2
    )
    //
    //    //    vectorCalcs now contains the following fields:    (brand1, brand2) -> List( (rating1 * rating2, rating1, rating2, √rating1, √rating2, rater1number, rater2number),
    //    //                                                                                (rating1 * rating2, rating1, rating2, √rating1, √rating2, rater1number, rater2number)
    //    //                                                                                              .................  )
    val vectorCalcs = tempVectorCalcs.groupByKey().map(data =>
      (data._1, (
        data._2.size,
        data._2.map(f => f._1).sum, // ∑ rateing i * rateing j
        data._2.map(f => f._2).sum, // ∑ rateing i
        data._2.map(f => f._3).sum, // ∑ rateing j
        data._2.map(f => f._4).sum, // ∑ √ratingi
        data._2.map(f => f._5).sum, // ∑ √ratingj
        data._2.map(f => f._6).max, // max rater1number
        data._2.map(f => f._7).max)) // max rater2number
    )
    //
    //    //  due to matrix is not symmetry , use half matrix build full matrix
    val vectorCalcsTotal = vectorCalcs ++ vectorCalcs.map(x => ((x._1._2, x._1._1), (x._2._1, x._2._2, x._2._4, x._2._3, x._2._6, x._2._5, x._2._8, x._2._7)))
    //
    //    //  compute similarity metrics for each movie pair,  similarities meaning brand1 to brand1 similarity
    val tempSimilarities = vectorCalcsTotal.map(fields => {
      //      val similarFun = SparkSession.getActiveSession.get.conf.get("itemcf.similarity.function")
      //    tanimoto cosine pearson regularizepearson improvecosine
      val similarFun = "improvecosine"
      val (size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq, numRaters, numRaters2) = fields._2

      val cosSim = similarFun match{
        //        case "pearson" => SimilarityMeasures.correlation(size,dotProduct,ratingSum, rating2Sum,ratingNormSq, rating2NormSq )
        //        case "regularizepearson" => SimilarityMeasures.regularizedCorrelation(size,dotProduct,ratingSum, rating2Sum,ratingNormSq, rating2NormSq)
        case "cosine" => SimilarityMeasures.cosineSimilarity(dotProduct,ratingNormSq,rating2NormSq)
        case "improvecosine" => SimilarityMeasures.improvedCosineSimilarity(dotProduct, ratingNormSq, rating2NormSq,size,numRaters, numRaters2)
        //        case "tanimoto" => SimilarityMeasures.jaccardSimilarity(numRaters, numRaters2)
      }
      //      println(cosSim)
      //      val cosSim = SimilarityMeasures.cosineSimilarity(dotProduct, scala.math.sqrt(ratingNormSq), scala.math.sqrt(rating2NormSq))*size/(numRaters*math.log10(numRaters2+10))
      (fields._1._1, (fields._1._2, cosSim))
    })
    //

    val similarities = tempSimilarities.groupByKey().flatMap(x =>
      x._2.map(temp => (x._1, (temp._1, temp._2))).toList
        .sortWith((a, b) => a._2._2 > b._2._2)
        .take(50)
        .map(x => (x._1, x._2._1, x._2._2))
    )

    import spark.implicits._
    similarities.toDF().createOrReplaceTempView("brandsimilarities")

    val fmt = new SimpleDateFormat("yyyyMMdd")


    spark.sql(s"insert overwrite table  dml.dml_hotel_brand_similarity  PARTITION  (par_day='${fmt.format(new Date)}') select * from brandsimilarities ")

    /****************************************************************************************************
      *                                         USER RECOMMEND                                           *
      ****************************************************************************************************/
    //    //
    val te = similarities.map(simi => (simi._1,(simi._2 , simi._3)))
    //    (喜达屋,((9767018,喜达屋,8),(太平洋,0.001518269202496212)))
    //  numRatersPerItem now contains the following fields: (user, brand) ->  (simi, simi * rate )
    val statistics =  ratings.join(te).map(x=>((x._2._1._1,x._2._2._1),(x._2._2._2,x._2._1._3 * x._2._2._2)))

    val predictResult = statistics.reduceByKey((x,y)=>(x._1+y._1,x._2+y._2)).map(x=>(x._1,x._2._2/x._2._1))

    val filterItem = oriRatings.map(x=>((x._2._1,x._2._2),Double.NaN))

    val totalScore = predictResult ++ filterItem

    //  dml.dml_hotel_brand_userrec
    val finalResult = totalScore.reduceByKey(_+_).filter(x=> !(x._2 equals(Double.NaN)))
      .map(x=>(x._1._1,x._1._2,x._2)).groupBy(x=>x._1).flatMap(x=>(x._2.toList.sortWith((a,b)=>a._3>b._3).take(50)))
    finalResult.toDF().createOrReplaceTempView("userrec")

    spark.sql(s"insert overwrite table  dml.dml_hotel_brand_userrec  PARTITION  (par_day='${fmt.format(new Date)}') select * from userrec" )
    //
    //              .foreach( v => println(v))

  }

}

package com.common

import org.apache.spark.sql.SparkSession

/**
  *
  * Created on 2018/9/27  Thu PM 15:13
  * mian-component
  *
  * @Author: <a href="mailto:(yuanyuan.liu@dsglyy.com)">Liu Yuanyuan</a>
  * @Version: SimilarityMeasures V 0.0, Sep 27, 2018 DSG Exp$$
  * @Since 1.8
  * @Description :
  *
  *
  */
object SimilarityMeasures {
  /**
    * The correlation between two vectors A, B is
    *   cov(A, B) / (stdDev(A) * stdDev(B))
    *
    * This is equivalent to
    *   [n * dotProduct(A, B) - sum(A) * sum(B)] /
    *     sqrt{ [n * norm(A)^2 - sum(A)^2] [n * norm(B)^2 - sum(B)^2] }
    */
  def correlation(size: Double, dotProduct: Double, ratingSum: Double,
                  rating2Sum: Double, ratingNormSq: Double, rating2NormSq: Double) = {

    val numerator = size * dotProduct - ratingSum * rating2Sum
    val denominator = scala.math.sqrt(size * ratingNormSq - ratingSum * ratingSum) *
      scala.math.sqrt(size * rating2NormSq - rating2Sum * rating2Sum)
    numerator / denominator
  }

  /**
    * Regularize correlation by adding virtual pseudocounts over a prior:
    * RegularizedCorrelation = w * ActualCorrelation + (1 - w) * PriorCorrelation
    * where w = # actualPairs / (# actualPairs + # virtualPairs).
    */
  def regularizedCorrelation(size: Double, dotProduct: Double, ratingSum: Double,
                             rating2Sum: Double, ratingNormSq: Double, rating2NormSq: Double) = {
    val PRIOR_CORRELATION = SparkSession.getActiveSession.get.conf.get("itemcf.similarity.regularizedCorrelation.priorCorrelation").toDouble
    val PRIOR_COUNT = SparkSession.getActiveSession.get.conf.get("itemcf.similarity.regularizedCorrelation.virtualCount").toDouble
    val unregularizedCorrelation = correlation(size, dotProduct, ratingSum, rating2Sum, ratingNormSq, rating2NormSq)
    val w = size / (size + PRIOR_COUNT)

    w * unregularizedCorrelation + (1 - w) * PRIOR_CORRELATION
  }

  /**
    * The cosine similarity between two vectors A, B is
    * dotProduct(A, B) / (norm(A) * norm(B))
    */
  def cosineSimilarity(dotProduct: Double, ratingNorm: Double, rating2Norm: Double) = {
    dotProduct / (scala.math.sqrt(ratingNorm) * scala.math.sqrt(rating2Norm))
  }

  /**
    * The improved cosine similarity between two vectors A, B is
    * dotProduct(A, B) * num(A ∩ B) / (norm(A) * norm(B) * num(A) * log10(10 + num(B)))
    */
  def improvedCosineSimilarity(dotProduct: Double, ratingNorm: Double, rating2Norm: Double
                               , numAjoinB: Long, numA: Long, numB: Long): Double = {
    dotProduct * numAjoinB / (scala.math.sqrt(ratingNorm) * scala.math.sqrt(rating2Norm) * numA * math.log10(10 + numB))
  }

  /**
    * The Jaccard Similarity between two sets A, B is
    * |Intersection(A, B)| / |Union(A, B)|
    */
  def jaccardSimilarity(totalUsers1: Double, totalUsers2: Double) = {
    val usersInCommon = totalUsers1 + totalUsers2
    val union = totalUsers1 + totalUsers2 - usersInCommon
    usersInCommon / union
  }

}

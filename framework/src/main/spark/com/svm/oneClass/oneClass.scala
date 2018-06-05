package oneClass

/**
  * Created by jinss on 6/2/17.
  */

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.mllib.classification.{SVMModel, SVMWithSGD}
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

object oneClassWithSGD {
  val myconf = new SparkConf()
  val mysc = new SparkContext(myconf)

  def train(input: RDD[Vector],
            numIterations: Int,
            stepSize: Double,
            regParam: Double,
            miniBatchFraction: Double,
            initialWeights: Vector
           ): SVMModel = {
    val inputLabeledPoint = input.map(xs => LabeledPoint(1.0, xs))
    SVMWithSGD.train(inputLabeledPoint, numIterations, stepSize, regParam, miniBatchFraction, initialWeights)
  }

  def train(input: RDD[Vector],
            numIterations: Int,
            stepSize: Double,
            regParam: Double,
            miniBatchFraction: Double): SVMModel = {
    val inputLabeledPoint = input.map(xs => LabeledPoint(1.0, xs))
    SVMWithSGD.train(inputLabeledPoint, numIterations, stepSize, regParam, miniBatchFraction)
  }

  def train(input: RDD[Vector],
            numIterations: Int,
            stepSize: Double,
            regParam: Double): SVMModel = {
    train(input, numIterations, stepSize, regParam, 1.0)
  }

  def train(input: RDD[Vector], numIterations: Int): SVMModel = {
//    RDD  迭代次数  步长  正则花参数   每次迭代参与计算的样本比例·23
    train(input, numIterations, 1.0, 0.01, 1.0)
  }
}
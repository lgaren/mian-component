package bSpline

import breeze.linalg.DenseVector

/* @(#)bSpline.scala
 */
/**
  *
  * Time-stamp: <2017-07-24 21:49:10 jinss>
  * Author: jinss
  *
  * @author <a href="mailto:(shusong.jin@Istuary.com)">Jin Shusong</a>
  *         Version: $Id: bSpline.scala,v 0.0 2017/07/24 01:58:41 jinss Exp$
  *         \revision$Header: /home/jinss/study/IdeaProjects/bSpline/src/main/scala/bSpline.scala,v 0.0 2017/07/24 01:58:41 jinss Exp$
  */

object bSpline extends myUtil {
  def bSplineDerivEval(x: Double, degree: Int,
                       knotsVector: breeze.linalg.DenseVector[Double]
                      ): breeze.linalg.DenseMatrix[Double] = {
    val mytuple = bSplineDerivEvalNonzero(x, degree, knotsVector)
    val iStart = mytuple._1
    val iEnd = iStart + degree
    val nonzeroVector = mytuple._2
    val part0 = bSplineEval(x, degree, knotsVector).toArray
    val part1 = zeroArrayDouble(iStart)
    val part2 = nonzeroVector.toArray
    val part3 = zeroArrayDouble(knotsVector.length - degree - 1 - iEnd - 1)
    val part = part0 ++ part1 ++ part2 ++ part3
    new breeze.linalg.DenseMatrix(part0.length, 2, part)
  }

  def bSplineEval(x: Double, degree: Int,
                  knotsVector: breeze.linalg.DenseVector[Double]
                 ): breeze.linalg.DenseVector[Double] = {
    val mytuple = bSplineEvalNonzero(x, degree, knotsVector)
    val iStart = mytuple._1
    val iEnd = iStart + degree
    val nonzeroVector = mytuple._2
    val part1 = zeroArrayDouble(iStart)
    val part2 = nonzeroVector.toArray
    val part3 = zeroArrayDouble(knotsVector.length - degree - 1 - iEnd - 1)
    val part = part1 ++ part2 ++ part3
    new breeze.linalg.DenseVector(part)
  }

  def bSplineDerivEvalNonzero(x: Double, degree: Int,
                              knotsVector: breeze.linalg.DenseVector[Double]
                             ): (Int, DenseVector[Double]) = {
    val iStart = bSplineFindInterval(x, degree, knotsVector)
    val iEnd = iStart + degree
    val kMinusOneVector = bSplineEval(x, degree - 1, knotsVector)
    val der = (iStart to iEnd).map { i =>
      myDiv(degree.toDouble * kMinusOneVector(i),
        knotsVector(i + degree) - knotsVector(i)) -
        myDiv(degree.toDouble * kMinusOneVector(i + 1),
          knotsVector(i + degree + 1) - knotsVector(i + 1))
    }
    val derV = new breeze.linalg.DenseVector[Double](der.toArray)
    (iStart, derV)
  }

  def bSplineEvalNonzero(x: Double, degree: Int,
                         knotsVector: breeze.linalg.DenseVector[Double]
                        ): (Int, breeze.linalg.DenseVector[Double]) = {
    val iStart = bSplineFindInterval(x, degree, knotsVector)
    val iEnd = iStart + degree
    val returnV = if (x == knotsVector(0)) {
      val tmp = zeroArrayDouble(degree + 1)
      tmp(0) = 1.0
      tmp
    } else if (x == knotsVector(-1)) {
      val tmp = zeroArrayDouble(degree + 1)
      tmp(degree) = 1.0
      tmp
    } else {
      val tmp = (iStart to iEnd).map(i => findBSplineIK(x, i, degree, knotsVector))
      tmp.toArray
    }
    (iStart, new breeze.linalg.DenseVector(returnV))
  }

  def findBSplineIK(x: Double,
                    i: Int, //subscript
                    k: Int, //degree of B Spline
                    knots: breeze.linalg.DenseVector[Double]): Double = {
    val numOfBreaks = knots.length - 2 * k
    require(numOfBreaks > 2)
    require(i >= 0 && i <= numOfBreaks + k - 1)
    val tmp = if (k == 0) {
      if (knots(i) <= x && x < knots(i + 1)) 1.0 else 0
    } else {
      myDiv(x - knots(i), knots(i + k) - knots(i)) *
        findBSplineIK(x, i, k - 1, knots) +
        myDiv(knots(i + k + 1) - x, knots(i + k + 1) - knots(i + 1)) *
          findBSplineIK(x, i + 1, k - 1, knots)
    }
    tmp
  }

  // find the postion of nonzero B-spline
  def bSplineFindInterval(x: Double, degree: Int,
                          knots: breeze.linalg.DenseVector[Double]
                         ): Int = {
    val position = knots.toArray.count(_ <= x)
    require(position > 0 && position < knots.length || x == knots(-1))
    val res = if (x == knots(-1)) position - 2 * degree - 2 else position - degree - 1
    res
  }

  def findBSplineMatrix(x: breeze.linalg.DenseVector[Double],
                        degree: Int,
                        knotsVector: breeze.linalg.DenseVector[Double]
                       ): breeze.linalg.DenseMatrix[Double] = {
    val result: Array[Double] = x.toArray.flatMap { xElement =>
      val tmpv = bSplineEval(xElement, degree, knotsVector)
      tmpv.toArray
    }
    val tmpMatrix = new breeze.linalg.DenseMatrix(knotsVector.length - degree - 1,
      x.length, result)
    val tM = tmpMatrix.t
    tM
  }

  def findBSplinePrimeMatrix(x: breeze.linalg.DenseVector[Double],
                             knotsVector: breeze.linalg.DenseVector[Double],
                             degree: Int): breeze.linalg.DenseMatrix[Double] = {
    require(knotsVector != null)
    val knotsLength = knotsVector.length

    val result = for (i <- 0 until knotsLength; j <- 0 until x.length)
      yield findBSplineIKPrime(x(j), i, degree, knotsVector)
    new breeze.linalg.DenseMatrix(x.length, knotsLength, result.toArray)
  }

  def findBSplineIKPrime(x: Double,
                         i: Int, //subscript
                         k: Int, //degree of B Spline
                         knots: breeze.linalg.DenseVector[Double]): Double = {
    val numOfBreaks = knots.length
    require(numOfBreaks - 2 - k > 0)
    require(i >= 0 && i <= numOfBreaks - 2 - k)
    require(k > 0)
    myDiv(k.toDouble, knots(i + k) - x) *
      findBSplineIK(x, i, k - 1, knots) -
      myDiv(k.toDouble, knots(i + k + 1) - knots(i + 1)) *
        findBSplineIK(x, i + 1, k - 1, knots)
  }

  def myDiv(nominator: Double, denominator: Double): Double = {
    //  if (denominator == 0) require(nominator == 0)
    if (denominator == 0) 0 else nominator / denominator
  }

  //===============================================================
  //直接在[a,b]中取等间距的节点
  def findKnotsUniform(x1: Double, x2: Double,
                       numOfBreaks: Int, degree: Int
                      ): breeze.linalg.DenseVector[Double] = {
    require(x1 != x2)
    val a = if (x1 < x2) x1 else x2
    val b = if (x2 < x1) x1 else x2
    val delta = (b - a) / (numOfBreaks - 1.0)
    val tmp = (0 until numOfBreaks).map(i => a + i * delta)
    val tmpArray = tmp.toArray
    val tmpBreaks = new breeze.linalg.DenseVector[Double](tmpArray)
    bSplineKnots(degree, tmpBreaks)
  }

  def bSplineKnots(degree: Int,
                   breakVector: breeze.linalg.DenseVector[Double]
                  ): breeze.linalg.DenseVector[Double] = {
    val nbreaks = breakVector.length
    val x0: Double = breakVector(0)
    val x1: Double = breakVector(nbreaks - 1)
    val tmp1 = (0 until degree).map(i => x0)
    val tmp1Vector = new breeze.linalg.DenseVector(tmp1.toArray)
    val tmp2 = ((degree + nbreaks) until (nbreaks + 2 * degree)).map(i => x1)
    val tmp2Vector = new breeze.linalg.DenseVector(tmp2.toArray)
    val tmp3Vector = breeze.linalg.DenseVector.vertcat(tmp1Vector, breakVector)
    breeze.linalg.DenseVector.vertcat(tmp3Vector, tmp2Vector)
  }

  //从输入的数据得到knots
  def findKnotsWithData(numOfBreaks: Int,
                        inputData: breeze.linalg.DenseVector[Double],
                        degree: Int
                       ): breeze.linalg.DenseVector[Double] = {
    require(numOfBreaks > 2)
    val trueVector = findUniqueSequenceSorted(inputData)
    //找出数据中不重复的样本
    val num = trueVector.length //数据中不重复样本的个数
    require(num > numOfBreaks)
    //不重复样本量应大于节点个数
    val a = trueVector(0)
    //最小样本
    val b = trueVector(-1)
    //最大样本
    val tmpVector = breeze.linalg.DenseVector.zeros[Double](numOfBreaks)
    val delta1 = (trueVector(1) - trueVector(0)) / 100.0
    val delta2 = (trueVector(-1) - trueVector(-2)) / 100.0
    val myTrue = true
    for (i <- 1 to (numOfBreaks - 2)) {
      tmpVector(i) = findQuantileBreezeVector(i.toDouble / (numOfBreaks.toDouble - 1.0),
        trueVector, myTrue)
    }
    tmpVector(0) = a - delta1
    tmpVector(-1) = b + delta2
    bSplineKnots(degree, tmpVector)
  }

}

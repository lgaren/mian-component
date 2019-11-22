package bSpline

/**
  * Created by jinss on 7/5/17.
  */

import org.apache.commons.math3.distribution.UniformIntegerDistribution
import org.apache.commons.math3.ml.distance.CanberraDistance
import org.apache.commons.math3.ml.distance.ChebyshevDistance
import org.apache.commons.math3.ml.distance.EuclideanDistance
import org.apache.commons.math3.ml.distance.ManhattanDistance
import scala.reflect.ClassTag

trait myUtil {
  def vectorSum(vec: org.apache.spark.ml.linalg.DenseVector): Double = {
    val vecArray = vec.values
    vecArray.sum
  }

  def colSum(matrix: org.apache.spark.ml.linalg.DenseMatrix): org.apache.spark.ml.linalg.DenseVector = {
    val tmp = for (vec <- matrix.colIter) yield vectorSum(vec.toDense)
    new org.apache.spark.ml.linalg.DenseVector(tmp.toArray)
  }

  def rowSum(matrix: org.apache.spark.ml.linalg.DenseMatrix): org.apache.spark.ml.linalg.DenseVector = {
    val tmp = for (vec <- matrix.rowIter) yield vectorSum(vec.toDense)
    new org.apache.spark.ml.linalg.DenseVector(tmp.toArray)
  }

  def matrixMinusVector(matrix: org.apache.spark.ml.linalg.DenseMatrix,
                        vec: org.apache.spark.ml.linalg.DenseVector): org.apache.spark.ml.linalg.DenseMatrix = {
    val tmp1 = matrix.toArray
    val tmp2 = vec.values
    val matrixLength = tmp1.length
    val vecLength = tmp2.length
    var i = 0
    while (i < matrixLength) {
      var j = 0
      while (j < vecLength && i < matrixLength) {
        tmp1(i) -= tmp2(j)
        j += 1
        i += 1
      }
    }
    new org.apache.spark.ml.linalg.DenseMatrix(matrix.numRows, matrix.numCols, tmp1)
  }

  def vecScale(vec: org.apache.spark.ml.linalg.DenseVector,
               x: Double): org.apache.spark.ml.linalg.DenseVector = {
    require(x != 0.0)
    val tmp = vec.toArray
    val tmp2 = for (y <- tmp) yield y * x
    new org.apache.spark.ml.linalg.DenseVector(tmp2)
  }

  def vecScale(vec: breeze.linalg.DenseVector[Double], x: Double): breeze.linalg.DenseVector[Double] = {
    require(x != 0.0)
    val tmp = vec.toArray
    val tmp2 = for (y <- tmp) yield y * x
    new breeze.linalg.DenseVector(tmp2)
  }

  def matrixScale(matrix: org.apache.spark.ml.linalg.DenseMatrix,
                  x: Double): org.apache.spark.ml.linalg.DenseMatrix = {
    require(x != 0)
    val tmp = matrix.toArray
    val tmp2 = for (y <- tmp) yield y * x
    new org.apache.spark.ml.linalg.DenseMatrix(matrix.numRows, matrix.numCols, tmp2)
  }

  def matrixScale(matrix: breeze.linalg.DenseMatrix[Double],
                  x: Double): breeze.linalg.DenseMatrix[Double] = {
    assert(x != 0)
    val tmp = matrix.toArray
    val tmp2 = for (y <- tmp) yield y * x
    new breeze.linalg.DenseMatrix(matrix.rows, matrix.cols, tmp2)
  }

  def centralizeKernelMatrix(matrix: org.apache.spark.ml.linalg.DenseMatrix
                            ): org.apache.spark.ml.linalg.DenseMatrix = {
    val colsum = colSum(matrix)
    val rowsum = rowSum(matrix)
    val nrow = matrix.numRows
    val tmp1 = vecScale(colsum, 1.0 / nrow)
    val tmp2 = vecScale(rowsum, 1.0 / nrow)
    val tmp3 = matrixMinusVector(matrix, tmp1).transpose
    val tmp4 = matrixMinusVector(tmp3, tmp2).transpose
    val tmp5 = vectorSum(colsum)
    val tmp6 = tmp4.toArray.map(x => x + tmp5 / (nrow * nrow))
    new org.apache.spark.ml.linalg.DenseMatrix(matrix.numRows, matrix.numCols, tmp6)
  }

  def centralizeKernelMatrix(matrix1: org.apache.spark.ml.linalg.DenseMatrix,
                             matrix2: org.apache.spark.ml.linalg.DenseMatrix
                            ): org.apache.spark.ml.linalg.DenseMatrix = {
    val rowsum1 = rowSum(matrix1)
    val nrow1 = matrix1.numRows
    val nrow2 = matrix2.numRows
    val rowsum1scale = vecScale(rowsum1, 1.0 / nrow2)
    val tmp1 = matrixMinusVector(matrix1, rowsum1scale)
    val tmp1t = tmp1.transpose
    val rowsum2 = rowSum(matrix2)
    val rowsum2scale = vecScale(rowsum2, 1.0 / nrow2)
    val tmp2 = matrixMinusVector(tmp1t, rowsum2scale)
    val tmp2t = tmp2.transpose
    val sumMatrix2 = vectorSum(rowsum2)
    val tmp3 = tmp2t.toArray.map(x => x + sumMatrix2 / (nrow1 * nrow2))
    new org.apache.spark.ml.linalg.DenseMatrix(nrow1, nrow2, tmp3)
  }

  def reverseMatrixByColumn(matrix: org.apache.spark.ml.linalg.DenseMatrix
                           ): org.apache.spark.ml.linalg.DenseMatrix = {
    val ncol = matrix.numCols
    val reEye = org.apache.spark.ml.linalg.Vectors.zeros(ncol * ncol).toArray
    for (i <- 0 until ncol) reEye((i + 1) * ncol - i - 1) = 1
    val tmp = new org.apache.spark.ml.linalg.DenseMatrix(ncol, ncol, reEye)
    matrix.multiply(tmp)
  }

  def reverseMatrixByColumn(matrix: breeze.linalg.DenseMatrix[Double]
                           ): breeze.linalg.DenseMatrix[Double] = {
    val ncol = matrix.cols
    matrix(::, (ncol - 1) to 0 by -1)
  }

  def breezeDenseMatrixToSparkDenseMatrix(matrix: breeze.linalg.DenseMatrix[Double]
                                         ): org.apache.spark.ml.linalg.DenseMatrix = {
    val nrow = matrix.rows
    val ncol = matrix.cols
    new org.apache.spark.ml.linalg.DenseMatrix(nrow, ncol, matrix.toArray)
  }

  def sparkDenseMatrixToBreezeDenseMatrix(matrix: org.apache.spark.ml.linalg.DenseMatrix
                                         ): breeze.linalg.DenseMatrix[Double] = {
    val nrow = matrix.numRows
    val ncol = matrix.numCols
    new breeze.linalg.DenseMatrix[Double](nrow, ncol, matrix.toArray)
  }

  def breezeDenseVectorToSparkDenseVector(vec: breeze.linalg.DenseVector[Double]
                                         ): org.apache.spark.ml.linalg.DenseVector = {
    new org.apache.spark.ml.linalg.DenseVector(vec.toArray)
  }

  def sparkDenseVectorToBreezeDenseVector(vec: org.apache.spark.ml.linalg.DenseVector
                                         ): breeze.linalg.DenseVector[Double] = {
    new breeze.linalg.DenseVector[Double](vec.toArray)
  }

  def breezeMatrixSlicing(matrix: breeze.linalg.DenseMatrix[Double],
                          rowIndex: Array[Int] = null,
                          colIndex: Array[Int] = null): breeze.linalg.DenseMatrix[Double] = {
    require(rowIndex != null || colIndex != null)
    val rowLimit = matrix.rows
    val colLimit = matrix.cols
    val tmpRowIndex: Array[Int] = if (rowIndex == null) (0 until rowLimit).toArray else rowIndex
    val tmpColIndex: Array[Int] = if (colIndex == null) (0 until colLimit).toArray else colIndex
    val testRow = tmpRowIndex.count(math.abs(_) >= rowLimit)
    val testCol = tmpColIndex.count(math.abs(_) >= colLimit)
    require(testRow == 0 && testCol == 0)
    val trueRowIndex = tmpRowIndex.map(x => if (x >= 0) x else rowLimit + x)
    val trueColIndex = tmpColIndex.map(x => if (x >= 0) x else colLimit + x)
    val tmpVector = org.apache.spark.ml.linalg.Vectors.zeros(trueRowIndex.length * trueColIndex.length).toArray
    val myTupleArray = for (j <- trueColIndex; i <- trueRowIndex) yield (i, j)
    tmpVector.indices.map(i => tmpVector(i) = matrix(myTupleArray(i)._1, myTupleArray(i)._2))
    new breeze.linalg.DenseMatrix(trueRowIndex.length, trueColIndex.length, tmpVector)
  }

  def sparkMatrixSlicing(matrix: org.apache.spark.ml.linalg.DenseMatrix,
                         rowIndex: Array[Int] = null,
                         colIndex: Array[Int] = null): org.apache.spark.ml.linalg.DenseMatrix = {
    require(rowIndex != null || colIndex != null)
    val rowLimit = matrix.numRows
    val colLimit = matrix.numCols
    val tmpRowIndex: Array[Int] = if (rowIndex == null) (0 until rowLimit).toArray else rowIndex
    val tmpColIndex: Array[Int] = if (colIndex == null) (0 until colLimit).toArray else colIndex
    val testRow = tmpRowIndex.count(math.abs(_) >= rowLimit)
    val testCol = tmpColIndex.count(math.abs(_) >= colLimit)
    require(testRow == 0 && testCol == 0)
    val trueRowIndex = tmpRowIndex.map(x => if (x >= 0) x else rowLimit + x)
    val trueColIndex = tmpColIndex.map(x => if (x >= 0) x else colLimit + x)
    val tmpVector = org.apache.spark.ml.linalg.Vectors.zeros(trueRowIndex.length * trueColIndex.length).toArray
    val myTupleArray = for (j <- trueColIndex; i <- trueRowIndex) yield (i, j)
    tmpVector.indices.map(i => tmpVector(i) = matrix(myTupleArray(i)._1, myTupleArray(i)._2))
    new org.apache.spark.ml.linalg.DenseMatrix(trueRowIndex.length, trueColIndex.length, tmpVector)
  }

  def sampling(lowerLimit: Int,
               upperLimit: Int,
               numOfSamples: Int,
               withoutReplacement: Boolean = true): Array[Int] = {
    require(lowerLimit < upperLimit)

    if (withoutReplacement) require(math.abs(upperLimit - lowerLimit) > 2 * numOfSamples)
    val uf = new UniformIntegerDistribution(lowerLimit, upperLimit)
    val choiceVector: Array[Int] =
      if (withoutReplacement) {
        val t1 = (0 until numOfSamples).map(_ => uf.sample())
        var t1List = t1.toList
        while (t1List.toSet.size < numOfSamples) {
          val t2 = (0 until numOfSamples).map(_ => uf.sample())
          t1List = t2.toList ::: t1List
        }
        val tmpVector = t1List.toSet.toArray
        val tmpVector2 = for (i <- 0 until numOfSamples) yield tmpVector(i)
        tmpVector2.toArray
      } else {
        val tmpVector3 = (0 until numOfSamples).map(_ => uf.sample())
        tmpVector3.toArray
      }
    choiceVector
  }

  def rowSliceOfSparkDenseMatrix(rowIndex: Array[Int],
                                 m: org.apache.spark.ml.linalg.DenseMatrix
                                ): org.apache.spark.ml.linalg.DenseMatrix = {
    val nrow = rowIndex.length
    val ncol = m.numCols
    val tmp = rowIndex.flatMap(i => oneRowOfSparkDenseMatrixArray(i, m))
    val tmpMatrix = new org.apache.spark.ml.linalg.DenseMatrix(ncol, nrow, tmp)
    tmpMatrix.transpose
  }

  def colSliceOfSparkDenseMatrix(colIndex: Array[Int],
                                 m: org.apache.spark.ml.linalg.DenseMatrix
                                ): org.apache.spark.ml.linalg.DenseMatrix = {
    val ncol = colIndex.length
    val nrow = m.numRows
    val tmp = colIndex.flatMap(i => oneColumnOfSparkDenseMatrixArray(i, m))
    new org.apache.spark.ml.linalg.DenseMatrix(nrow, ncol, tmp)
  }

  def rowDropOfSparkDenseMatrix(rowIndex: Int,
                                m: org.apache.spark.ml.linalg.DenseMatrix
                               ): org.apache.spark.ml.linalg.DenseMatrix = {
    val rowIndexArray: Array[Int] = Array(rowIndex)
    rowDropOfSparkDenseMatrix(rowIndexArray, m)
  }

  def colDropOfSparkDenseMatrix(colIndex: Int,
                                m: org.apache.spark.ml.linalg.DenseMatrix
                               ): org.apache.spark.ml.linalg.DenseMatrix = {
    val colIndexArray: Array[Int] = Array(colIndex)
    colDropOfSparkDenseMatrix(colIndexArray, m)
  }


  def rowDropOfSparkDenseMatrix(rowIndex: Array[Int],
                                m: org.apache.spark.ml.linalg.DenseMatrix
                               ): org.apache.spark.ml.linalg.DenseMatrix = {
    val nrow = m.numRows
    val ncol = m.numCols
    val trueDrop = rowIndex.map(i => if (i >= 0) i else nrow + i)
    val over = trueDrop.filter(_ < nrow)
    require(over.length == rowIndex.length)
    val tmpSet1 = (0 until nrow).toSet
    val tmpSet2 = trueDrop.toSet
    val tmpSet3 = tmpSet1 diff tmpSet2
    val tmpArray = tmpSet3.toArray.sorted
    val nrow2 = tmpArray.length
    val tmp = tmpArray.flatMap(i => oneRowOfSparkDenseMatrixArray(i, m))
    val tmpMatrix = new org.apache.spark.ml.linalg.DenseMatrix(ncol, nrow2, tmp)
    tmpMatrix.transpose
  }

  def colDropOfSparkDenseMatrix(colIndex: Array[Int],
                                m: org.apache.spark.ml.linalg.DenseMatrix
                               ): org.apache.spark.ml.linalg.DenseMatrix = {
    val ncol = m.numCols
    val nrow = m.numRows
    val trueDrop = colIndex.map(i => if (i >= 0) i else ncol + i)
    val over = trueDrop.filter(_ < ncol)
    require(over.length == colIndex.length)
    val tmpSet1 = (0 until ncol).toSet
    val tmpSet2 = trueDrop.toSet
    val tmpSet3 = tmpSet1 diff tmpSet2
    val tmpArray = tmpSet3.toArray.sorted
    val ncol2 = tmpArray.length
    val tmp = tmpArray.flatMap(i => oneColumnOfSparkDenseMatrixArray(i, m))
    new org.apache.spark.ml.linalg.DenseMatrix(nrow, ncol2, tmp)
  }

  def oneRowOfSparkDenseMatrixArray(rowIndex: Int,
                                    m: org.apache.spark.ml.linalg.DenseMatrix
                                   ): Array[Double] = {
    val nrow = m.numRows
    val trueRowIndex = if (rowIndex >= 0) rowIndex else nrow + rowIndex
    require(trueRowIndex < nrow)
    val ncol = m.numCols
    val tmp = (0 until ncol).map(i => m(trueRowIndex, i))
    tmp.toArray
  }

  def oneColumnOfSparkDenseMatrixArray(colIndex: Int,
                                       m: org.apache.spark.ml.linalg.DenseMatrix
                                      ): Array[Double] = {
    val ncol = m.numCols
    val trueColumnIndex = if (colIndex >= 0) colIndex else colIndex + ncol
    require(trueColumnIndex < ncol)
    val nrow = m.numRows
    val tmp = (0 until nrow).map(i => m(i, trueColumnIndex))
    tmp.toArray
  }

  def oneRowOfSparkDenseMatrix(rowIndex: Int,
                               m: org.apache.spark.ml.linalg.DenseMatrix
                              ): org.apache.spark.ml.linalg.DenseVector = {
    val nrow = m.numRows
    val trueRowIndex = if (rowIndex >= 0) rowIndex else nrow + rowIndex
    require(trueRowIndex < nrow)
    val ncol = m.numCols
    val tmp = (0 until ncol).map(i => m(trueRowIndex, i))
    new org.apache.spark.ml.linalg.DenseVector(tmp.toArray)
  }

  def oneColumnOfSparkDenseMatrix(colIndex: Int,
                                  m: org.apache.spark.ml.linalg.DenseMatrix
                                 ): org.apache.spark.ml.linalg.DenseVector = {
    val ncol = m.numCols
    val trueColumnIndex = if (colIndex >= 0) colIndex else colIndex + ncol
    require(trueColumnIndex < ncol)
    val nrow = m.numRows
    val tmp = (0 until nrow).map(i => m(i, trueColumnIndex))
    new org.apache.spark.ml.linalg.DenseVector(tmp.toArray)
  }

  def distanceVector(x: org.apache.spark.ml.linalg.DenseVector,
                     y: org.apache.spark.ml.linalg.DenseMatrix,
                     distanceMeasure: String
                    ): org.apache.spark.ml.linalg.DenseVector = {
    require(x.size == y.numCols)
    val xArray = x.toArray
    val yIter = y.rowIter
    val distanceVec = yIter.map { yv =>
      val tmpVector = yv.toArray
      distanceCompute(xArray, tmpVector, distanceMeasure)
    }
    val tmp2 = distanceVec.toArray
    new org.apache.spark.ml.linalg.DenseVector(tmp2)
  }

  def dropPositionArray[T: ClassTag](position: Int, vec: Array[T]): Array[T] = {
    val len = vec.length
    val truePos = if (position >= 0) position else position + len
    val tmpVec: Array[T] = if (truePos >= len || truePos < 0) vec else {
      val part1: Array[T] = vec.slice(0, truePos)
      val part2: Array[T] = vec.slice(truePos + 1, len)
      part1 ++ part2
    }
    tmpVec
  }

  def dropPosition(position: Int, distanceVec: org.apache.spark.ml.linalg.DenseVector
                  ): Array[(Double, Int)] = {
    val t1 = dropPositionArray[Double](position, distanceVec.toArray)
    val t2 = (0 until distanceVec.size).toArray
    val t3 = dropPositionArray[Int](position, t2)
    val tmpSeq = (0 until distanceVec.size - 1).map(i => (t1(i), t3(i)))
    tmpSeq.toArray
  }

  def distanceCompute(x1: Array[Double],
                      x2: Array[Double],
                      distanceMeasure: String): Double = {
    require(x1.length == x2.length && x1.length > 0)
    val tmpDistanceName = distanceMeasure.toLowerCase.trim
    val distance = tmpDistanceName match {
      case "chebyshev" => new ChebyshevDistance()
      case "canberra" => new CanberraDistance()
      case "euclidean" => new EuclideanDistance()
      case "manhattan" => new ManhattanDistance()
      case _ => new EuclideanDistance()
    }
    distance.compute(x1, x2)
  }

  def distanceMatrix(x: org.apache.spark.ml.linalg.DenseMatrix,
                     distanceMeasure: String
                    ): org.apache.spark.ml.linalg.DenseMatrix = {
    val dM = distanceMeasure
    val nrow = x.numRows
    val tmpV = (0 until nrow * nrow).map(i => 0.0).toArray
    for (i <- 0 until nrow) {
      val tmpV2Array = oneRowOfSparkDenseMatrixArray(i, x)
      for (k <- 0 until i) {
        val tmpV3Array = oneRowOfSparkDenseMatrixArray(k, x)
        tmpV(i * nrow + k) = distanceCompute(tmpV2Array, tmpV3Array, dM)
        tmpV(k * nrow + i) = tmpV(i * nrow + k)
      }
    }
    new org.apache.spark.ml.linalg.DenseMatrix(nrow, nrow, tmpV)
  }

  def order(x: Array[Int]): Array[Int] = {
    case class tmpClassJuly19(tmpx: Double, position: Int)
    val length = x.length
    val tmpSeq = (0 until length).map(i => tmpClassJuly19(x(i), i))
    tmpSeq.toArray.sortWith(_.tmpx < _.tmpx).map(y => y.position)
  }

  def order(x: Array[Double]): Array[Int] = {
    case class tmpClassJuly19(tmpx: Double, position: Int)
    val length = x.length
    val tmpSeq = (0 until length).map(i => tmpClassJuly19(x(i), i))
    tmpSeq.toArray.sortWith(_.tmpx < _.tmpx).map(y => y.position)
  }

  def zeroArrayDouble(len: Int): Array[Double] = {
    val tmpV = (0 until len).map(i => 0.0)
    tmpV.toArray
  }

  def zeroArrayInt(len: Int): Array[Int] = {
    val tmpV = (0 until len).map(i => 0)
    tmpV.toArray
  }

  def findUniqueSequenceSorted(inputData: Array[Double]): Array[Double] = {
    val tmpData = inputData.toSet
    tmpData.toArray.sorted
  }

  def findUniqueSequenceSorted(inputData: breeze.linalg.DenseVector[Double]
                              ): breeze.linalg.DenseVector[Double] = {
    val tmpData = inputData.toArray.toSet
    new breeze.linalg.DenseVector(tmpData.toArray.sorted)
  }

  def findQuantileArray(prob: Double,
                        inputData: Array[Double],
                        sortedAndUnique: Boolean = false): Double = {
    require(prob > 0)
    val tmpVector = if (sortedAndUnique) inputData else findUniqueSequenceSorted(inputData)
    val perShare = 1.0 / (tmpVector.length.toDouble - 1.0)
    val position1 = (prob / perShare).toInt
    val a = tmpVector(position1)
    val b = tmpVector(position1 + 1)
    val p1 = position1 * perShare
    val p2 = p1 + perShare
    ((prob - p1) * b + (p2 - prob) * a) / (p2 - p1)
  }


  def findQuantileBreezeVector(prob: Double,
                               inputData: breeze.linalg.DenseVector[Double],
                               sortedAndUnique: Boolean = false): Double = {
    require(prob > 0)
    val tmpVector = if (sortedAndUnique) {
      inputData
    } else {
      findUniqueSequenceSorted(inputData)
    }
    val perShare = 1 / (tmpVector.length - 1.0)
    val position1 = (prob / perShare).toInt
    val a = tmpVector(position1)
    val b = tmpVector(position1 + 1)
    val p1 = position1 * perShare
    val p2 = p1 + perShare
    ((prob - p1) * b + (p2 - prob) * a) / (p2 - p1)
  }

  def getSequence(start: Double, end: Double, length: Int): Array[Double] = {
    require(end > start)
    require(length > 2)
    val delta = (end - start) / (length - 1).toDouble
    val tmp = (0 until length).map(i => delta * i.toDouble + start)
    tmp.toArray
  }
}

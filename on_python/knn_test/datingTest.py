#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/18

machine-learning-test

@author: DSG
"""
from numpy import *

import kNN


def file2matrix(filename):
    fr = open(filename)
    arryOLines = fr.readlines()
    #  Return a new array of given shape and type, filled with zeros.  shape (行数，列数)
    returnMat = zeros((len(arryOLines), 3))
    classLabelVector = []
    index = 0
    for line in arryOLines:
        line = line.strip()
        listFromLine = line.split('\t')
        # "Matrix  returnMat  n x 3 将listFromLine[0:3] 赋给矩阵的某一行"
        returnMat[index, :] = listFromLine[0:3]
        classLabelVector.append(int(listFromLine[-1]))
        index += 1
    fr.close()
    return returnMat, classLabelVector


"""newValue = (oldValue-min)/(max-min)"""


def autoNorm(dataSet):
    minVals = dataSet.min(0)  # min(0)  参数表示纬度的序号，0表示第一维  （列）  ，1 表示第二维 （行）
    #  minVals  [ 0.        0.        0.001156]

    maxVals = dataSet.max(0)  # maxVals  [  9.12730000e+04   2.09193490e+01   1.69551700e+00]
    ranges = maxVals - minVals  # ranges 矩阵
    normDataSet = zeros(shape(dataSet))  # shape( Matrix ) 求出矩阵的 行列数  返回一个 array[0] = 行数，array[1] = 列数
    m = dataSet.shape[0]  # dataSet.shape[0] 下标0表示维度序号，则dataSet.shape[0] 返回矩阵第0维度 1000*3 返回1000

    normDataSet = dataSet - tile(minVals, (m, 1))  # tile(minVals,(m,1)) minVals 表示以minVals(矩阵)为元素，用这个元素拼成m * 1的矩阵
    # 如果 minVals 的行列为 4，3 则生成的新矩阵的行列就是 4000 ，3
    # tile(  {shape = (2,3)  },(4,3)) 返回shape = (8 , 9)
    #   1
    #        X   3   ,2     =  3 ,6
    #   3
    #  如果参数 1 为 2 * 3 的矩阵, 参数2是一个（3,4）  则新矩阵就是 6 * 12 阶

    # normDataSet = (oldValue-min)

    normDataSet = normDataSet / tile(ranges, (m, 1))
    # print shape(tile(zeros((2,3)),(4,3)))
    return normDataSet, ranges, minVals


def datingClassTest(normMat, label, hoRatio, k):
    m = normMat.shape[0]
    numTestVecs = int(m * hoRatio)
    errorCount = 0.0
    for i in range(numTestVecs):
        # classify0(inX, dataSet, labels, k) inX 等待分类的数据， dataSet样本数据 ， 样本数据的类别 ， K值
        # normMat[i,:] 拿第行数据
        # normMat[numTestVecs:m,:]拿取  numTestVecs 到m 行数据
        calssifierResult = kNN.classify0(normMat[i, :], normMat[numTestVecs:m, :], label[numTestVecs:m], k)
        print "the classifier came back with: %d, the real answer is: %d" % (calssifierResult, label[i])
        if (calssifierResult != label[i]): errorCount += 1.0
    print "\nthe total error rate is: %f" % (errorCount / float(numTestVecs))


def datingClass():
    mat, label = file2matrix('../../../resource/knn/datingTestSet2.txt')
    normMat, ranges, minVals = autoNorm(mat)
    hoRatio = 0.1
    m = normMat.shape[0]
    numTestVecs = int(m * hoRatio)
    errorCount = 0.0
    for i in range(numTestVecs):
        # classify0(inX, dataSet, labels, k) inX 等待分类的数据， dataSet样本数据 ， 样本数据的类别 ， K值
        # normMat[i,:] 拿第行数据
        # normMat[numTestVecs:m,:]拿取  numTestVecs 到m 行数据
        calssifierResult = kNN.classify0(normMat[i, :], normMat[numTestVecs:m, :], label[numTestVecs:m], 3)
        print "the classifier came back with: %d, the real answer is: %d" % (calssifierResult, label[i])
        if (calssifierResult != label[i]): errorCount += 1.0
    print "\nthe total error rate is: %f" % (errorCount / float(numTestVecs))


if __name__ == '__main__':
    mat, label = file2matrix('../../../resource/knn/datingTestSet2.txt')
    import matplotlib.pyplot as plt

    # print mat
    fig = plt.figure()
    ax = fig.add_subplot(111)
    '按下标拿数据'
    # ax.scatter(mat[:,0],mat[:,1],15.0*array(label),15.0*array(label))
    # ax.scatter(mat[:,1],mat[:,2],15.0*array(label),15.0*array(label))
    # plt.show()

    normMat, ranges, minVals = autoNorm(mat)
    # datingClassTest(normMat, label, 0.1, 5)

    datingClass()

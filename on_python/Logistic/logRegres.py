#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/27

machine-learning-test

@author: DSG
"""
import matplotlib.pyplot as plt
from numpy import *

from on_python.common import common as params

# http://blog.csdn.net/whai362/article/details/51860379
# http://blog.csdn.net/ariessurfer/article/details/41310525

"""
需求：给一组样本数据，给出这组样本最佳的 ‘分界线’
需求分析：这组样本既然已经被抽到，则就认为这组样本出现的概率最大，而这组样本服从 Xn -- f(Θ)， L（Θ）
       似然函数的最大值就用来描述已经抽出的这组样本就是全部样本空间中抽出这么多样本最应该出现的结果。
       似然则用来描述已知随机变量输出结果时，求未知参数的可能取值
算法：样本的类别只有两类，所以用 Sigmoid 函数来分类，而Sigmoid函数需要一个参数，这个参数直接影响到分类的效果
      因此问题转换为求最佳参数的问题，在这个最佳参数的问题上自然想到了似然函数，在似然函数取最大值的时候的参数，
      似然函数的最大值的概念就是最像，给一组样本，这组样本的最应该的出现那种总情况的概率。
      所以用似然函数取得最大值时候的参数，用梯度上升法来求这组参数。
细节：见 " ../../resource/com.dsglyy.logistic/Logistic.docx "  '../../resource/com.dsglyy.logistic/testSet.txt'
"""


def plotBestFit(weights):
    dataMat, labelMat = loadDataSet()
    dataArr = array(dataMat)
    n = shape(dataArr)[0]
    xcord1 = [];
    ycord1 = []
    xcord2 = [];
    ycord2 = []
    for i in range(n):
        if int(labelMat[i]) == 1:
            xcord1.append(dataArr[i, 1]);
            ycord1.append(dataArr[i, 2])
        else:
            xcord2.append(dataArr[i, 1]);
            ycord2.append(dataArr[i, 2])
    fig = plt.figure()
    ax = fig.add_subplot(111)
    ax.scatter(xcord1, ycord1, s=30, c='red', marker='s')
    ax.scatter(xcord2, ycord2, s=30, c='green')
    x = arange(-3.0, 3.0, 0.1)
    y = (-weights[0] - weights[1] * x) / weights[2]
    ax.plot(x, y)
    plt.xlabel('X1');
    plt.ylabel('X2');
    plt.show()


def loadDataSet():
    dataMat = [];
    labelMat = []
    fr = open('../../resource/logistic/testSet.txt')  # 数据中每一条数据包含两个特征 一个类别
    for line in fr.readlines():
        lineArr = line.strip().split()
        dataMat.append([1.0, float(lineArr[0]), float(lineArr[1])])  # [[1.0, shape1, shape2],.....]
        # 矩阵第一列为1.0的原因是因为，在所有特征中中，每个特征给他乘以一个参数，
        # 然后在加上一个常数项，因此为了奥齿统一和减小计算难度，
        # 认为给所有数据加上一个恒唯一的特征，这个特征用于和 常熟参数想乘。
        labelMat.append(int(lineArr[2]))  # 类别存放对应函数的类别
    return dataMat, labelMat


def sigmoid(inX):
    return 1.0 / (1 + exp(-inX))


def gradAscent(dataMatIn, classLabels):
    dataMatrix = mat(dataMatIn)
    labelMat = mat(classLabels).transpose()  # 转置类别矩阵
    m, n = shape(dataMatrix)  # 求出数据矩阵的各维度的阶数
    alpha = 0.001  # 步长
    maxCycles = 500  # 限定迭代的次数
    weights = ones((n, 1))  # 回归系数矩阵  n x 1
    # print weights
    for k in range(maxCycles):  # 开始迭代
        h = sigmoid(dataMatrix * weights)  # 括号里面的计算 W 的转置乘以 X， 外面的计算 sigmoid函数，是一个向量，是一个矩阵
        error = (labelMat - h)  # y - h(x) 在这里没有乘 X， 在下面乘 X ,  计算估计差，估计误差
        # print h
        weights = weights + alpha * dataMatrix.transpose() * error  # 梯度上升算法，w := w + ▽f(w)
    return weights


def stocGradAscent(dataMatrix, classLabels):
    # print dataMatrix
    m, n = shape(dataMatrix)
    alpha = 0.01
    weights = ones(n)  # 参数  list [1, 1, 1]
    # print weights
    for i in range(m):
        # print (dataMatrix[i]) # [  1.   0.317029  14.739025]
        # print dataMatrix[i] * weights  # [ 1.01718876  0.27239035 -5.35487895]
        h = sigmoid(sum(dataMatrix[i] * weights))  # [ 0.98557465  0.98617913  0.97509038] 每一条破数据乘以参数 求和
        # 这里的参数 实质上可以理解为是没个特征的  权
        # 而这里的做法 类似于 加权求和
        # print h # 0.68169995954 h 为一个数据
        error = (classLabels[i] - h) * dataMatrix[i]  #
        weights = weights + alpha * error
    return weights


def stocGradAscent1(dataMatrix, classLabels, numIter=150):
    m, n = shape(dataMatrix)
    weights = ones(n)
    for j in range(numIter):
        dataIndex = range(m)
        for i in range(m):
            alpha = 4 / (1.0 + j + i) + 0.01  # 调整步长，可以是参数更快的收敛，防止到达底部步长太大，跨过极值点。
            randIndex = int(random.uniform(0, len(dataIndex)))  # 采取随机选取样本，避免周期波动。
            h = sigmoid(sum(dataMatrix[randIndex] * weights))
            error = classLabels[randIndex] - h
            weights = weights + alpha * error * dataMatrix[randIndex]
            del (dataIndex[randIndex])
    return weights


def classifyVector(inX, weights):
    prob = sigmoid(sum(inX * weights))
    if prob > 0.5:
        return 1.0
    else:
        return 0.0


def colicTest():
    frTrain = open(params.logisticPath + 'horseColicTraining.txt')
    frTest = open(params.logisticPath + 'horseColicTest.txt')
    trainingSet = [];
    trainingLabels = []
    for line in frTrain.readlines():
        currLine = line.strip().split('\t')
        lineArr = []
        for i in range(21):
            lineArr.append(float(currLine[i]))
        trainingSet.append(lineArr)
        trainingLabels.append(float(currLine[21]))
    trainWeights = stocGradAscent1(array(trainingSet), trainingLabels, 1000)
    errorCount = 0;
    numTestVec = 0.0
    for line in frTest.readlines():
        numTestVec += 1.0
        currLine = line.strip().split('\t')
        lineArr = []
        for i in range(21):
            lineArr.append(float(currLine[i]))
        if int(classifyVector(array(lineArr), trainWeights)) != int(currLine[21]):
            errorCount += 1
    errorRate = (float(errorCount) / numTestVec)
    print ("the error rate of this test is: %f" % errorRate)
    return errorRate


def multiTest():
    numTests = 10;
    errorSum = 0.0
    for k in range(numTests):
        errorSum += colicTest()
    print ("after %d iterations the average error rate is: %f" % (numTests, errorSum / float(numTests)))


dataMat, labelMat = loadDataSet()

# print gradAscent(dataMat, labelMat)
weights = gradAscent(dataMat, labelMat)
plotBestFit(weights.getA())
# print dataMat
# weights = stocGradAscent(array(dataMat), labelMat)
# plotBestFit(weights)
# weights = stocGradAscent1(array(dataMat), labelMat)
# plotBestFit(weights)

multiTest()  # RuntimeWarning: overflow encountered in exp # 数位越界，解决办法取对数  box-cox变换

# http://blog.csdn.net/jasonzzj/article/details/52017438
# http://blog.csdn.net/bitcarmanlee/article/details/51165444
# http://blog.csdn.net/bitcarmanlee/article/details/51473567

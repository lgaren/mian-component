#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/8/11

machine-learning-course

@author: DSG
"""
from numpy import *
import matplotlib.pyplot as plt

import common.common as params

path = params.globalPath + 'SVM/'


def loadDataSet(fileName):
    dataMat = [];
    labelMat = []
    fr = open(fileName)
    for line in fr.readlines():
        lineArr = line.strip().split('\t')
        dataMat.append([float(lineArr[0]), float(lineArr[1])])
        labelMat.append(float(lineArr[2]))
    return dataMat, labelMat


def selectJrand(i, m):
    j = i
    while (j == i):
        j = int(random.uniform(0, m))
    return j


def clipAlpha(aj, H, L):
    if aj > H:
        aj = H
    if L > aj:
        aj = L
    return aj


"""
函数接受的参数为，原始数据，对应的标签，常数C，常数C越大支持向量越少，允许的损失误差， 最大的迭代次数（没有任何alpha更新的迭代次数）
外循环主要用来保证所有的alpha都已经收敛，内循环用来更型所有的alpha，内循环一次如果没有任何的alpha更新则会把计数器加一，如果有数据更
新则计数器会复位，如果连续没有任何alpha更新的次数达到设置的标准，即计数器值达到40 ，则方法结束，SVM训练完毕
对于alpha的更新没有使用单个迭代，而是采用轮次（外循环），每一轮更新所有的alpha，内循环用来对所有的alpha检查，更新。
因为每条数据对应一个alpha所以内循环一数据量作为循环的次数。
对应的alpha的更新条件以及更新方法，详见文档
"""


def smoSimple(dataMatIn, classLabels, C, toler, maxIter):
    dataMatrix = mat(dataMatIn);
    labelMat = mat(classLabels).transpose()  # 传入的数据为list 转化为矩阵
    b = 0;  # 初始的b为0
    m, n = shape(dataMatrix)
    alphas = mat(zeros((m, 1)))  # 创建 alphas ,初始的全部为0

    # 记录没有任何alpha更新的次数
    iter = 0
    g = 0
    while (iter < maxIter):
        g += 1
        # 用来记录更新的alpha对数，如果选择的两个 alpha 都更新了 则加一
        # 每次循环重置
        alphaPairsChanged = 0
        for i in range(m):

            # 计算f(x)也就是估计值  alphas乘以类别 在乘以 向量与数据集的内积 公式 1
            fXi = float(multiply(alphas, labelMat).T * (dataMatrix * dataMatrix[i, :].T)) + b

            # 计算估计误差
            Ei = fXi - float(labelMat[i])

            # if checks if an example violates KKT conditions
            # 条件1    alphas > 0  or  alphas < C
            # 条件2    labelMat[i]*Ei 乘以对应的类别只是为了添加符号，保证为正的的是后要大于 toler，负的 要 小于 -toler
            # 条件分析 负类别 误差超出允许的范围，并且 alpha < C,或者 正类别 alpha > 0
            # 在这里并不是判断是否满足 0 < alpha < C,仅仅是为了强制alpha 不等于0
            if ((labelMat[i] * Ei < -toler) and (alphas[i] < C)) or (
                (labelMat[i] * Ei > toler) and (alphas[i] > 0)):  # 0 < alphas < C
                j = selectJrand(i, m)  # 随机选择第二个 alpha
                # 计算第二个数据的估计值
                fXj = float(multiply(alphas, labelMat).T * (dataMatrix * dataMatrix[j, :].T)) + b
                Ej = fXj - float(labelMat[j])

                # 缓存old alpha
                alphaIold = alphas[i].copy();
                alphaJold = alphas[j].copy()

                # 计算 alpha 的上下限，计算方法 公式2
                if (labelMat[i] != labelMat[j]):
                    L = max(0, alphas[j] - alphas[i])
                    H = min(C, C + alphas[j] - alphas[i])
                else:
                    L = max(0, alphas[j] + alphas[i] - C)
                    H = min(C, alphas[j] + alphas[i])

                # 如果上下限一样 放弃本次优化
                if L == H: continue

                # 计算η = 2 K12 - K11 - K22    与文档不同的是这里选择去相反数，在文档中目标函数是求最小值，而这里是求最大值结果相同
                eta = 2.0 * dataMatrix[i, :] * dataMatrix[j, :].T - dataMatrix[i, :] * dataMatrix[i, :].T - dataMatrix[
                                                                                                            j,
                                                                                                            :] * dataMatrix[
                                                                                                                 j, :].T

                # 分析可以得到 η 是目标函数的二阶导数，如果二阶导数大于0，说明一阶导数是上升的直线，一阶导数为上升的直线的时候，原函数就是
                # 先降后增加的函数，而这里的问题，是求函数的最大值，所以在 η >= 0 时只能在边界取值alpha = 0 or alpha = C 不需要优化，
                # 对应的数据为支撑向量或者噪音数据，所以跳出笨次优化
                if eta >= 0: continue

                # 按照公式3更新alpha，和文档不同的是还是最大值变最小值
                alphas[j] -= labelMat[j] * (Ei - Ej) / eta

                # 按照公式4裁剪alpha
                alphas[j] = clipAlpha(alphas[j], H, L)

                # 如果alpha变化不明显，则放弃更新第二个alpha
                if (abs(alphas[j] - alphaJold) < 0.00001): continue

                # 按照公式5计算第二个alpha
                alphas[i] += labelMat[j] * labelMat[i] * (alphaJold - alphas[j])  # update i by the same amount as j

                # the update is in the oppostie direction
                # 线面的就是根据公式  6 和 7 更新 b
                b1 = b - Ei - labelMat[i] * (alphas[i] - alphaIold) * dataMatrix[i, :] * dataMatrix[i, :].T - labelMat[
                                                                                                                  j] * (
                                                                                                              alphas[
                                                                                                                  j] - alphaJold) * dataMatrix[
                                                                                                                                    i,
                                                                                                                                    :] * dataMatrix[
                                                                                                                                         j,
                                                                                                                                         :].T
                b2 = b - Ej - labelMat[i] * (alphas[i] - alphaIold) * dataMatrix[i, :] * dataMatrix[j, :].T - labelMat[
                                                                                                                  j] * (
                                                                                                              alphas[
                                                                                                                  j] - alphaJold) * dataMatrix[
                                                                                                                                    j,
                                                                                                                                    :] * dataMatrix[
                                                                                                                                         j,
                                                                                                                                         :].T

                if (0 < alphas[i]) and (C > alphas[i]):
                    b = b1
                elif (0 < alphas[j]) and (C > alphas[j]):
                    b = b2
                else:
                    b = (b1 + b2) / 2.0

                # 更新计数器
                alphaPairsChanged += 1
                print ("iter: %d i:%d, pairs changed %d" % (iter, i, alphaPairsChanged))

        # 如果内循环alpha没有更新，则把计数器加 1 ，有更新就把计数器归 0 ，直到 40 时外循环就会跳出
        # 连续40 次没有alpha更新说明alpha已经收敛不需要在继续更新，达到退出目的
        # 上面alpha设置有允许的误差范围，到达允许的误差值，就不会再更新，所以就会出现。好久不会更新任何的alpha
        if (alphaPairsChanged == 0):
            iter += 1

        else:
            iter = 0
        # print "iteration number: %d" % iter
    print (g)
    return b, alphas


class optStruct:
    def __init__(self, dataMatIn, classLabels, C, toler, kTup):  # Initialize the structure with the parameters
        self.X = dataMatIn
        self.labelMat = classLabels
        self.C = C
        self.tol = toler
        self.m = shape(dataMatIn)[0]
        self.alphas = mat(zeros((self.m, 1)))
        self.b = 0
        # error 缓存
        self.eCache = mat(zeros((self.m, 2)))  # first column is valid flag

        # K的结构如下所示，是一个矩阵
        # K[i,j] 缓存的数 第i 和 j 个向量的乘积
        # 在对象初始化的时候这个所有的内积就已经计算完成。
        self.K = mat(zeros((self.m, self.m)))
        # [[ 0.  0.  0. ...,  0.  0.  0.]
        # [ 0.  0.  0. ...,  0.  0.  0.]
        # ...,
        # [ 0.  0.  0. ...,  0.  0.  0.]
        # [ 0.  0.  0. ...,  0.  0.  0.]]
        for i in range(self.m):
            # 矩阵的第 i 列存放 第 i 个元素与所有的数据向量的内积，
            # kernelTrans一次计算一个向量与所有的数据内积。
            self.K[:, i] = kernelTrans(self.X, self.X[i, :], kTup)


def kernelTrans(X, A, kTup):  # calc the kernel or transform data to a higher dimensional space
    m, n = shape(X)
    K = mat(zeros((m, 1)))
    if kTup[0] == 'lin':
        K = X * A.T  # linear kernel
    elif kTup[0] == 'rbf':
        for j in range(m):
            deltaRow = X[j, :] - A
            K[j] = deltaRow * deltaRow.T
        K = exp(K / (-1 * kTup[1] ** 2))  # divide in NumPy is element-wise not matrix like Matlab
    else:
        raise NameError('Houston We Have a Problem -- \
    That Kernel is not recognized')
    return K


def calcEk(oS, k):
    fXk = float(multiply(oS.alphas, oS.labelMat).T * oS.K[:, k] + oS.b)
    Ek = fXk - float(oS.labelMat[k])
    #
    return Ek


# 6902 + 1600 + 20 17 20 66 52 20 14 50 30 20 50 50 200 100 52  52 13 61 50 200 150 60 200 50 10
print (6902 + 1600 + 20 + 17 + 20 + 66 + 52 + 20 + 14 + 50 + 30 + 20 + 50 + 50 + 200 + 100 + 52 + 52 + 13 + 61 + 50 + 200 + 150 + 60 + 200 + 50 + 10)


# 参数，估计误差第一个alpha，以及 OS对象
def selectJ(i, oS, Ei):  # this is the second choice -heurstic, and calcs Ej

    # 被选中第二个alpha
    maxK = -1;

    # 缓存最大的误差差值，
    maxDeltaE = 0;

    # 选中的alpha的估计差
    Ej = 0

    # 存下所欲的误差值，用来选择误差最大的alpha进行更新
    oS.eCache[i] = [1, Ei]  # set valid #choose the alpha that gives the maximum delta E

    # nonzero 原矩阵有多少维这里返回的元组里面就有几个元素，元素的位置顺序与矩阵维度的顺序相同，每个元素都是一个数组
    # 数组里面的值是元素矩阵中非0元素对应维度的下标
    validEcacheList = nonzero(oS.eCache[:, 0].A)[0]
    if (len(validEcacheList)) > 1:
        for k in validEcacheList:  # loop through valid Ecache values and find the one that maximizes delta E

            # i 已经被选择不用计算
            if k == i: continue  # don't calc for i, waste of time

            # 计算K的估计误差
            Ek = calcEk(oS, k)

            # 在公式3中 Ei - Ek 的值越大越能加快alpha的收敛，所以在这里每次选择最大的误差差，用这个误差差来选择alpha
            deltaE = abs(Ei - Ek)
            if (deltaE > maxDeltaE):
                # 选中的第二个alpha
                maxK = k;

                # 记录最大的误差差
                maxDeltaE = deltaE

                # 对应的估计误差
                Ej = Ek
        return maxK, Ej
    else:  # in this case (first time around) we don't have any valid eCache values
        j = selectJrand(i, oS.m)
        Ej = calcEk(oS, j)
    return j, Ej


def updateEk(oS, k):  # after any alpha has changed update the new value in the cache
    Ek = calcEk(oS, k)
    oS.eCache[k] = [1, Ek]


def innerL(i, oS):
    # 计算估计误差
    Ei = calcEk(oS, i)

    # 判断是否满足KKT条件
    if ((oS.labelMat[i] * Ei < -oS.tol) and (oS.alphas[i] < oS.C)) or (
        (oS.labelMat[i] * Ei > oS.tol) and (oS.alphas[i] > 0)):

        # 选择第二个要更新的alpha
        j, Ej = selectJ(i, oS, Ei)  # this has been changed from selectJrand
        alphaIold = oS.alphas[i].copy();
        alphaJold = oS.alphas[j].copy();
        if (oS.labelMat[i] != oS.labelMat[j]):
            L = max(0, oS.alphas[j] - oS.alphas[i])
            H = min(oS.C, oS.C + oS.alphas[j] - oS.alphas[i])
        else:
            L = max(0, oS.alphas[j] + oS.alphas[i] - oS.C)
            H = min(oS.C, oS.alphas[j] + oS.alphas[i])
        if L == H: print ("L==H"); return 0
        eta = 2.0 * oS.K[i, j] - oS.K[i, i] - oS.K[j, j]  # changed for kernel
        if eta >= 0: print ("eta>=0"); return 0
        oS.alphas[j] -= oS.labelMat[j] * (Ei - Ej) / eta
        oS.alphas[j] = clipAlpha(oS.alphas[j], H, L)

        # 更新完alpha后重新记录缓存
        updateEk(oS, j)  # added this for the Ecache
        if (abs(oS.alphas[j] - alphaJold) < 0.00001): print ("j not moving enough"); return 0
        oS.alphas[i] += oS.labelMat[j] * oS.labelMat[i] * (alphaJold - oS.alphas[j])  # update i by the same amount as j
        updateEk(oS, i)  # added this for the Ecache                    #the update is in the oppostie direction
        b1 = oS.b - Ei - oS.labelMat[i] * (oS.alphas[i] - alphaIold) * oS.K[i, i] - oS.labelMat[j] * (
        oS.alphas[j] - alphaJold) * oS.K[i, j]
        b2 = oS.b - Ej - oS.labelMat[i] * (oS.alphas[i] - alphaIold) * oS.K[i, j] - oS.labelMat[j] * (
        oS.alphas[j] - alphaJold) * oS.K[j, j]
        if (0 < oS.alphas[i]) and (oS.C > oS.alphas[i]):
            oS.b = b1
        elif (0 < oS.alphas[j]) and (oS.C > oS.alphas[j]):
            oS.b = b2
        else:
            oS.b = (b1 + b2) / 2.0
        return 1
    else:
        return 0

    # 200, 0.0001, 10000, ('rbf', k1)


# 函数接受的参数为，原始数据，对应的标签，常数C，常数C越大支持向量越少，允许的损失误差， 最大的迭代次数（没有任何alpha更新的迭代次数）
def smoP(dataMatIn, classLabels, C, toler, maxIter, kTup=('lin', 0)):  # full Platt SMO
    # 将数据封装为对象，数据结构
    oS = optStruct(mat(dataMatIn), mat(classLabels).transpose(), C, toler, kTup)

    # 空循环极速器
    iter = 0
    entireSet = True;
    alphaPairsChanged = 0
    while (iter < maxIter) and ((alphaPairsChanged > 0) or (entireSet)):
        alphaPairsChanged = 0
        if entireSet:  # go over all
            for i in range(oS.m):
                # 仅当两个alpha都已经更新过后才返回1
                alphaPairsChanged += innerL(i, oS)
                print ("fullSet, iter: %d i:%d, pairs changed %d" % (iter, i, alphaPairsChanged))
            iter += 1
        else:  # go over non-bound (railed) alphas
            nonBoundIs = nonzero((oS.alphas.A > 0) * (oS.alphas.A < C))[0]
            for i in nonBoundIs:
                alphaPairsChanged += innerL(i, oS)
                print ("non-bound, iter: %d i:%d, pairs changed %d" % (iter, i, alphaPairsChanged))
            iter += 1
        # 每一轮内循环结束就切换entireSet的值
        # 当为True时改为False
        # 为True是表示第一歌alpha按照顺序取，第二个按误差差最大的拿
        # 为False的时候表示第一个alpha拿取费边界值
        if entireSet:
            entireSet = False  # toggle entire set loop
        # 当为没有更新任何alpha时改为True
        elif (alphaPairsChanged == 0):
            entireSet = True
        print ("iteration number: %d" % iter)
    return oS.b, oS.alphas


def calcWs(alphas, dataArr, classLabels):
    X = mat(dataArr);
    labelMat = mat(classLabels).transpose()
    m, n = shape(X)
    w = zeros((n, 1))
    for i in range(m):
        w += multiply(alphas[i] * labelMat[i], X[i, :].T)
    return w


def testRbf(k1=1.3):
    dataArr, labelArr = loadDataSet(path + 'testSetRBF.txt')
    b, alphas = smoP(dataArr, labelArr, 200, 0.0001, 10000, ('rbf', k1))  # C=200 important
    datMat = mat(dataArr);
    labelMat = mat(labelArr).transpose()
    svInd = nonzero(alphas.A > 0)[0]
    sVs = datMat[svInd]  # get matrix of only support vectors
    labelSV = labelMat[svInd];
    print ("there are %d Support Vectors" % shape(sVs)[0])
    m, n = shape(datMat)
    errorCount = 0
    for i in range(m):
        kernelEval = kernelTrans(sVs, datMat[i, :], ('rbf', k1))
        predict = kernelEval.T * multiply(labelSV, alphas[svInd]) + b
        if sign(predict) != sign(labelArr[i]): errorCount += 1
    print ("the training error rate is: %f" % (float(errorCount) / m))
    dataArr, labelArr = loadDataSet(path + 'testSetRBF2.txt')
    errorCount = 0
    datMat = mat(dataArr);
    labelMat = mat(labelArr).transpose()
    m, n = shape(datMat)
    for i in range(m):
        kernelEval = kernelTrans(sVs, datMat[i, :], ('rbf', k1))
        predict = kernelEval.T * multiply(labelSV, alphas[svInd]) + b
        if sign(predict) != sign(labelArr[i]): errorCount += 1
    print ("the test error rate is: %f" % (float(errorCount) / m))


def img2vector(filename):
    returnVect = zeros((1, 1024))
    fr = open(filename)
    for i in range(32):
        lineStr = fr.readline()
        for j in range(32):
            returnVect[0, 32 * i + j] = int(lineStr[j])
    return returnVect


# 雪花秀
def loadImages(dirName):
    from os import listdir
    hwLabels = []
    trainingFileList = listdir(dirName)  # load the training set
    m = len(trainingFileList)
    trainingMat = zeros((m, 1024))
    for i in range(m):
        fileNameStr = trainingFileList[i]
        fileStr = fileNameStr.split('.')[0]  # take off .txt
        classNumStr = int(fileStr.split('_')[0])
        if classNumStr == 9:
            hwLabels.append(-1)
        else:
            hwLabels.append(1)
        trainingMat[i, :] = img2vector('%s/%s' % (dirName, fileNameStr))
    return trainingMat, hwLabels


def testDigits(kTup=('rbf', 10)):
    dataArr, labelArr = loadImages(path + 'digits/trainingDigits')
    b, alphas = smoP(dataArr, labelArr, 200, 0.0001, 10000, kTup)
    datMat = mat(dataArr);
    labelMat = mat(labelArr).transpose()
    svInd = nonzero(alphas.A > 0)[0]
    sVs = datMat[svInd]
    labelSV = labelMat[svInd];
    print ("there are %d Support Vectors" % shape(sVs)[0])
    m, n = shape(datMat)
    errorCount = 0
    for i in range(m):
        kernelEval = kernelTrans(sVs, datMat[i, :], kTup)
        predict = kernelEval.T * multiply(labelSV, alphas[svInd]) + b
        if sign(predict) != sign(labelArr[i]): errorCount += 1
    print ("the training error rate is: %f" % (float(errorCount) / m))
    dataArr, labelArr = loadImages(path + 'digits/testDigits')
    errorCount = 0
    datMat = mat(dataArr);
    labelMat = mat(labelArr).transpose()
    m, n = shape(datMat)
    for i in range(m):
        kernelEval = kernelTrans(sVs, datMat[i, :], kTup)
        predict = kernelEval.T * multiply(labelSV, alphas[svInd]) + b
        if sign(predict) != sign(labelArr[i]): errorCount += 1
    print ("the test error rate is: %f" % (float(errorCount) / m))


dataArr, labelArr = loadDataSet(path + 'testSet.txt')
print ( mat(dataArr)[1, :])
print  (mat(dataArr)[:, 1])
# b, alphas = smoSimple(dataArr, labelArr, 0.6, 0.001, 40)
# b, alphas = smoP(dataArr, labelArr, 0.6, 0.001, 40)
# w = calcWs(alphas, dataArr, labelArr)
# testRbf()
# testDigits()
# b2 =array([[True, False, True], [True, False, False]])
# print b2[0][0]

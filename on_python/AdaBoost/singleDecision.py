# -*- coding: utf-8 -*-

"""
Created on 2018/7/8  Sun PM 15:45 

mian-component 

@author: yuanyuan.liu@dsglyy.com

Version:  test V 0.0, Jul 08, 2018 DSG Exp$$


"""
from  numpy import *

def loadSimpData():
    datMat = matrix([[1.,2.1],
                     [2.,1.1],
                     [1.3,1.],
                     [1.,1.],
                     [2.,1.]])
    classLabels = [1.0,1.0,-1.0,-1.0,1.0]
    return datMat,classLabels

def stumpClassify(dataMatrix,dimen,threshVal,threshIneq):
    retArray = ones((shape(dataMatrix)[0],1))
    if threshIneq == 'lt' :
        retArray[dataMatrix[:,dimen] <= threshVal] = -1.0
    else:
        retArray[dataMatrix[:,dimen] > threshVal] = 1.0

    # print  dataMatrix[:,dimen].T ,threshVal ,retArray. T
    return retArray

def buildStump(dataArr,classLabels,D):
    dataMatrix = mat (dataArr); labelMat = mat(classLabels).T
    m,n = shape(dataMatrix)
    numSteps = 10.0

    """
       第一层循环用来变里所有的特征，
       numSteps 为表示在一个特征上最多遍历几次，用这个特征的最大致减去最小值，除以numSteps就是步长。
    """

    bestStump={}
    bestClasEst=mat(zeros((m,1)))
    minError=inf
    for i in range(n):
        rangeMin=dataMatrix[:,i].min()
        rangeMax=dataMatrix[:,i].max()
        stepSize = (rangeMax-rangeMin)/numSteps
        for j in range(-1,int(numSteps) + 1):
            for inequal in ['lt','gt']:
                threshVal = (rangeMin + float(j) * stepSize)
                predictedVals = stumpClassify(dataMatrix,i,threshVal,inequal)
                errArr = mat(ones((m,1)))
                errArr[ predictedVals == labelMat  ] = 0

                # 乘以权重
                weightedError = D.T * errArr
                #print "split:dim %d,thresh %.2f, thresh inequal : %s, the weighted error is %.3f" % (i,threshVal,inequal,weightedError)
               # < 是numpy中提供的方法
                if weightedError < minError:
                    minError = weightedError
                    bestClasEst = predictedVals.copy()
                    bestStump['dim'] = i
                    bestStump['thresh'] = threshVal
                    bestStump['ineq']= inequal
    return bestStump,minError,bestClasEst


def adaBoostTrainDS(dataArr,classLabels,numIt=40):
    weakClassArr= []
    m = shape(dataArr)[0]
    D = mat(ones((m,1))/m)
    aggClassEst = mat(zeros((m,1)))
    for i in range(numIt):
        print ("D : ", D.T)
        bestStump,error,classEst = buildStump(dataArr,classLabels,D)
        alpha = float(0.5*log((1.0 - error)/max(error,1e-16)))
        print (alpha)
        bestStump['alpha'] = alpha
        weakClassArr.append(bestStump)
        print ("classEst:" ,classEst.T)

D = mat(ones((5,1))/5)  #  权重
print (type(D.T))

dataMat,classLabels= loadSimpData()
print (buildStump(dataMat,classLabels,D))

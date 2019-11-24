#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/19

machine-learning-test

@author: DSG
"""
from  numpy import *

import kNN


def img2vector(fileName):
    returnVector = zeros((1, 1024))
    fr = open(fileName, 'r')
    lines = fr.readlines()
    for i in range(32):
        linestr = lines[i]
        for j in range(32):
            returnVector[0, 32 * i + j] = int(linestr[j])
    fr.close()
    return returnVector


def loadData(path):
    hwlabels = []
    trainingFileList = os.listdir(path)
    m = len(trainingFileList)
    trainingMat = zeros((m, 1024))
    for i in range(m):
        fileName = trainingFileList[i]
        hwlabels.append(int(fileName.split('_')[0]))
        trainingMat[i, :] = img2vector(path + '/' + fileName)
    return hwlabels, trainingMat


if __name__ == '__main__':
    #  训练  trainingDigits
    #  测试  testDigits
    #  1_72.txt
    # img2vector('../../machine_learning_in_action/Ch02/digits/trainingDigits/0_0.txt')
    hwlabels, trainingMat = loadData('../../machine_learning_in_action/Ch02/digit s/trainingDigits')

    path = '../../machine_learning_in_action/Ch02/digits/testDigits'
    testFileList = os.listdir(path)
    errorCount = 0.0
    mTest = len(testFileList)
    for i in range(mTest):
        fileName = testFileList[i]
        classNum = int(fileName.split('_')[0])
        imgVector = img2vector(path + '/' + fileName)
        classifierResult = kNN.classify0(imgVector, trainingMat, hwlabels, 3)
        print ("the classifier came back with: %d, the real answer is: %d" % (classifierResult, classNum))
        if (classifierResult != classNum): errorCount += 1.0
    print ("\nthe total number of errors is: %d" % errorCount)
    print ("\nthe total error rate is: %f" % (errorCount / float(mTest)))

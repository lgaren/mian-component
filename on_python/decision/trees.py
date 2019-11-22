#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/20

machine-learning-test

@author: DSG
"""
import operator
import pickle
from math import log  # log运算

import treePlotter


def calcShannonEnt(dataSet):
    numEntries = len(dataSet)  #返回当前数据集中数据量

    labelCounts = {}
    for featVec in dataSet:
        currentLabel = featVec[-1]
        # 计算每个类别出现的次数  矩阵的最后一列为类别，这里是计算所有的类别而不是所有的特征
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] = 0
        labelCounts[currentLabel] += 1
    shannonEnt = 0.0
    for key in labelCounts:
        # 计算概率
        prob = float(labelCounts[key]) / numEntries
        # 计算香农熵
        shannonEnt -= prob * log(prob, 2)
    return shannonEnt


def createDataSet():
    dataSet = [[1, 1, 'yes'], [1, 1, 'yes'], [1, 0, 'no'], [0, 1, 'no'], [0, 1, 'no']]

    # labels 属性列表。第一第二列的别名。
    labels = ['no surfacing', 'flippers']
    return dataSet, labels


"""
  dataSet数据集； axis数据特征（其实是，dataSet是一个矩阵， axis对应的列数及特征所在的位置，
  value，当axis指定位置的特征值等于 value时，则这一行的数据是本次抽取
  操作需要返回的数据，所有的要返回的数据存于一个集合中一并返回,在返回的数据中丢弃第一列）
  细节：每次计算后就会消耗一个特征，急返回的数据集中不在有这个特征。。。。。。。
"""


def splitDataSet(dataSet, axis, value):
    returnDataSet = []
    for featVec in dataSet:
        if featVec[axis] == value:
            reducedFeatVec = featVec[:axis]
            reducedFeatVec.extend(featVec[axis + 1:])
            returnDataSet.append(reducedFeatVec)
    return returnDataSet


"""
   需求：这个是要找出数据中所有的特征中，那个才是最好的划分数据的特征，依据就是信息增益量，即香农差，
   思路：按照每个特征把数据进行划分，然后在计算划分后的香农熵加权平均和，然后与原始数据集的香农熵比较，最后求出增益最大的特征
   细节：计算某个的特征下的所有取值（遍历数据集拿到每条数据在这个特征下的取值，最后去重），然后按照每个取值把数据进行划分，
         在求划分后的香农熵

         每次分支后就少一个特征
"""


def chooseBestFeatureToSplit(dataSet):
    numFeatures = len(dataSet[0]) - 1  # 统计数据所拥有的的特征总和，在矩阵中其实就是行下表的最大位置，在此位置之前的标记全是特征信息
    baseEntropy = calcShannonEnt(dataSet)  # 计算总数据集的香农熵
    baseInfoGain = 0.0;
    bestFeature = -1
    for i in range(numFeatures):  # 外循环找出所有的所有特征的的所有不重复取值
        featList = [example[i] for example in dataSet]  # 针对某个特征，找出数据集中的所有的值
        uniqueVals = set(featList)  # 对整个数据中的某个特征下的所有的值 去重。
        newEntropy = 0.0
        for value in uniqueVals:  # 内循环计算某个特征在某个取值下的所有数据集中的香农熵
            subDataSet = splitDataSet(dataSet, i, value)  # 按某个类别抽取数据 抽取数据
            prob = len(subDataSet) / float(len(dataSet))  # 计算分类子集与数据全集之间的比例
            newEntropy += prob * calcShannonEnt(subDataSet)  # 计算某个分类子集的香农熵的加权平均
        infoGain = baseEntropy - newEntropy  # 计算信息增益量 ，最大的信息增益特征就是最好的分类依据
        if (infoGain > baseInfoGain):  # 香农熵越大数据越混乱，则最好的分类方法应该使得数据的香农熵减小，所以用最原始的香农熵减去新的香农熵
            # 差值越大，增益越大
            baseInfoGain = infoGain
            bestFeature = i
    return bestFeature


def majorityCnt(classList):
    classCount = {}
    for vote in classList:
        if vote not in classCount.keys(): classCount[vote] = 0
        classCount[vote] += 1
    # operator.itemgetter(1)返回一个函数，必须作用在某个对象上才起作用。
    sortedClassCount = sorted(classCount.iteritems(), key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]


"""
需求：递归创建决策树
思路：把接受到的决策树，首先判断是否需要结束递归，如果结束则直接返回，返回的内容将作为上次递归的的叶子节点
     如果没返回，则继续计算，划分的方法就是，用最佳的信息增益来划分，通过计算香农上的的办法找到最佳的划分属性，然后这个属性
     的每一个取值都会作为本次生成树的一个孩子节点，创建完所有的孩子节点后，返回本次生成的树
{'no surfacing': {0: 'no', 1: {'flippers': {0: 'no', 1: 'yes'}}}}
"""


def createTree(dataSet, labels):
    classList = [example[-1] for example in dataSet]  # 拿取所有的类别标签
    if classList.count(classList[0]) == len(classList):  # 递归停止的第一个条件
        # 所有的类标签都一样，满足停止的条件，则不需要在继续，返回
        return classList[0]
    if len(dataSet[0]) == 1:  # 递归停止的第二个条件
        #  此处消耗掉所有的属性，矩阵的最后一列为类别标签，不是属性，所以判断条件为  len(dataSet[0])  == 1
        #  而第一个返回条件还未结束，说明属性用完了，而类别却不唯一，之能选举次数最多的作为类别
        return majorityCnt(classList)
    bestFeat = chooseBestFeatureToSplit(dataSet)  # 找出本次划分数据的最佳属性
    bestFeatLabels = labels[bestFeat]
    myTree = {bestFeatLabels: {}}  # 以本次的最佳属性为划分条件（父节点），生成子树，下面的每次循环都会产生一个孩子节点
    del (labels[bestFeat])  # 消耗掉一个属性
    featValues = [example[bestFeat] for example in dataSet]  # 找出本次数据集中所要划分属性的所有非重复取值，在下面，每一个取值将会产生一个子树
    uniqueValues = set(featValues)
    for value in uniqueValues:
        subLabels = labels[:]  # 将剩余的属性复制子出来，以供下次递归使用
        myTree[bestFeatLabels][value] = createTree(splitDataSet(dataSet, bestFeat, value),
                                                   subLabels)  # 递归调用，调用的结果是在本次的树中产生一个孩子节点
    return myTree  # 本次调用结束返回生成的决策树，这个决策树如果部署最终的结果就是上次调用的一个子树


"""
参数已经存在的决策树， 构造决策树的标签向量， 待分类的数据
待分类的数据是一条完整的数据（包含所有的属性，没有类别标签）
思路; 拿到某个标签下的所有孩子节点，（孩子节点其实就是该属性的所有取值），然后遍历所有的属性取值（孩子节点）
     同时和，拿到测试数据中的改属性的去取值，测试数据的取值和，当前决策树中的属性取值完全一样的时候，则走入的求分类的diamante中
"""


def classify(inputTree, featLabels, testVec):
    # 拿取本次的标签
    firstStr = inputTree.keys()[0]
    # 拿取 firstStr 所有的孩子节点
    secondDict = inputTree[firstStr]
    # 找到特征标签在，标签向量中的位置，这个位置也是本次需要判断特征，也是测试数据中的下表
    featIndex = featLabels.index(firstStr)
    for key in secondDict.keys():  # secondDict.keys() 得到的是 属性firstStr在决策树中的所有取值
        #  这个循环的虽然是遍历 secondDict.keys() ，但是他加入了测试数据的下表判断，
        # 因此整个分类的程序的流程控制并不在循环，而是数据的所有标签，
        # 再循环中只有在属性取值匹配时才走入子树中，不然直接pass
        if testVec[featIndex] == key:
            if type(secondDict[key]).__name__ == 'dict':  # 还未到海子节点，需要继续递归
                classLabel = classify(secondDict[key], featLabels, testVec)
            else:
                classLabel = secondDict[key]
    return classLabel


def storeTree(inputTree, filename):
    fw = open(filename, 'w')
    pickle.dump(inputTree, fw)
    fw.close()


def grabTree(filename):
    fr = open(filename)
    return pickle.load(fr)


if __name__ == '__main__':
    myData, labels = createDataSet()
    # 香农熵越大，数据越混乱
    # print calcShannonEnt(myData)
    # print splitDataSet(myData,0,1)
    print chooseBestFeatureToSplit(myData)
    #
    myTree = createTree(myData, labels)
    print myTree
    print classify(myTree, ['no surfacing', 'flippers'], [0, 0])
    storeTree(myTree, '../../../resource/decision/decision')
    print grabTree('../../../resource/decision/decision')

    # ========================test===========================
    fr = open('../../../resource/decision/lenses.txt')
    lense = [line.strip().split('\t') for line in fr.readlines()]
    lenseLabels = ['age', 'perscript', 'astigmatic', 'tearRate']
    lenseTree = createTree(lense, lenseLabels)
    print lenseTree
    treePlotter.createPlot(lenseTree)

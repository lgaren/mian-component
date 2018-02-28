#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
* ━━━━━━神兽出没━━━━━━
* 　　　┏┓　　　┏┓
* 　　┏┛┻━━━┛┻┓
* 　　┃　　　　　　　┃
* 　　┃　　　━　　　┃
* 　　┃　┳┛　┗┳　┃
* 　　┃　　　　　　　┃
* 　　┃　　　┻　　　┃
* 　　┃　　　　　　　┃
* 　　┗━┓　　　┏━┛
* 　　　　┃　　　┃神兽保佑, 永无BUG!
* 　　　　┃　　　┃Code is far away from bug with the animal protecting
* 　　　　┃　　　┗━━━┓
* 　　　　┃　　　　　　　┣┓
* 　　　　┃　　　　　　　┏┛
* 　　　　┗┓┓┏━┳┓┏┛
* 　　　　　┃┫┫　┃┫┫
* 　　　　　┗┻┛　┗┻┛
* ━━━━━━━━━━━━━━━━
*
Created on 2017/7/25

machine-learning-test

@author: DSG
"""

# train  : 训练
import re
from numpy import *
import feedparser


def loadDtaSet():
    data = """  my dog has flea problems help please
      maybe not take him to dog park stupid
      my dalmation is so cute I love him
      stop posting stupid worthless garbage
      mr licks ate my steak how to stop him
      quit buying worthless dog food stupid"""
    # list .remove 位空返回值，所以最终全部为none
    dataSet = [re.split("\s*", line.strip()) for line in data.split('\n')]
    classVec = [0, 1, 0, 1, 0, 1]
    return dataSet, classVec


"""
创建一个包含数据集中所有不重复的单词的list
"""


def createVocabList(dataSet):
    vocabSet = set([])
    for document in dataSet:
        vocabSet = vocabSet | set(document)
    return list(vocabSet)


"""
输入一个词汇向量，以及文档，然后返回一个向量，返回的向量中与词汇向量具有相同的维度，如果词汇的向量中的
某个词出现在了输入文档中，则在返回向量的对应维度就会是1，其余的维度为0
"""


def setOfWords2Vec(vocabList, inputSet):
    returnVec = [0] * len(vocabList)
    for word in inputSet:
        if word in vocabList:
            returnVec[vocabList.index(word)] = 1
        else:
            print "the word : s% is not in my Vocabulary!" % word
    return returnVec


"""
需求：求某个类别中的每一个词汇，在这个类别中的中词汇中的概率，条件概率

trainMatrix: 所有文档与所有词汇对应的向量，向量的长度就是所有词汇的个数，当包含某个人词汇的时候，对应的单位就是1
             否则就是0；
trainCatrgory：就是所有文档对应的标签，是否包含脏话
返回两个概率向量，以及一个脏话类别文档占总文档的概率，
概率向量分别是脏话和非脏话的概率向量，向量每一个维度上的取值都表示这个类别中对应位置词汇占这个类别中总词汇的比份
所有类别的总和都是对样本空间的一个切分，最后在每个类别中求每个词汇所占的比份
事件 C 就是某个类别，事件 W 就是某个词汇出现。p(Wi|C) 就是这个函数最终返回的结果
最终返回的词汇向量中，包含了所有的p(w|c)
"""

def trainNB0(trainMatrix, trainCategory):
    # 获取文档总数
    numTrainDocs = len(trainMatrix)
    # 非重复的词汇总数
    numWords = len(trainMatrix[0])
    # 计算包含脏话文档的概率
    pAbusive = sum(trainCategory) / float(numTrainDocs)
    # 构造两个矩阵，用于存放两个类别的所有向量
    p0Num = ones(numWords);
    p1Num = ones(numWords)
    p0Denom = 0.0;
    p1Denom = 0.0
    # 遍历所有的文档，if判断，当文档是脏话的时候
    for i in range(numTrainDocs):
        # 当网当时脏话的时候
        if trainCategory[i] == 1:
            # 这里矩阵想加，并不是把向量全部放在矩阵里面，而是对应元素求和
            p1Num += trainMatrix[i]
            # 计算这个类别中的中词汇数
            p1Denom += sum(trainMatrix[i])
        # 不是脏话 处理方法和上面的一样
        else:
            p0Num += trainMatrix[i]
            p0Denom += sum(trainMatrix[i])
    # 这个操作相当于矩阵中的每个元素都除以这个类别中的词汇总数，下同
    # 取自然对数，避免数值下移除
    p1Vect = log(p1Num / p1Denom)
    p0Vect = log(p0Num / p0Denom)
    return p0Vect, p1Vect, pAbusive


"""
需求：输入测试是数据的向量，和每个类别中词汇的条件概率，以及每个类别的概率，算出测试数据属于的类别

p(Ci) = p(W|Ci)p(Ci) / p(W)  在这里因为最终结果只需要知道所属的类别，类别有大小判断得出，所以这里将不再  除以 p（W）
                             因为p(W)的符号为正，所以在两个概率上都除以或不出不会影响他们的大小关系

"""


def classifyNB(vec2Classify, p0Vec, p1Vec, pClass1):
    # 因为采用了避免下溢出措施，对概率去自然对数，所以在这里乘法就会变成加法
    print sum(vec2Classify * p1Vec)
    p1 = sum(vec2Classify * p1Vec) + log(pClass1)  # element-wise mult
    p0 = sum(vec2Classify * p0Vec) + log(1.0 - pClass1)
    if p1 > p0:
        return 1
    else:
        return 0


def testingNB():
    dataSet, classVec = loadDtaSet()
    myVocabList = createVocabList(dataSet)
    trainMat = []
    for doc in dataSet:
        trainMat.append(setOfWords2Vec(myVocabList, doc))
    p0V, p1V, pAb = trainNB0(array(trainMat), array(classVec))
    testEntry = ['love', 'my', 'dalmation']
    thisDoc = array(setOfWords2Vec(myVocabList, testEntry))
    print testEntry, 'classified as: ', classifyNB(thisDoc, p0V, p1V, pAb)
    testEntry = ['stupid', 'garbage']
    thisDoc = array(setOfWords2Vec(myVocabList, testEntry))
    print testEntry, 'classified as: ', classifyNB(thisDoc, p0V, p1V, pAb)


"""
输入一个词汇向量，以及文档，然后返回一个向量，返回的向量中与词汇向量具有相同的维度，如果词汇的向量中的
与上面不同的是，上面相当于是去重每个次只记录一次，而这里某个词出现在了输入文档中，则在返回向量的对应维度就会 +1，
向量中对应位置的值代表词汇出现的次数
"""


def bagOfWords2Vec(vocabList, inputSet):
    returnVec = [0] * len(vocabList)
    for word in inputSet:
        if word in vocabList:
            returnVec[vocabList.index(word)] += 1
        else:
            print "the word : %s is not in my Vocabulary!" % word
    return returnVec


"""
将子符串处理成单词表
"""


def textParse(bigString):
    import re
    listOfTokens = re.split(r'\W*', bigString)
    return [tok.lower() for tok in listOfTokens if len(tok) > 2]


# ../../resource/bayes/spam/%d.txt
def spamTest():
    docList = [];  # 用于存放所有的文档
    classList = [];  # 存放上面所有文档的类别
    for i in range(1, 26):  # 每个文件下有 25 个文档
        wordList = textParse(open('../../resource/bayes/email/spam/%d.txt' % i).read())
        docList.append(wordList);
        classList.append(1)
        wordList = textParse(open('../../resource/bayes/email/ham/%d.txt' % i).read())
        docList.append(wordList);
        classList.append(0)

    # 在这里抽取十组测试数据。
    testSet = []  # 用于存放测试数据
    trainingSet = range(50)  #
    vocabList = createVocabList(docList)  # 创建词汇向量
    for i in range(10):
        randIndex = int(random.uniform(0, len(trainingSet)))  # 随机抽取10组数据
        testSet.append(trainingSet[randIndex])
        del (trainingSet[randIndex])  # 删除已经抽取的数据，剩余的数据用于训练数据集
    trainMat = []  # 用于存放训练数据向量
    trainClass = []  # 存放训练数据的类别
    for docIndex in trainingSet:
        trainMat.append(setOfWords2Vec(vocabList, docList[docIndex]))
        trainClass.append(classList[docIndex])
    p0V, p1V, psPam = trainNB0(array(trainMat), array(trainClass))  # 计算条件概率
    errorCount = 0;
    for docIndex in testSet:
        wordVector = setOfWords2Vec(vocabList, docList[docIndex])  # 计算文档词汇的向量
        if classifyNB(array(wordVector), p0V, p1V, psPam) != classList[docIndex]:  # 测试数据分类
            errorCount += 1
    print 'the error rate is: ', float(errorCount) / len(testSet)


"""
计算出现最多的词汇
"""


def calcMostFreq(vocabList, fullText):
    import operator
    freqDict = {}
    for token in vocabList:
        freqDict[token] = fullText.count(token)
    sortedFreq = sorted(freqDict.iteritems(), key=operator.itemgetter(1), reverse=True)
    return sortedFreq[:30]


def localWords(feed1, feed0):
    docList = [];
    classList = [];
    fullText = []
    # 木桶能装多少水取决于最短的那个木板
    minLen = min(len(feed1['entries']), len(feed0['entries']))
    # 分别读取两类数据
    for i in range(minLen):
        wordList = textParse(feed1['entries'][i]['summary'])
        docList.append(wordList)
        fullText.append(wordList)
        classList.append(1)
        wordList = textParse(feed0['entries'][i]['summary'])
        docList.append(wordList)
        fullText.append(wordList)
        classList.append(0)
    vocabList = createVocabList(docList)
    top30Words = calcMostFreq(vocabList, fullText)

    # 删除出现最高的30 个词汇
    for pairW in top30Words:
        if pairW[0] in vocabList: vocabList.remove(pairW[0])
    trainingSet = range(2 * minLen);
    testSet = []

    # 抽取测试数据集
    for i in range(20):
        randIndex = int(random.uniform(0, len(trainingSet)))
        testSet.append(trainingSet[randIndex])
        del (trainingSet[randIndex])
    trainMat = [];
    trainClass = []
    for docIndex in trainingSet:
        trainMat.append(bagOfWords2Vec(vocabList, docList[docIndex]))
        trainClass.append(classList[docIndex])
    p0V, p1V, pSpam = trainNB0(array(trainMat), array(trainClass))
    errorCount = 0;
    for docIndex in testSet:
        wordVector = bagOfWords2Vec(vocabList, docList[docIndex])
        print wordVector
        if classifyNB(array(wordVector), p0V, p1V, pSpam) != classList[docIndex]:
            errorCount += 1
    print 'the error rate is :', float(errorCount) / len(testSet)
    return vocabList, p0V, p1V


def getTopWords(ny, sf):
    import operator
    vocabList, p0V, p1V = localWords(ny, sf)

    topNY = [];
    topSF = []
    for i in range(len(p0V)):
        if p0V[i] > -6.0: topSF.append((vocabList[i], p0V[i]))
        if p0V[i] > -6.0: topNY.append((vocabList[i], p0V[i]))
    sortedSF = sorted(topSF, key=lambda pair: pair[1], reverse=True)
    for item in sortedSF:
        print item[0]

    sortedNY = sorted(topNY, key=lambda pair: pair[1], reverse=True)
    for item in sortedNY:
        print item[0]


dataSet, classVec = loadDtaSet()
myVocabList = createVocabList(dataSet)
trainMat = []
for doc in dataSet:
    trainMat.append(setOfWords2Vec(myVocabList, doc))
p0V, p1V, pAb = trainNB0(trainMat, classVec)

# print p0V #,p1V, pAb
# testingNB()
spamTest()
# print setOfWords2Vec(createVocabList(dataSet), dataSet[0])
# ny = feedparser.parse('http://newyork.craigslist.org/stp/index.rss')
# sf = feedparser.parse('http://sfbay.craigslist.org/stp/index.rss')
# vocabList, pSF, pNY = localWords(ny, sf)
# getTopWords(ny, sf)

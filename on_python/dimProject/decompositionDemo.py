#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Dec  5 15:15:56 2019

@author: jiongwu

当特征选择完成后，可以直接训练模型了，但是可能由于特征矩阵过大，导致计算量大，训练时间长的问题，
因此降低特征矩阵维度也是必不可少的。
常见的降维方法除了以上提到的基于L1惩罚项的模型以外，
另外还有主成分分析法（PCA）和线性判别分析（LDA），线性判别分析本身也是一个分类模型。
PCA和LDA有很多的相似点，其本质是要将原始的样本映射到维度更低的样本空间中，
但是PCA和LDA的映射目标不一样：
PCA是为了让映射后的样本具有最大的发散性；
而LDA是为了让映射后的样本有最好的分类性能。
所以说PCA是一种无监督的降维方法，而LDA是一种有监督的降维方法。

"""

from sklearn import datasets
from sklearn.decomposition import PCA
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis  # LDA

# load_iris dataset
iris = datasets.load_iris()
X = iris.data
y = iris.target

# =============================================================================
# 主成分分析法（PCA）
# =============================================================================
# 主成分分析法，返回降维后的数据
# 参数n_components为主成分数目
X_pca = PCA(n_components=2).fit_transform(X,y)


# =============================================================================
# 线性判别式分析法（LDA）
# =============================================================================
# 线性判别分析法，返回降维后的数据
# 参数n_components为降维后的维数
X_lda = LinearDiscriminantAnalysis(n_components=2).fit_transform(X,y)


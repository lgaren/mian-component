#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/18

machine-learning-test

@author: DSG
"""

from numpy import *

# 生成一个随机对的数组
arr = random.rand(4, 4)
print (arr)
# 数组转化为矩阵
ranMat = mat(arr)
print (ranMat)
# 矩阵求逆
invRandMat = ranMat.I
print (invRandMat)

# 矩阵想乘
print (ranMat * invRandMat)

# 获取对角矩阵
print (eye(4))

#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/18

machine-learning-test

@author: DSG
"""
import KNN
from numpy import *

group = array([[1.0, 1.1], [1.0, 1.0], [0, 0], [0, 0.1]])
labels = ['A', 'A', 'B', 'B']

KNN.classify0([2, 2], group, labels, 3)  # B
result = KNN.classify0([2, 2], group, labels, 4)  # A
print (result)

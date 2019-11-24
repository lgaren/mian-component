#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2018/7/14  Sat PM 22:51 

mian-component 

@author: yuanyuan.liu@dsglyy.com

Version:  matrix V 0.0, Jul 14, 2018 DSG Exp$$

"""
from  numpy import *

dataMat = mat([[1,2],
                 [2,2],
                 [1,2],
                 [1,2],
                 [2,2]])

# <class 'numpy.matrixlib.defmatrix.matrix'>  *  <class 'numpy.matrixlib.defmatrix.matrix'>
# <class 'numpy.matrixlib.defmatrix.matrix'>
# <type 'numpy.ndarray'>

adc=mat(ones((5,1))/5).T

asd=ones((5,1))

print (max(e - 16))


print (adc *asd)
# print type(adc)0
# print type(ones((5,1)))
# print type(adc * asd)
if matrix([[2]]) >= 3.0 :
    print("sdfsggs")

#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/31

machine-learning-course

@author: DSG
"""


#
def f_prime(x_old):
    return -2 * x_old + 3


def cal():
    x_old = 0
    x_new = 6
    eps = 0.01
    presision = 0.00001
    while (x_new - x_old) > presision:
        x_old = x_new
        x_new = x_old + eps * f_prime(x_old)
        print x_old, x_new
    return x_new


print cal()

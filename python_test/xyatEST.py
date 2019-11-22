#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/8/9

machine-learning-course

@author: DSG
"""

import matplotlib
import numpy as np
import matplotlib.cm as cm
import matplotlib.mlab as mlab
import matplotlib.pyplot as plt


# delta = 0.025
# x = np.arange(-3.0, 3.0, delta)
# y = np.arange(-3.0, 3.0, delta)
# X, Y = np.meshgrid(x, y)
# Z1 = -(X**2)
# Z2 = -(Y**2)
# Z = 1.0 * (Z1 + 3 * Z2 + 2 * X * Y)+6.0
#
# plt.figure()
#
# CS = plt.contour(X, Y, Z)
#
# a = []
# b = []
#
# a.append(2.0)
# b.append(2.0)
#
# j = 1
#
# for i in xrange(200):
#     a_tmp = b[j-1]
#     a.append(a_tmp)
#     b.append(b[j-1])
#
#     j = j+1
#
#     b_tmp = a[j-1] / 3
#     a.append(a[j-1])
#     b.append(b_tmp)
#
# plt.plot(a,b)
#
# plt.title('Coordinate Ascent')
# plt.xlabel('x')
# plt.ylabel('y')
# plt.show()

def f(x):
    x_1 = x[0]
    x_2 = x[1]
    x_3 = x[2]

    result = -(x_1 * x_1) - 2 * (x_2 * x_2) - 3 * (x_3 * x_3) + 2 * x_1 * x_2 + 2 * x_1 * x_3 - 4 * x_2 * x_3 + 6

    return result


if __name__ == "__main__":
    # print "hello world"
    err = 1.0e-10
    x = [1.0, 1.0, 1.0]
    f_0 = f(x)
    while 1:
        # print "Hello"
        x[0] = x[1] + x[2]
        x[1] = x[0] / 2 - x[2]
        x[2] = x[0] / 3 - 2 * x[1] / 3
        print x
        f_t = f(x)

        if (abs(f_t - f_0) < err):
            break

        f_0 = f_t

    print "max: " + str(f_0)
    print x

#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/9/19

machine-learning-course

@author: DSG
"""

import matplotlib.pyplot as plt
from matplotlib import *
from numpy import *
from math import pi
from mpl_toolkits.mplot3d.axes3d import Axes3D
alpha = 0.7
phi_ext = 2 * pi * 0.5

def flux_qubit_potential(phi_m, phi_p):
    return 2 + alpha - 2 * cos(phi_p)*cos(phi_m) - alpha * cos(phi_ext - 2*phi_p)

phi_m = linspace(0, 2*pi, 100)
phi_p = linspace(0, 2*pi, 100)
X,Y = meshgrid(phi_p, phi_m)
Z = flux_qubit_potential(X, Y).T
fig = plt.figure(figsize=(14,6))

# `ax` is a 3D-aware axis instance because of the projection='3d' keyword argument to add_subplot
ax = fig.add_subplot(121, projection='3d')

p = ax.plot_surface(X, Y, Z, rstride=4, cstride=4, linewidth=0)

# surface_plot with color grading and color bar
ax = fig.add_subplot(1, 2, 2, projection='3d')
p = ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap='coolwarm', linewidth=0, antialiased=False)
cb = fig.colorbar(p, shrink=0.5)
# fig.show()
plt.show()

group_sort = dict(zip(range(5+1)[1:], [[]] * 5))
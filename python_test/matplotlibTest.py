#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/8/1

machine-learning-course

@author: DSG
"""

import numpy as np
import matplotlib.pyplot as plt
import mpl_toolkits.mplot3d
import scipy.io as sio

"""Accent, Accent_r, Blues, Blues_r, BrBG, BrBG_r, BuGn, BuGn_r, BuPu, BuPu_r, CMRmap, CMRmap_r,
    Dark2, Dark2_r, GnBu, GnBu_r, Greens, Greens_r, Greys, Greys_r, OrRd, OrRd_r, Oranges, Oranges_r,
    PRGn, PRGn_r, Paired, Paired_r, Pastel1, Pastel1_r, Pastel2, Pastel2_r, PiYG, PiYG_r, PuBu, PuBuGn,
    PuBuGn_r, PuBu_r, PuOr, PuOr_r, PuRd, PuRd_r, Purples, Purples_r, RdBu, RdBu_r, RdGy, RdGy_r, RdPu,
    RdPu_r, RdYlBu, RdYlBu_r, RdYlGn, RdYlGn_r, Reds, Reds_r, Set1, Set1_r, Set2, Set2_r, Set3, Set3_r,
    Spectral, Spectral_r, Vega10, Vega10_r, Vega20, Vega20_r, Vega20b, Vega20b_r, Vega20c, Vega20c_r,
    Wistia, Wistia_r, YlGn, YlGnBu, YlGnBu_r, YlGn_r, YlOrBr, YlOrBr_r, YlOrRd, YlOrRd_r, afmhot, afmhot_r,
    autumn, autumn_r, binary, binary_r, bone, bone_r, brg, brg_r, bwr, bwr_r, cool, cool_r, coolwarm,
    coolwarm_r, copper, copper_r, cubehelix, cubehelix_r, flag, flag_r, gist_earth, gist_earth_r, gist_gray,
    gist_gray_r, gist_heat, gist_heat_r, gist_ncar, gist_ncar_r, gist_rainbow, gist_rainbow_r,
    gist_stern, gist_stern_r, gist_yarg, gist_yarg_r, gnuplot, gnuplot2, gnuplot2_r, gnuplot_r, gray,
    gray_r, hot, hot_r, hsv, hsv_r, inferno, inferno_r, jet, jet_r, magma, magma_r, nipy_spectral,
    nipy_spectral_r, ocean, ocean_r, pink, pink_r, plasma, plasma_r, prism, prism_r, rainbow, rainbow_r,
    seismic, seismic_r, spectral, spectral_r, spring, spring_r, summer, summer_r, tab10, tab10_r,
    tab20, tab20_r, tab20b, tab20b_r, tab20c, tab20c_r, terrain, terrain_r, viridis, viridis_r, winter, winter_r
    """
# gcc-c++ gcc-gfortran.x86_64 gcc
# cpp.x86_64 0:4.8.5-16.el7                   gcc.x86_64 0:4.8.5-16.el7           gcc-gfortran.x86_64 0:4.8.5-16.el7        libgcc.x86_64 0:4.8.5-16.el7     libgfortran.x86_64 0:4.8.5-16.el7     libgomp.x86_64 0:4.8.5-16.el7     libquadmath.x86_64 0:4.8.5-16.el7
# libquadmath-devel.x86_64 0:4.8.5-16.el7     libstdc++.x86_64 0:4.8.5-16.el7     libstdc++-devel.x86_64 0:4.8.5-16.el7
# ../configure -Fa alg -fPIC -tl-Ss /root/tar/lapack-3.7.1/liblapack.a
# yum install atlas-devel
# yum tkinter tk-devel
# yum install gcc libffi-devel python-devel openssl-devel

def test3D():
    x, y = np.mgrid[-2:2:20j, -2:2:20j]
    z = x * np.exp(-x ** 2 - y ** 2)

    ax = plt.subplot(111, projection='3d')
    ax.plot_surface(x, y, z, rstride=2, cstride=1, cmap='spectral_r', alpha=1.0)

    ax.set_xlabel('x')
    ax.set_ylabel('y')
    ax.set_zlabel('z')

    plt.show()


def testPoint():
    mat1 = '../resource/4a.mat'  # 这是存放数据点的文件，需要它才可以画出来。上面有下载地址
    data = sio.loadmat(mat1)
    m = data['data']

    x, y, z = m[0], m[1], m[2]
    ax = plt.subplot(111, projection='3d')  # 创建一个三维的绘图工程

    # 将数据点分成三部分画，在颜色上有区分度
    ax.scatter(x[:1000], y[:1000], z[:1000], c='y')  # 绘制数据点
    ax.scatter(x[1000:4000], y[1000:4000], z[1000:4000], c='r')
    ax.scatter(x[4000:], y[4000:], z[4000:], c='g')

    ax.set_zlabel('Z')  # 坐标轴
    ax.set_ylabel('Y')
    ax.set_xlabel('X')
    plt.show()

test3D()
# testPoint()

#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2019/1/8  Tue AM 11:49

mian-component 

@author: yuanyuan.liu@dsglyy.com

Version:  functiongraph V 0.0, Jan 08, 2019 DSG Exp$$

"""

import numpy as np
import matplotlib.pyplot as plt
from RandomWalk import RandomWalk
<<<<<<< HEAD
import math
=======
>>>>>>> f515e2b1fd02cb3af610c9d249efc5732d1c37eb

"""
绘制函数图像
"""
# 下限只，上限值，步长
x = np.arange(-10, 10, 0.01)
y = np.sin(x)
y=np.log(x)
plt.plot(x, y)
plt.show()

"""
绘制点图
"""
#绘制散点图(传如一对x和y坐标，在指定位置绘制一个点)
plt.scatter(2,4)
#设置输出样式
plt.scatter(3,5,s=200)
plt.show()

# edgecolor='none', 删除点的轮廓
# c='red' 设置点颜色
# c=y_values,cmap=plt.cm.Blues,  渐变的颜色效果
#将c设置成一个y值列表，并使用参数cmap告诉pyplot使用那个颜色映射，这些代码将y值较小的点显示为浅蓝色，将y值较大的点显示为深蓝色


x_values = [1,2,3,4,5]
y_values = [1,4,9,16,25]
plt.scatter(x_values,y_values,edgecolor='none',c='red',s=100)
plt.show()

"""
自动函数计算
"""
x_values = np.arange(-50, 50, 0.001)
<<<<<<< HEAD
y_values = [ - 0.05 * x ** 2 + 10 for x in x_values]
plt.scatter(x_values,y_values,s=1 )
#设置每个坐标轴的取值范围（x轴取值，y轴取值）
plt.axis([0,30,0,30])
=======
y_values = [3 * (x*x) + x - 3 for x in x_values]
plt.scatter(x_values,y_values,s=0.01)
#设置每个坐标轴的取值范围（x轴取值，y轴取值）
plt.axis([-10,10,-10,10])
>>>>>>> f515e2b1fd02cb3af610c9d249efc5732d1c37eb
plt.show()


"""
保存图标，
参数1指定要以什么样的文件名保存图表，保存和代码的同目录下，第二个参数表示要将多余的空白区域剪掉，要保留空白区域，可省略第二个参数
"""
plt.savefig('squares_plot.png',bbox_inches='tight')
"""
隐藏坐标轴
"""
plt.axes().get_xaxis().set_visible(False)
plt.axes().get_yaxis().set_visible(False)
"""
设置绘图窗口的尺寸
figure()用于指定图表的宽度，高度，分辨率黑背景色figsize需要指定一个元组，单位英寸,dpi是分辨率，可传可不传
"""
plt.figure(dpi=128,figsize=(10,6))



#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2019/1/8  Tue PM 15:25

mian-component 

@author: yuanyuan.liu@dsglyy.com

Version:  RandomWalk V 0.0, Jan 08, 2019 DSG Exp$$

"""

from random import choice
import matplotlib.pyplot as plt

class RandomWalk(object):
    """一个生成随机漫步数据的类"""
    def __init__(self, num_points = 5000):
        """初始化随机漫步的属性"""
        #存储随机漫步次数的变量
        self.num_points = num_points
        #所有随机漫步都始于(0,0)
        #分别存储随机漫步经过的每个点的x和y坐标
        self.x_values = [0]
        self.y_values = [0]

    def fill_walk(self):
        """计算随机漫步包含的所有点"""

        #不断漫步，直到列表达到指定的长度
        while len(self.x_values) < self.num_points:
            #决定前进方向以及沿这个方向前进的距离, 方向为 +1 或者 —1
            x_direction = choice([1,-1])
            # 距离
            x_distance = choice([0,1,2,3,4])
            x_step = x_direction * x_distance

            y_direction = choice([1,-1])
            y_distance = choice([0,1,2,3,4])
            y_step = y_direction * y_distance

            #拒绝原地踏步
            if x_step == 0 and y_step == 0:
                continue

            #计算下一个点的x值和y值
            next_x = self.x_values[-1] + x_step
            next_y = self.y_values[-1] +y_step

            self.x_values.append(next_x)
            self.y_values.append(next_y)
        pass

if __name__ == '__main__':
    while True:
        #创建一个RandomWalk实例，并将其包含的点都绘制出来
        rw = RandomWalk()
        rw.fill_walk()

        point_numbers = list(range(rw.num_points))

        plt.scatter(rw.x_values,rw.y_values,c=point_numbers,cmap=plt.cm.Blues,edgecolor='none',s=15)
        plt.show()

        keep_running = input("Make another walk?(y/n)")
        if keep_running=='n':
            break

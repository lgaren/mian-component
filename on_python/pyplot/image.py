#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""

Created on 2019/1/8  Tue PM 17:27

mian-component 

@author: yuanyuan.liu@dsglyy.com

Version:  image V 0.0, Jan 08, 2019 DSG Exp$$

"""

import matplotlib.pyplot as plt
import matplotlib.image as mpimg

img=mpimg.imread('stinkbug.png')
imgplot = plt.imshow(img)

lum_img = img[:,:,0]
plt.imshow(lum_img)
plt.imshow(lum_img, cmap="hot")
imgplot.set_cmap('nipy_spectral')
plt.show()

print(img)

print(type(img))
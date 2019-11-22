#!/usr/bin/env python
# -*- coding: utf-8 -*-
# encoding:utf-8

"""
Created on 2017/7/23

machine-learning-test

@author: DSG
"""

import os
import re
import pip;
from time import sleep
# from tqdm import tqdm
import urllib2
import os

print(pip.pep425tags.get_supported())


def down_file():
    url = "http://repo.dsglyy.com/numpy-1.13.1%2Bmkl-cp36-cp36m-win_amd64.whl.bak"

    file_name = url.split('/')[-1]
    u = urllib2.urlopen(url)
    f = open(file_name, 'wb')
    meta = u.info()
    file_size = int(meta.getheaders("Content-Length")[0])

    file_size_dl = 0
    block_sz = 8192
    while True:
        buffer = u.read(block_sz)
        if not buffer:
            break

        file_size_dl += len(buffer)
        f.write(buffer)
    f.close()
    # down_file()
    # urllib2.urlretrieve(url, [filename=None, [reporthook=None, [data=None]]])
    # a = os.popen("wmic cpu")
    # b = a.read()
    #
    #
    #
    # print re.split( '\s*',b.split("\n")[1])
    # print re.split( '\s*',b.split("\n")[0])

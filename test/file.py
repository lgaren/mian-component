#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/11/152017 十一月 星期三下午 18:45

mian-component

@author: DSG
"""

file = open("load_employees.dump")
lines = file.readlines()
new = open("new","w")
for line in lines :
   line =  line.replace("'","").replace("(","").replace(")","")[:-2] + '\n'
   new.write(line)
file.close()
new.flush()
new.close()




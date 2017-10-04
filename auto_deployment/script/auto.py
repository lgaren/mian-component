#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Created on 2017/7/23

machine-learning-test

@author: DSG
"""
import os
import traceback

names = """adodbapi   alabaster   asn1crypto   astroid\
 astropy   babel   bitarray   blaze   bleach   bokeh   boto   bottleneck\
 bs4   cffi   chardet   click   cloudpickle   clyent   colorama   comtypes   contextlib2\
 Crypto   cryptography   cycler   Cython   cytoolz   dask   datashape   decorator\
 distributed   entrypoints   et_xmlfile   fastcache   flask   flask_cors   gevent   greenlet   h5py   heapdict\
 html5lib   idna   imagesize\
 isort   itsdangerous   jdcal   jedi   jinja2   jsonschema\
 lazy_object_proxy   llvmlite   locket   lxml   markupsafe   matplotlib   mistune\
 mpmath   multipledispatch   nbconvert   nbformat   networkx   nltk   nose   notebook\
 numba   numexpr   odo   olefile  openpyxl   packaging   pandas   partd\
 pathlib2   patsy   pep8   pickleshare   ply   prompt_toolkit   psutil   pycosat   pycparser\
 pycurl   pyflakes   pygments  pylint   pyodbc   pyparsing   pytest   pytz\
 qtawesome   qtconsole   qtpy   requests\
 rope   ruamel_yaml   run   scripts   seaborn   simplegeneric   singledispatch\
 six   sklearn   snowballstemmer   sortedcollections\
 sortedcontainers   Sphinx\
 sqlalchemy   statsmodels   sympy   tables   tblib   testpath   toolz\
 tornado   traitlets   unicodecsv   wcwidth   werkzeug   wheel   widgetsnbextension\
 win_unicode_console   wrapt   xlrd   xlsxwriter   xlwings   xlwt   zict   zmq"""

# backports.weakref: 1.0rc1-py36_0
# bleach:            1.5.0-py36_0
# html5lib:          0.9999999-py36_0
# libprotobuf:       3.2.0-0
# markdown:          2.6.8-py36_0
# protobuf:          3.2.0-py36_0
# tensorflow:        1.2.1-py36_0
# skimage
#  ls | while read LINE ;do ln -s /usr/share/java-1.8.0/bin/$LINE /usr/bin/ ;done
# yum tkinter tk-devel
# jupyter   jupyter_client   jupyter_console   jupyter_core
# yum install unixODBC Freetds mysql-connector-odbc
# xlwings only windows
list = names.split("   ")
# out = open('C:\\Users\\1\\Desktop\\output', 'a')
for name in list:
    try:
        # out.write(
            # '###########################################################\n' + name + '\n==================================\n\n\n')
        outs = os.system("python -m pip install " + name)
    except:
        pass
        # out.write(traceback.format_exc())
        # out.write(name)
# out.close()
import os
import traceback

names = """adodbapi   alabaster   anaconda_navigator   anaconda_project   asn1crypto   astroid\
 astropy   babel   backports   binstar_client   bitarray   blaze   bleach   bokeh   boto   bottleneck\
 bs4   cffi   chardet   click   cloudpickle   clyent   colorama   comtypes   conda   conda_env   contextlib2\
 Crypto   cryptography   curl   cycler   Cython   cython   cytoolz   dask   datashape   dateutil   decorator\
 distributed   entrypoints   et_xmlfile   fastcache   flask   flask_cors   gevent   greenlet   h5py   heapdict\
 html5lib   idna   imagesize   ipykernel   ipykernel_launcher   IPython   ipython_genutils   ipywidgets   isapi\
 isort   itsdangerous   jdcal   jedi   jinja2   jsonschema   jupyter   jupyter_client   jupyter_console   jupyter_core\
 lazy_object_proxy   llvmlite   locket   lxml   markupsafe   matplotlib   menuinst   mistune   mkl   mpl_toolkits\
 mpmath   msgpack   multipledispatch   navigator_updater   nbconvert   nbformat   networkx   nltk   nose   notebook\
 numba   numexpr   numpy   odo   olefile   OleFileIO_PL   openpyxl   OpenSSL   packaging   pandas   partd   path\
 pathlib2   patsy   pep8   pickleshare   PIL   ply   prompt_toolkit   psutil   py   pycosat   pycparser\
 pycurl   pyflakes   pygments   pylab   pylint   pyodbc   pyparsing   PyQt5   pytest   pythoncom   pythonwin   pytz\
 PyWin32   pywin32   pywin32   pywin32_system32   pywt   pyximport   qtawesome   qtconsole   qtpy   README   requests\
 rope   ruamel_yaml   run   scipy   scripts   seaborn   setuptools-27   setuptools   simplegeneric   singledispatch\
 singledispatch_helpers   sip   sip   sipconfig   sipdistutils   six   skimage   sklearn   snowballstemmer   sortedcollections\
 sortedcontainers   Sphinx-1   Sphinx   spyder   spyder_breakpoints   spyder_io_dcm   spyder_io_hdf5   spyder_profiler\
 spyder_pylint   sqlalchemy   statsmodels   sympy   tables   tblib   testpath   test_path   test_pycosat   tlz   toolz\
 tornado   traitlets   unicodecsv   wcwidth   werkzeug   wheel   widgetsnbextension   win32   win32com   win32comext\
 win_unicode_console   wrapt   xlrd   xlsxwriter   xlwings   xlwt   yaml   yaml   zict   zmq"""

list = names.split("   ")
out = open('C:\\Users\\admin\\Desktop\\output', 'a')
for name in list:
    try:
        out.write(
            '###########################################################\n' + name + '\n==================================\n\n\n')
        os.system("python -m pip install " + name)
    except:
        out.write(traceback.format_exc())
        out.write(str(e.message))
        out.write(name)

out.close()

#!/usr/bin/env python

import re
str1 ="""
##---**---^^---$$---&&---@@This is the automatically generated KYLIN configuration---%%---!!---~~
listen kylin_build_server
bind *:18080
mode http
balance roundrobin
server kylin_bulid_web slaves3.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3

listen kylin_query_server
bind *:18082
mode http
balance roundrobin
server kylin_query_web1 slaves5.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3
server kylin_query_web2 slaves4.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3
server kylin_query_web3 slaves3.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 2000

##---**---^^---$$---&&---@@This is the automatically generated KYLIN configuration---%%---!!---~~
"""
str2 = "\n##---**---^^---$$---&&---@@This is the automatically generated KYLIN configuration---%%---!!---~~\n"
# re0 = '\s+'
# re1 = '#{2}'
# re2 = '(?:-{3}[*$&@^]{2})+'
# re3 = 'This is the automatically generated KYLIN configuration'
# re5 = 'kylin_build_server'
# re4 = 'listen'
# re6 = 'bind \*:\d+'
re7 = '\nmode http\nbalance roundrobin\n'
# re8 = '(?:-{3}[%!~]{2})+'
# # re9 = 'server' + re0 + 'kylin_bulid_web' + re0 + '\S+:\d+/kylin\scookie' + re0 + '\d\sweight' + re0 + '\d' + re0 + 'check' + re0 + 'inter' + re0 + '\d+\srise' + re0 + '\d' + re0 + 'fall' + re0 + '\d'
# re9 = 'server\skylin_bulid_web\s\S+:\d+/kylin\scookie\s\d\sweight\s\d\scheck\sinter\s\d+\srise\s\d\sfall\s\d'
# re10 = 'kylin_query_server'
# # re11 = '(?:server' + re0 + 'kylin_query_web\d+' + re0 + '\S+:\d+/kylin' + re0 + 'cookie' + re0 + '\d' + re0 + 'weight' + re0 + '\d' + re0 + 'check' + re0 + 'inter' + re0 + '\d+' + re0 + 'rise' + re0 + '\d' + re0 + 'fall' + re0 + '\d\n?)*'
# # re11 = '(?:server\s+kylin_query_web\d+\s+\S+:\d+/kylin\s+cookie\s+\d\s+weight\s+\d\s+check\s+inter\s+\d+\s+rise\s+\d+\s+fall\s+\d+\n?)*'
# re11 = '(?:server\skylin_query_web\d+\s\S+:\d+/kylin\scookie\s\d\sweight\s\d\scheck\sinter\s\d+\srise\s\d\sfall\s\d)+'
# # par = re.compile(re1 + re2 + re3 + re8 + re0 + re4 + re0 + re5 + re0  + re6+ re0 +re7 + re0 + re9 + re0 + re4+ re0  + re10 + re0 + re6+ re0 + re7+ re0  + re11 + re1+ re2 + re3 + re8)
#
#
# print re.findall(par,str)
# print par.pattern

# print re11
# master3.istuary.com master3
# 172.21.9.105  slaves5.istuary.com slaves5
# 172.21.9.90  master1.istuary.com master1
mode = '\nmode http\nbalance roundrobin\n'
kylin_lb_build_port = 'master3.istuary.com'
query_hosts = ['slaves5.istuary.com','master1.istuary.com']
lbConf   = str2
lbConf  += "listen kylin_build_server \n"
lbConf  +=  "bind *:" + "10800"
lbConf  +=  mode
lbConf  += format("server kylin_bulid_web {kylin_build_host}:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3\n")
lbConf  += "listen kylin_query_server\n"
lbConf  += "bind *:" + '180801'

lbConf  +=  mode
i = 1
for host in query_hosts :
    lbConf += "server kylin_query_web" + str(i)
    lbConf += format(" slaves3.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3\n")
    i += 1
lbConf += "server  kylin_query_web" + str(i)
lbConf += format("  slaves3.istuary.com:7070/kylin cookie 1 weight 5 check inter 2000 rise 2 fall 3\n")
lbConf  += str2


re_0 = '\s*'
re_1 = '[a-zA-Z\s]+'
re_2 =  re_0 + '\d+' + re_0
re_3  = '(?:'+ re_1 + re_2 +')*'

re1 = '#{2}'
re2 = '(?:-{3}[*$&@^]{2})+'
re3 = 'This is the automatically generated KYLIN configuration'
re5 = 'kylin_build_server'
re4 = 'listen'
re6 = 'bind' + re_0 + '\*:\d+'
re7 = 'mode' + re_0 + re_1 + re_0 + 'balance' + re_0 + re_1
re8 = '(?:-{3}[%!~]{2})+'

re9 = 'server' + re_0 + 'kylin_bulid_web' + re_0 + '\S+:\d+/kylin' + re_0 + re_3
re10 = 'kylin_query_server'
re11 = '(?:server' + re_0 + 'kylin_query_web\d+' + re_0 + '\S+:\d+/kylin' + re_0 + re_3 + ')*'

par = re.compile(re1 + re2 + re3 + re8 + re_0 + re4 + re_0 + re5 + re_0  + re6+ re_0 +re7
                 + re_0 + re9 + re4+ re_0  + re10 + re_0 + re6+ re_0 + re7+ re_0  + re11 + re_0+ '\s' + re1+ re2 + re3 + re8)
print (par.pattern)
kylinLB = re.findall(par,str1)
# print lbConf
print re.findall(par,str1)
if len(kylinLB) !=0 :
    print kylinLB[0].replace('\n','\n#')
else :
    print kylinLB


#!/bin/bash
#dir=$(cd `dirname $0`; pwd)
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)


DSG_HOME=${KYLIN_HOME:-"${dir}/../"}

conf=${DSG_HOME}/conf

lib=${DSG_HOME}/lib

for jar in $lib*;do  
    CLASSPATH=$jar:"$CLASSPATH"
done 
CLASSPATH=${conf}:$CLASSPATH
#用文件名天加文件只读取classpath以内的文件
#用addInputStream 可以之需要路径
java -classpath ${CLASSPATH}  -Dlog4j.configuration=file:$conf/log4j.properties com.test.LogTest
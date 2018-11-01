#!/bin/bash

JAR_DIR=/opt/jars

dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
export CEP_HOME=${CEP_HOME:-"${dir}/.."}
logs=${CEP_HOME}/logs
lib=${CEP_HOME}/jar
tmp=${CEP_HOME}/tmp
model=$8
log=$logs/${model}_log.log
pid_file=$tmp/${model}_pid.pid
source /etc/profile
driver_memory=4g
executor_memory=14g
num_executors=4
executor_cores=1
class=com.lvmama.kpi.stream.KpiCountRun
jar_file=stream-kpi-1.0-SNAPSHOT.jar
# export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/opt/cloudera/parcels/CDH-5.10.2-1.cdh5.10.2.p0.5/lib/hadoop/lib/native
#export HADOOP_USER_NAME=deploy_man
start(){
    code=0
    if [ -f $pid_file ];then
        APP_ID=`tail -1 $pid_file`
        echo "$APP_ID  already running"
        yarn application -status $APP_ID
    else
        if [ ! -d $tmp ];then
            mkdir $tmp
        fi
        echo "starting application please wait... "
        logtran
        nohup  spark2-submit \
            --driver-memory ${driver_memory} \
            --executor-memory ${executor_memory} \
            --num-executors ${num_executors} \
            --executor-cores ${executor_cores} \
            --master yarn \
            --deploy-mode cluster \
            --class ${class}  \
            --conf spark.executor.extraClassPath=${JAR_DIR}/* \
            --conf spark.driver.extraClassPath=${JAR_DIR}/* \
            ${lib}/${jar_file} $@  >> $log  2>&1  &
        echo $! >> $pid_file
        #echo "application start successful ! "
        start_time=0
        #set -x
        while true
          do
             start_info=`cat $log | grep 'INFO yarn.Client: Application report for application_' | tail -1`
             application_id=`echo $start_info | awk  '{print $8}'`
             start_status=`echo $start_info | awk  '{print $10}'`
             if [ "$application_id"x != ""x ] && [ "$start_status"x = "RUNNING)"x ];then
                 echo $application_id >> $pid_file
                 echo $start_info
                 echo "application start successful , application_id is $application_id"
                 break
             else
                 error=`cat $log | grep -E "Exception|exception|ERROR|error"`
                 if [ "$error"x != ""x ] || [ "$start_status"x = "KILLED)"x ] || [ "$start_status"x = "FAILED)"x ]  ;then
                     echo -e "application submitted fail ! application start fail ! will kill the job!"
                     echo $application_id >> $pid_file
                     echo $error
                     echo $start_info
                     stop
                     log $application_id
                     code=1
                     break
                 fi
             fi
             let start_time=$start_time+3
             sleep 3
             if [ $start_time -gt 60 ];then
                echo "application has been submitted  over $start_time  seconds ! "
                echo $start_info
             fi
        done
    fi
    return $code 
}

stop(){
   if [ -f $pid_file ];then
       echo "stopping application please wait... "
       sleep 3
       yarn application -kill `tail -1 $pid_file`
#       if [ $? -eq 0 ] ; then
           kill -9 `cat $pid_file | head -1`  >/dev/null  2>&1
           rm -f $pid_file
           echo "stop application successful"
           return 0
#       else
#           echo "application stop fail "
#           return 1
#       fi
   else 
       echo "application not running"  
       return 0
   fi 
}

status(){
   if [ -f $pid_file ];then
       tail -50 $log
       yarn application -status `tail -1 $pid_file`
   else
       echo "application not running"  
   fi
}

restart(){
   stop
   if [ $? -eq 0 ] ; then 
        start $@
   else
        echo "application stop fail, skip start command "
   fi 
}

log(){
    echo "the job log is : "
    sleep 2
    if [ -f $pid_file ];then 
        yarn logs -applicationId `tail -1 $pid_file`
    else 
        yarn logs -applicationId $1
    fi
}

logtran(){
    if [ -f $log ] ; then 
        logtar=$logs/backlog.tar.gz
        back_log=${log}.`date "+%Y-%m-%d.%H:%M:%S"`
        cat $log  >> $back_log
        echo "" > $log
        if [ ! -f $logtar ];then
            mkdir $logs/backlog
        else
            tar zxPf $logtar
            rm -rf $logtar 
        fi
        mv $back_log  $logs/backlog
        tar zcPf $logtar $logs/backlog  --remove-files
        filesize=`du -m $logtar | awk '{print $1}'`
        if [ $filesize -ge 100 ] ; then 
            mv $logtar ${logtar}.`date "+%Y-%m-%d"` 
        fi
    fi
}

help(){
    echo -e "Usage: \n    $0  < start | stop | status | restart | help | log | logtran > "
}

if [ ! -d $tmp ];then
     mkdir $tmp
fi

if [ $#  -lt 2 ] ; then help ; exit 1;fi  
command=$1
driver_memory=$2
executor_memory=$3
num_executors=$4
executor_cores=$5
jar_file=$6
class=$7
shift 7
$command $@  
exit $?
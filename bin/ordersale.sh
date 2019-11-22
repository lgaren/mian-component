#!/bin/bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
common=$1
shift 1
#  start driver_memory executor_memory num_executors executor_cores
. $dir/applicationopt.sh $common 4g 14g 4 1 stream-kpi-1.0-SNAPSHOT.jar com.lvmama.kpi.stream.KpiCountRun  order $@
#!/bin/bash
dir=$(cd -P -- "$(dirname -- "$0")" && pwd -P)
. $dir/applicationopt.sh logtran 0 0 0 0 0 0 order
. $dir/applicationopt.sh logtran 0 0 0 0 0 0 userOnline
. $dir/applicationopt.sh logtran 0 0 0 0 0 0 syncToDb
. $dir/applicationopt.sh logtran 0 0 0 0 0 0 userRegister
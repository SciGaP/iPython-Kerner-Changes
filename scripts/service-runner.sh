#!/bin/bash

LOG_FILE="service.log"
TRACE_PID_FILE="TRACE_PID"

python3 tools/tracing-server/strace_server.py &> $LOG_FILE &
echo $! > $TRACE_PID_FILE
jupyter notebook --ip 0.0.0.0 --allow-root

if [ -f $TRACE_PID_FILE ]; then
  PID=$(cat $TRACE_PID_FILE)
  echo "Killing tracing process $PID"
  kill $PID;
fi
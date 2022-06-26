#!/bin/bash

LOG_FILE="service.log"
TRACE_PID_FILE="TRACE_PID"

FILE=/opt/ARCHIVE.zip
if [ -f "$FILE" ]; then
    echo "$FILE exists. And loading the archive"
    unzip ARCHIVE.zip -d ARCHIVE
    python3 scripts/initialize.py
fi


python3 tools/tracing-server/strace_server.py &> $LOG_FILE &
echo $! > $TRACE_PID_FILE
cd Notebooks
jupyter notebook --ip 0.0.0.0 --allow-root --NotebookApp.token=$NOTEBOOK_TOKEN

if [ -f $TRACE_PID_FILE ]; then
  PID=$(cat $TRACE_PID_FILE)
  echo "Killing tracing process $PID"
  kill $PID;
fi

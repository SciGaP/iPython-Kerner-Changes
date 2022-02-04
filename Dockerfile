FROM ubuntu:20.04

USER root

RUN apt update
RUN apt install -y strace
RUN apt install -y python3
RUN apt install -y python3-pip

RUN mkdir -p /opt
COPY . /opt
RUN pip install wheel
RUN cd /opt/ipykernel; pip install .
RUN cd /opt/tools/state_capture_magic; pip install .
RUN pip install notebook
RUN jupyter kernelspec install  --user /opt/ipykernel/

RUN chmod 777 /opt/scripts/service-runner.sh

WORKDIR /opt
EXPOSE 8888
ENTRYPOINT ["/opt/scripts/service-runner.sh"]

FROM ubuntu:20.04

USER root

RUN apt update
RUN apt install -y strace
RUN apt install -y python3
RUN apt install -y python3-pip

RUN mkdir -p /opt
COPY ipykernel /opt/ipykernel

RUN pip install wheel
RUN pip install dill
RUN pip install notebook
RUN pip install requests

RUN cd /opt/ipykernel; pip install .

COPY tools /opt/tools
COPY ExampleNotebooks /opt/ExampleNotebooks
RUN cd /opt/tools/state_capture_magic; pip install .
RUN jupyter kernelspec install  --user /opt/ipykernel/

COPY scripts /opt/scripts
RUN chmod 777 /opt/scripts/service-runner.sh

WORKDIR /opt
EXPOSE 8888
ENTRYPOINT ["/opt/scripts/service-runner.sh"]

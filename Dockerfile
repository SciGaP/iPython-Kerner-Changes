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
RUN pip install torch
RUN pip install torchvision
RUN pip install ipywidgets

RUN apt install -y wget
RUN apt install -y unzip

ENV DEBIAN_FRONTEND noninteractive
RUN apt update && apt-get install -y --no-install-recommends  --fix-missing ffmpeg libsm6 libxext6

RUN cd /opt/ipykernel; pip install .

COPY tools /opt/tools
COPY ExampleNotebooks /opt/Notebooks
RUN cd /opt/tools/state_capture_magic; pip install .
RUN jupyter kernelspec install  --user /opt/ipykernel/


#RUN pip3 install jupytemplate
#COPY ExampleNotebooks/Example.ipynb /usr/local/lib/python3.8/dist-packages/jupytemplate/jupytemplate/template.ipynb
#RUN jupyter nbextension install --py jupytemplate --sys-prefix
#RUN jupyter nbextension enable jupytemplate/main --sys-prefix

COPY scripts /opt/scripts
COPY scripts/start.py /root/.ipython/profile_default/startup/start.py
RUN chmod 777 /opt/scripts/service-runner.sh

WORKDIR /opt
EXPOSE 8888
ENTRYPOINT ["/opt/scripts/service-runner.sh"]

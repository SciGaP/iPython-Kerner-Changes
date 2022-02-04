# iPython-Kerner-Changes

### Docker Instructions (Recommended)

1. `docker build -t dimuthuupe/ipykernel:1.0 .`
2. `docker run --cap-add=SYS_PTRACE -it -p 8888:8888  dimuthuupe/ipykernel:1.0`

### Local Installation Instructions
##### Create the virtual environment

1. `python3 -m venv ENV`
2. `source ENV/bin/activate`

##### Build the Kernel and Run Notebook Server
1. `pip install wheel`
2. `pip install ipykernel/`
3. `pip install tools/state_capture_magic`
4. `jupyter kernelspec install  --user /home/dimuthu/code/iPython-Kerner-Changes/ipykernel/`
5. `pip install notebook`
6. `jupyter notebook --ip 0.0.0.0 `

##### Start Tracing Server

1. `sudo python3 tools/tracing-server/strace_server.py`
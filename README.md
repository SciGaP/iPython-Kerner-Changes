# iPython-Kerner-Changes

<!---
1. Include Java Version
2. Instructions to set it up
-->

### Docker Instructions (Recommended)

1. `docker build -t dimuthuupe/ipykernel:1.0 .`
2. Start a fresh session :`docker run --cap-add=SYS_PTRACE -it -p 8888:8888  dimuthuupe/ipykernel:1.0`
2. Load from an exported session :`docker run --cap-add=SYS_PTRACE -it -p 8888:8888 -v <ARCHIVE_FILE>:/opt/ARCHIVE.zip  dimuthuupe/ipykernel:1.0`

### Magic Line Commonads to run in the Notebook

1. `%load_ext StateCaptureMagic` : Loads the magic extension
2. `%load_local_context`: Loads python runtime if an exported session archive is mounted
3. `%export_states createArchive=True` Exports the current session and provides a link to download it

### Local Installation Instructions (For Developers)
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

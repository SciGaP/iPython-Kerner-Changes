from __future__ import print_function
from IPython.core.magic import (Magics, magics_class, line_magic,
                                cell_magic, line_cell_magic)
from IPython.core.magic import needs_local_scope
import os
import types
from importlib.metadata import PackageNotFoundError
from importlib.metadata import version
import sys
import string
import random
import shutil
import json
from contextlib import redirect_stdout
import dill as pickle
import io
from IPython.display import FileLink
from pathlib import Path
import shutil
from os.path import exists

import re
import ipykernel
from notebook.notebookapp import list_running_servers
import requests
from requests.compat import urljoin
import json
import os.path
from os.path import exists
import socket
import uuid
import subprocess

def load_ipython_extension(ipython):
    ipython.register_magics(StateCaptureMagic)

@magics_class
class StateCaptureMagic(Magics):

    @line_magic
    @needs_local_scope
    def print_local_context(self, line, cell="", local_ns=None):
        print(local_ns.keys())


    @line_magic
    @needs_local_scope
    def load_local_context(self, line, cell="", local_ns=None):

        context_file = "/opt/ARCHIVE/context.p"
        self.load_context_from_file(context_file, local_ns)

    def load_context_from_file(self, context_file, local_ns):
        if exists(context_file):

            print("local context before")
            print(local_ns.keys())

            with open(context_file, "rb") as input_file:
                final_scope  = pickle.load(input_file)

                print("remote context")
                print(final_scope.keys())

            for var in final_scope:
                local_ns[var] = final_scope[var]

            print("local context after")
            print(local_ns.keys())

            print("Successfully loaded run time context from " + context_file)
        else:
            print("No archive is loaded or context is not exported to the archive")


    def control_tracing(self, turn_on):
        pid = os.getpid()

        sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        server_address = '/tmp/uds_socket'

        try:
            sock.connect(server_address)
        except socket.error as msg:
            sys.exit(1)

        try:
            if turn_on:
                # print("Turning on tracing")
                message = "START:" + str(pid)
                sock.sendall(str.encode(message))
            else:
                # print("Turning off tracing")
                message = "STOP:" + str(pid)
                sock.sendall(str.encode(message))
        finally:
            sock.close()

    @cell_magic
    @needs_local_scope
    def run_hpc(self, line, cell, local_ns=None):
        
        
        try:
            print("Session ID "  + self.session_id)
        except AttributeError:
            self.session_id = str(uuid.uuid4())
            print("Session ID "  + self.session_id)
            
        self.control_tracing(False)

        parameters = line.split(",")

        uploadServer = None
        archiveName = "HPC Export"
        compute_id = None
        ignoredDeps = {"ipykernel", "ipython", "ipython-genutils", "ipywidgets",
                       "jupyter-client", "jupyter-core", "jupyterlab-pygments", "jupyterlab-widgets", "nbclient",
                       "nbconvert", "nbformat", "notebook", "state-capture-magic", "pip", "pycparser", "Pygments",
                       "pyparsing", "pyrsistent", "python-dateutil", "setuptools", "six", "soupsieve", "stack-data",
                       "terminado", "terminado", "dill", "debugpy", "executing","idna", "fastjsonschema", "jsonschema",
                       "MarkupSafe", "argon2-cffi", "typing-extensions", "urllib3", "wcwidth", "webencodings", "wheel",
                       "widgetsnbextension", "zipp", "beautifulsoup4", "bleach", "certifi", "cffi", "charset-normalizer",
                       "argon2-cffi-bindings", "asttokens", "attrs", "backcall", "contourpy", "cycler", "decorator", "defusedxml",
                       "entrypoints", "fonttools", "importlib-resources", "jedi", "Jinja2", "kiwisolver", "mistune", "nest-asyncio",
                       "packaging", "pandocfilters", "parso", "pexpect", "pickleshare", "Pillow", "prometheus-client", "prompt-toolkit",
                       "psutil", "ptyprocess", "pure-eval", "pyzmq", "requests", "Send2Trash", "tinycss2", "tornado", "traitlets"}

        for param in parameters:
            param = param.strip()

            if param.startswith("uploadServer"):
                uploadServer = param.split("=")[1]
            
            if param.startswith("computeResourceId"):
                compute_id = param.split("=")[1]

            if param.startswith("ignoredDependencies"):
                depListStr = param.split("=")[1]
                depsList = depListStr.split(";")
                for dep in depsList:
                    ignoredDeps.add(dep)

        accessed_files = self.get_accessed_files(ignoreAbsPaths=True)
        dependencies = self.get_dependencies(local_ns, ignoredDeps)
        archive_dir = "ARCHIVE"

        dirpath = Path(archive_dir)
        if dirpath.exists() and dirpath.is_dir():
            shutil.rmtree(dirpath)

        os.mkdir(archive_dir)

        archive_dict = {}
        for f in accessed_files:
            if not f == "" and exists(f):
                letters = string.ascii_lowercase
                random_name = ''.join(random.choice(letters) for i in range(10));
                archive_dict[random_name] = f
                shutil.copyfile(f, archive_dir + "/" + random_name)

        f = open(archive_dir + "/files.json", "w")
        json.dump(archive_dict, f)
        f.close()

        f = open(archive_dir + "/dependencies.json", "w")
        json.dump(dependencies, f)
        f.close()

        var_context = self.get_local_context(local_ns)

        pickle.dump( var_context, open( archive_dir + "/context.p", "wb" ))

        f = open(archive_dir + "/code.txt", "w")
        f.write(cell)
        f.close()

        shutil.make_archive("ARCHIVE", 'zip', archive_dir)
        # print("Download the state export ")
        # display(FileLink("ARCHIVE.zip"))

        if uploadServer:
            archiveId = self.upload_and_register_archive_in_server(uploadServer, archiveName)
            if archiveId:
                # print("Uploaded to archive " + archiveId)
                run_url = uploadServer + "/remote/run/" + compute_id + "/" +  archiveId + "/" + self.session_id
                # print("Executing run url " + run_url)
                response = requests.get(run_url)
                if response.status_code == 200:

                    job_id = response.json()['jobId']
                    # print ("Received job id " + job_id)

                    monitor_url = uploadServer + "/job/status/" + job_id
                    response = requests.get(monitor_url)
                    if response.status_code == 200:
                        job_state = response.json()["state"]
                        # print("Job state is " + job_state)
                        if job_state == "COMPLETED":
                            state_download_url = uploadServer + "/archive/download/" + job_id
                            downloaded_zip = self.download_file_from_url(state_download_url)
                            if downloaded_zip:
                                shutil.unpack_archive(downloaded_zip, "LATEST_STATE")
                                self.load_context_from_file("LATEST_STATE/final-context.p", local_ns)
                                with open('LATEST_STATE/stdout.txt', 'r') as f:
                                    print(f.read())

                                with open('LATEST_STATE/stderr.txt', 'r') as f:
                                    print(f.read())
                            else:
                                print("Downloading state archive from remote failed")
                    else:
                        print("Failed while monitoring job " + job_id)
                else:
                    print("Cell execution submission failed")

            else:
                print("Upload to server failed")

        self.control_tracing(True)

    def download_file_from_url(self, download_url):
        resp = requests.get(download_url, allow_redirects=True)
        disp_header = resp.headers.get('content-disposition')
        if disp_header :
            file_name = re.findall('filename=(.+)', disp_header)
            if len(file_name) == 0:
                return None

            open(file_name[0], 'wb').write(resp.content)
            print("File " + file_name[0] + " was downloaded")
            return file_name[0]
        else:
            print("Could not find the file name in download response")

    @line_magic
    @needs_local_scope
    def export_states(self, line, cell="", local_ns=None):

        self.control_tracing(False)

        parameters = line.split(",")

        createArchive = False
        uploadServer = None
        captureLocalContext = True
        archiveName = ""

        ignoredDeps = {"ipykernel", "ipython", "ipython-genutils", "ipywidgets",
                       "jupyter-client", "jupyter-core", "jupyterlab-pygments", "jupyterlab-widgets", "nbclient",
                       "nbconvert", "nbformat", "notebook", "state-capture-magic", "pip", "pycparser", "Pygments",
                       "pyparsing", "pyrsistent", "python-dateutil", "setuptools", "six", "soupsieve", "stack-data",
                       "terminado", "terminado", "dill", "debugpy", "executing","idna", "fastjsonschema", "jsonschema",
                       "MarkupSafe", "argon2-cffi", "typing-extensions", "urllib3", "wcwidth", "webencodings", "wheel",
                       "widgetsnbextension", "zipp", "beautifulsoup4", "bleach", "certifi", "cffi", "charset-normalizer",
                       "argon2-cffi-bindings", "asttokens", "attrs", "backcall", "contourpy", "cycler", "defusedxml",
                       "entrypoints", "fonttools", "importlib-resources", "jedi", "Jinja2", "kiwisolver", "mistune", "nest-asyncio",
                       "packaging", "pandocfilters", "parso", "pexpect", "pickleshare", "Pillow", "prometheus-client", "prompt-toolkit",
                       "psutil", "ptyprocess", "pure-eval", "pyzmq", "requests", "Send2Trash", "tinycss2", "tornado", "traitlets"}

        for param in parameters:
            param = param.strip()
            if param.startswith("createArchive"):
                flag = param.split("=")[1]
                if flag == "True":
                    createArchive = True

            if param.startswith("uploadServer"):
                uploadServer = param.split("=")[1]

            if param.startswith("archiveName"):
                archiveName = param.split("=")[1]

            if param.startswith("captureLocalCtx"):
                flag = param.split("=")[1]
                if flag == "True":
                    captureLocalContext = True

        notebook_name = self.get_notebook_name()

        accessed_files = self.get_accessed_files()
        accessed_files.add(notebook_name)
        dependencies = self.get_dependencies(local_ns, ignoredDeps)

        if createArchive:
            archive_dir = "ARCHIVE"

            dirpath = Path(archive_dir)
            if dirpath.exists() and dirpath.is_dir():
                shutil.rmtree(dirpath)

            os.mkdir(archive_dir)

            archive_dict = {}
            for f in accessed_files:
                if not f == "" and exists(f):
                    letters = string.ascii_lowercase
                    random_name = ''.join(random.choice(letters) for i in range(10));
                    archive_dict[random_name] = f
                    shutil.copyfile(f, archive_dir + "/" + random_name)

            f = open(archive_dir + "/files.json", "w")
            json.dump(archive_dict, f)
            f.close()

            f = open(archive_dir + "/dependencies.json", "w")
            json.dump(dependencies, f)
            f.close()

            metadata = {}
            metadata["workingDir"] = os.getcwd()
            f = open(archive_dir + "/metadata.json", "w")
            json.dump(metadata, f)
            f.close()

            if captureLocalContext:
                var_context = self.get_local_context(local_ns)
                print("local context")
                print(var_context.keys())

                pickle.dump( var_context, open( archive_dir + "/context.p", "wb" ))

            shutil.make_archive("ARCHIVE", 'zip', archive_dir)
            print("Download the state export ")
            display(FileLink("ARCHIVE.zip"))

            if uploadServer:
                self.upload_and_register_archive_in_server(uploadServer, archiveName)


        self.control_tracing(True)

        return {"accessed_files": list(accessed_files), "dependencies": dependencies}


    def upload_archive_to_server(self, base_url):
        headers={'Accept': 'application/json, text/plain, */*',
                 'Accept-Encoding': 'gzip, deflate, br',
                 'Accept-Language': 'en-US,en;q=0.9',
                 'Connection': 'keep-alive'}

        files = {'file': open('ARCHIVE.zip', 'rb')}
        response = requests.post(base_url + "/archive/upload", data={}, headers=headers, files=files)
        return response

    def upload_and_register_archive_in_server(self, base_url, archiveName):

        headers={'Accept': 'application/json, text/plain, */*',
        'Accept-Encoding': 'gzip, deflate, br',
        'Accept-Language': 'en-US,en;q=0.9',
        'Connection': 'keep-alive'}

        response = self.upload_archive_to_server(base_url)

        if response.status_code == 200:
            response_json = response.json()
            archive_json = {"path": response_json["path"],"description": archiveName}
            headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
            response = requests.post(base_url + '/archive/', data=json.dumps(archive_json), headers=headers)
            if response.status_code == 200:
                print("Archive with name " + archive_json["description"] + " was uploaded")
                return response.json()["id"]
            else:
                print("Failed to create archive metadata in server with status code " + str(response.status_code))
        else:
            print("Upload to server failed with code " + str(response.status_code))

    @line_magic
    def get_magic_out(self, command):
        ipy = get_ipython()
        out = io.StringIO()

        with redirect_stdout(out):
            ipy.magic(command)

        return out.getvalue().replace('\n', '').split('\t ')


    def get_notebook_name(self):

        kernel_id = re.search('kernel-(.*).json',
                              ipykernel.connect.get_connection_file()).group(1)
        servers = list_running_servers()
        for ss in servers:
            response = requests.get(urljoin(ss['url'], 'api/sessions'),
                                    params={'token': ss.get('token', '')})
            for nn in json.loads(response.text):
                if nn['kernel']['id'] == kernel_id:
                    relative_path = nn['notebook']['path']
                    return os.path.join(ss['notebook_dir'], relative_path)

    def get_accessed_files(self, ignoreAbsPaths=False):

        pid = os.getpid()
        log_file = "/tmp/p" + str(pid)
        f = open(log_file)
        raw = f.read()
        lines = raw.splitlines()

        ignore_list = ['/usr', '/lib','/home/dimuthu/.ipython/', "/dev", ".so", "/proc",
                       "/etc", "/tmp/pip-", "/home/dimuthu/.cache", "/root/.cache",
                       "ARCHIVE", "/root/.local", "dependencies.json", "context.p", "/root/.keras", '/var',
                       "files.json", "metadata.json", '/root/.fonts']

        for path in sys.path[1:]:
            if path:
                ignore_list.append(path)

        accessed_files = set()
        for line in lines:
            if line.count("O_NOFOLLOW") > 0:
                continue
            if line.count("O_DIRECTORY") > 0:
                continue
            if line.count("AT_FDCWD") == 0:
                continue

            if line.count("\"") > 1:
                start = line.index("\"")
                end = line.index("\"", start + 1)
                path = line[start+1 : end]
                should_ignore = False
                for ignore in ignore_list:
                    if path.count(ignore):
                        should_ignore = True
                        break

                if path.startswith('/opt/Notebooks'):
                    path = path.replace('/opt/Notebooks', '.', 1)

                if ignoreAbsPaths:
                    if path.startswith('/'):
                        should_ignore = True
                if not should_ignore:
                    accessed_files.add(path)

        if log_file in accessed_files:
            accessed_files.remove(log_file)

        prev_files_f = "/opt/ARCHIVE/files.json"
        if exists(prev_files_f):
            f = open(prev_files_f)
            prev_files_json = json.load(f)
            prev_files = set(prev_files_json.values())
            prev_files.remove(self.get_notebook_name())
            accessed_files = set.union(prev_files, accessed_files)

        return accessed_files

    def get_dependencies(self, local_ns, ignoredDeps = set()):
        raw_deps = subprocess.check_output(['pip', 'list']).decode("utf-8").split('\n')[2:]
        dependencies = {}
        for i in range(len(raw_deps)):
            parts = raw_deps[i].strip().split(' ')
            if len(parts) > 1 and not parts[0] in ignoredDeps:
                dependencies[parts[0]] = parts[-1]
        return dependencies

    def get_local_context(self, local_ns):
        var_context = {}
        local_variables = self.get_magic_out("who")[:-1]
        for var_name in local_ns:
            var_context[var_name] = local_ns[var_name]
            try:
                pickle.dumps(var_context)
            except:
                print("Warning: Variable " + var_name + " can not be exported")
                var_context.pop(var_name)

        return var_context

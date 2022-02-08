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

import re
import ipykernel
from notebook.notebookapp import list_running_servers
import requests
from requests.compat import urljoin
import json
import os.path

def load_ipython_extension(ipython):
    ipython.register_magics(StateCaptureMagic)

@magics_class
class StateCaptureMagic(Magics):

    @line_magic
    @needs_local_scope
    def load_local_context(self, line, cell="", local_ns=None):
        with open("ARCHIVE/context.p", "rb") as input_file:
            final_scope  = pickle.load(input_file)

        for var in final_scope:
            local_ns[var] = final_scope[var]


    @line_magic
    @needs_local_scope
    def export_states(self, line, cell="", local_ns=None):
        parameters = line.split(" ")

        createArchive = False
        uploadServer = None
        captureLocalContext = True

        for param in parameters:
            if param.startswith("createArchive"):
                flag = param.split("=")[1]
                if flag == "True":
                    createArchive = True

            if param.startswith("uploadServer"):
                uploadServer = param.split("=")[1]

            if param.startswith("captureLocalCtx"):
                flag = param.split("=")[1]
                if flag == "True":
                    captureLocalContext = True

        #print(createArchive)
        #print(uploadServer)

        pid = os.getpid()
        log_file = "/tmp/p" + str(pid)
        f = open(log_file)
        raw = f.read()
        lines = raw.splitlines()

        ignore_list = ['/usr', '/lib','/home/dimuthu/.ipython/', "/dev", ".so", "/proc",
                       "/etc", "/tmp/pip-", "/home/dimuthu/.cache", "/root/.cache", "ARCHIVE", "/root/.local"]

        notebook_name = self.get_notebook_name()

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
                if not should_ignore:
                    accessed_files.add(path)

        accessed_files.remove(log_file)
        accessed_files.add(notebook_name)

        def imports():
            for name, val in local_ns.items():
                if isinstance(val, types.ModuleType):
                    yield val.__name__

        import_list = list(imports())
        dependencies = {}
        for imp in import_list:
            try:
                if imp.count(".") > 0:
                    imp = imp.split(".")[0]
                dependencies[imp] = version(imp)
            except PackageNotFoundError:
                pass

        if createArchive:
            archive_dict = {}
            archive_dir = "ARCHIVE"

            dirpath = Path(archive_dir)
            if dirpath.exists() and dirpath.is_dir():
                shutil.rmtree(dirpath)

            os.mkdir(archive_dir)

            for f in accessed_files:
                if not f == "":
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

            if captureLocalContext:
                local_variables = self.get_magic_out("who")[:-1]
                var_context = {}
                for var_name in local_variables:
                    var_context[var_name] = local_ns[var_name]
                    try:
                        pickle.dumps(var_context)
                    except:
                        print("Warning: Variable " + var_name + " can not be exported")
                        var_context.pop(var_name)
                pickle.dump( var_context, open( archive_dir + "/context.p", "wb" ))

            shutil.make_archive("ARCHIVE", 'zip', archive_dir)
            print("Download the state export ")
            display(FileLink("ARCHIVE.zip"))

        return {"accessed_files": list(accessed_files), "dependencies": dependencies}

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

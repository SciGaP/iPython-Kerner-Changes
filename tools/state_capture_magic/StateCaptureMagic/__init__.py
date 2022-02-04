from __future__ import print_function
from IPython.core.magic import (Magics, magics_class, line_magic,
                                cell_magic, line_cell_magic)
from IPython.core.magic import needs_local_scope
import os
import types
from importlib.metadata import PackageNotFoundError
from importlib.metadata import version
import sys

def load_ipython_extension(ipython):
    ipython.register_magics(StateCaptureMagic)

@magics_class
class StateCaptureMagic(Magics):

    @line_magic
    @needs_local_scope
    def export_states(self, line, cell="", local_ns=None):
        parameters = line.split(" ")

        createArchive = False
        uploadServer = None

        for param in parameters:
            if param.startswith("createArchive"):
                flag = param.split("=")[1]
                if (flag == "True"):
                    createArchive = True

            if param.startswith("uploadServer"):
                uploadServer = param.split("=")[1]

        #print(createArchive)
        #print(uploadServer)

        pid = os.getpid()
        log_file = "/tmp/p" + str(pid)
        f = open(log_file)
        raw = f.read()
        lines = raw.splitlines()

        ignore_list = ['/usr', '/lib','/home/dimuthu/.ipython/', "/dev", ".so", "/proc",
                       "/etc", "/tmp/pip-", "/home/dimuthu/.cache", "/root/.cache"]

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

        return {"accessed_files": list(accessed_files), "dependencies": dependencies}

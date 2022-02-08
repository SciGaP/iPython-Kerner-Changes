import json
import os
import shutil
import subprocess
import sys

archive_dir = "/opt/ARCHIVE/"

f = open(archive_dir + 'metadata.json')
metadata_json = json.load(f)
work_dir = metadata_json["workingDir"] + "/"

f = open(archive_dir + 'files.json')
files_json = json.load(f)

for v in files_json:
    target_path = files_json[v]
    if not target_path.startswith("/"):
        target_path = work_dir + target_path

    dir_path = os.path.dirname(target_path)
    os.makedirs(dir_path, exist_ok = True)

    shutil.copyfile(archive_dir + v, target_path)

f = open(archive_dir + 'dependencies.json')
dep_json = json.load(f)

for dep_name in dep_json:
    dep_version = dep_json[dep_name]
    subprocess.check_call([sys.executable, "-m", "pip", "install", dep_name + "==" + dep_version])

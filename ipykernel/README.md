# IPython Kernel for Jupyter

This package provides the IPython kernel for Jupyter.

## Installation from source

1. `git clone`
2. `cd ipykernel`
3. `pip install -e ".[test]"`
4. `jupyter kernelspec install  --user /home/dimuthu/code/ipykernel/`
5. `jupyter notebook --ip 0.0.0.0 `
6. `sudo python3 tools/strace_server.py`

After that, all normal `ipython` commands will use this newly-installed version of the kernel.

## Running tests

Follow the instructions from `Installation from source`.

and then from the root directory

```bash
pytest ipykernel
```

## Running tests with coverage

Follow the instructions from `Installation from source`.

and then from the root directory

```bash
pytest ipykernel -vv -s --cov ipykernel --cov-branch --cov-report term-missing:skip-covered --durations 10
```

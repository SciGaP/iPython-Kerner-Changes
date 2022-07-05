from IPython import get_ipython
ipython = get_ipython()

if 'ipython' in globals():
    print('\nWelcome to IPython!')
    ipython.magic('load_ext StateCaptureMagic')
    ipython.magic('load_local_context')
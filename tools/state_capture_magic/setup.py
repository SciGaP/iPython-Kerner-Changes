from distutils.core import setup

version = open('StateCaptureMagic/VERSION', 'r').readline().strip()

long_desc = """
IPython extension for capturing the notebook states
"""


setup(
    name='state-capture-magic',
    version=version,
    packages=['StateCaptureMagic'],
    url='https://github.com/rustyrazorblade/ipython-cql',
    license='Apache 2',
    author='Dimuthu Wannpurage',
    author_email='dwannipu@iu.edu',
    description='IPython Extension for Cassandra integration',
    install_requires=[
        'dill',
        'requests',
    ],
)

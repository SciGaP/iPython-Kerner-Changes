import socket
import sys
import os
import subprocess

trace_directory = "/home/dimuthu/jupyter-trace"
server_address = trace_directory + '/uds_socket'

# Make sure the socket does not already exist
try:
    os.unlink(server_address)
except OSError:
    if os.path.exists(server_address):
        raise

sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)

print('starting up on ' + server_address)
sock.bind(server_address)
os.chmod(server_address, 0o777)

sock.listen(1)

while True:
    # Wait for a connection
    print('waiting for a connection')
    connection, client_address = sock.accept()
    try:
        print('connection from ' + client_address)

        # Receive the data in small chunks and retransmit it
        data = connection.recv(16)
        print("received " +  str(data))
        if data:
            processId = data.decode("utf-8")
            print("Running sub process to strace")
            subprocess.call("sudo strace -p " + processId + " -Tfe trace=openat -o " + trace_directory + "/p" + processId + " &", shell = True)
            #print('sending data back to the client')
            #connection.sendall(data)
        else:
            print('no more data from' + client_address)
            break

    finally:
        # Clean up the connection
        connection.close()
import socket
import sys
import os
import subprocess

trace_directory = "/tmp"
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
strace_pids = {}

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
            start = True
            if processId.startswith("START:"):
                processId = processId.split(":")[1]

            if processId.startswith("STOP:"):
                processId = processId.split(":")[1]
                start = False

            if start :
                print("Running sub process to strace")
                process = subprocess.call("strace -p " + processId + " -Tfe trace=openat -A -o " + trace_directory + "/p" + processId + " &", shell = True)

            if not start:
                print("Stopping sub process strace")
                out = subprocess.run(["ps", "-ef"], capture_output=True)
                arr = out.stdout.decode("utf-8").split("\n")
                straceProcess = None
                for elem in arr:
                    if "strace -p " + processId in elem:
                        straceProcess = " ".join(elem.split()).split(" ")[1]

                if (straceProcess):
                    subprocess.run(["kill", straceProcess], capture_output=True)

        else:
            print('no more data from' + client_address)
            break

    finally:
        # Clean up the connection
        connection.close()
import socket

with socket.socket() as sock:
    sock.connect(("127.0.0.1", 8888))
    sock.send(b"Hello, TCP Echo Server!")
    print(sock.recv(1024))

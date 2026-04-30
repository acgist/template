import socket
import threading

def handle_client(conn, addr):
    print(f"[+] New TCP connection from {addr}")
    try:
        while True:
            data = conn.recv(65536)
            if not data:
                break
            conn.sendall(data)
            print(data)
    except Exception as e:
        print(f"[-] TCP Error with {addr}: {e}")
    finally:
        conn.close()
        print(f"[-] TCP Disconnection from {addr} closed")


def start_server(host="0.0.0.0", port=8888):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
        server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        server_socket.bind((host, port))
        server_socket.listen(5)
        print(f"[*] TCP Echo Server listening on {host}:{port}")
        while True:
            conn, addr = server_socket.accept()
            client_thread = threading.Thread(target=handle_client, args=(conn, addr))
            client_thread.daemon = True
            client_thread.start()

if __name__ == "__main__":
    start_server()

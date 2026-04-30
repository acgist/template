import socket

def udp_echo_server(host="0.0.0.0", port=1081):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.bind((host, port))
    print(f"UDP Echo Server 启动监听 {host}:{port} ...")
    try:
        while True:
            data, addr = sock.recvfrom(65535)
            print(
                f"收到来自 {addr} 的数据 ({len(data)} 字节): {data[:50]}{'...' if len(data) > 50 else ''}"
            )
            sock.sendto(data, addr)
    except KeyboardInterrupt:
        print("服务器已停止")
    finally:
        sock.close()

if __name__ == "__main__":
    udp_echo_server(port=1080)

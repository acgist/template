import socket
import sys


def udp_echo_client(host="127.0.0.1", port=1081, message="Hello, UDP Echo Server!"):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        print(f"正在向 {host}:{port} 发送消息: {message}")
        sock.sendto(message.encode("utf-8"), (host, port))
        data, server = sock.recvfrom(65535)
        print(
            f"收到来自 {server} 的回复 ({len(data)} 字节): {data.decode('utf-8', errors='replace')}"
        )
    except socket.timeout:
        print("请求超时")
    except Exception as e:
        print(f"发生错误: {e}")
    finally:
        sock.close()

if __name__ == "__main__":
    host = "127.0.0.1"
    port = 1080
    message = "Hello, UDP Echo Server!"
    if len(sys.argv) >= 2:
        host = sys.argv[1]
    if len(sys.argv) >= 3:
        port = int(sys.argv[2])
    if len(sys.argv) >= 4:
        message = sys.argv[3]
    udp_echo_client(host, port, message)

"""
哔哩哔哩下载
"""

import os
import re
import cv2
import time
import urllib
import requests

headers = {
    "User-Agent": "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Mobile Safari/537.36"
}

def get_download_url(play_url):
    print(f"预览地址：{play_url}")
    response = requests.get(play_url, headers = headers)
    urls = re.compile(""".*"url" ?\: ?"(.*mp4.*?)".*""").findall(response.text)
    if len(urls) != 1:
        return None
    return urls[0].encode("utf-8").decode("unicode_escape")

def download(download_url):
    if not download_url:
        return
    filename = os.path.basename(urllib.parse.urlparse(download_url).path)
    filepath = os.path.join("D:/download", filename)
    print(f"下载地址：{filepath} = {download_url}")
    with open(filepath, "wb") as file:
        response = requests.get(download_url, headers = headers)
        file.write(response.content)
        video = cv2.VideoCapture(filepath)
        print(f"视频信息：{video.get(cv2.CAP_PROP_FRAME_COUNT) / video.get(cv2.CAP_PROP_FPS)} s")

play_urls = [
    # -
]

for play_url in play_urls:
    download(get_download_url(play_url))
    time.sleep(10)
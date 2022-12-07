# 後に決め打つためにデバイスIDを取得するだけのプログラム

import requests
import json

# open token
OPEN_TOKEN = ''
# secret key
SEACRET_TOKEN= ''
API_HOST = 'https://api.switch-bot.com'

DEBIVELIST_URL = f"{API_HOST}/v1.0/devices"

HEADERS = {
    'Authorization': OPEN_TOKEN,
    'Content-Type': 'application/json; charset=utf8'
}


def _get_request(url):
    res = requests.get(url, headers=HEADERS)
    data = res.json()
    if data['message'] == 'success':
        return res.json()
    return {}
    
def get_device_list():
    try:
        return _get_request(DEBIVELIST_URL)["body"]
    except:
        return

def get_virtual_device_list():
    devices = get_device_list()
    return devices['infraredRemoteList']

data = get_device_list()
print(data)

# ウィンドウを閉じないようにするもの
input()
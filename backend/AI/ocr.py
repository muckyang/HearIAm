import json
import base64
import requests
import socket

if(socket.gethostname()[:7] == "DESKTOP"):
    with open("./frontend/public/images/kisa.jpg", "rb") as f:
        img = base64.b64encode(f.read())
else:
    pass
    # r = open('/var/lib/jenkins/workspace/maven-test/frontend/public/textFiles/' + argv[1], mode='rt', encoding='utf-8')


 # 본인의 APIGW Invoke URL로 치환
URL = "https://5fcbbe8db98e40e981e033236ea88c5d.apigw.ntruss.com/custom/v1/4517/8acc3840cb170d007ba2d6c38055e68292e26a1bb3837bc70ae69c47f8755eea/general"
    
 # 본인의 Secret Key로 치환
KEY = "WkZra0ZMUGhpSnZyVVlnWkRhUFlYd3BtSEtyQXVqaFU="
    
headers = {
    "Content-Type": "application/json",
    "X-OCR-SECRET": KEY
}
    
data = {
    "version": "V1",
    "requestId": "heariam", # 요청을 구분하기 위한 ID, 사용자가 정의
    "timestamp": 0, # 현재 시간값
    "images": [
        {
            "name": "sample_image",
            "format": "png",
            "data": img.decode('utf-8')
        }
    ]
}
data = json.dumps(data)
response = requests.post(URL, data=data, headers=headers)
res = json.loads(response.content)
datas = res["images"][0]["fields"]

info = []

for d in datas:
    info.append(d["inferText"])
    # print(d["inferText"])

# print(info)

name = info[32]
birth = info[47][2:4] + info[48] + info[49]
serialNum = info[9]
date = info[99][0:4] + info[101][0:2] + info[102][0:2]
code = info[0]

print("이름: " + name)
print("생년월일: " + birth)
print("자격번호: " + serialNum)
print("발급일: " + date)
print("내지번호: " + code)
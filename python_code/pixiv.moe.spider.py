import requests
import zipfile
import os
import xml.etree.ElementTree as ET
from xml.etree.ElementTree import Element
import random


mode = {
    "今日": "daily",
    "本周": "weekly",
    "本月": "monthly",
    "男性向": "male",
    "女性向": "female"
}

url = "https://api.pixiv.moe/v2/ranking?mode=" + mode[input("请输入类型： 今日 本周 本月 男性向 女性向\n")] + "&page=1"
URL = url[:-1]

headers = {
    "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 Edg/103.0.1264.62",
    "authorization": "JWT eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MjE2MTUxMDQ3NTI2MTU0OTAyNiwiaWF0IjoxNjU4MzE4NDcwLCJleHAiOjE2NTg3NTA0NzB9.su7i0kvq1RoLjyt-n_uv7q8nOOOkhfQGv50fONlb4s34nBvzzqDxtyAKgyp6FcMYAY5k6FtMX0_Wp23A1cbItwOv-cnqPntRsJowLefgMqzq9KHcJWwuD1HAfxBqA1Q9ox5_hU2XHM8VIIvJaD2l-IpVd62x6ztBW6Qxb-YDgha0qclJzmBTnlz_zUXjv1bnRDnuKGTIqhWqt0xO7a_FjL9ngJ8QL7rZ9oKhRN_AgANXd-V8E4mi_zG7oVlvvCkuLT9aWmay6NO4yzC47GZtDwSxo17JAgwECJNDLpvgr2LvpgbjpG8eRkb43s208QuOIDpkYEC3VK-KEE3BExqJhw",
    "origin": "https://pixiv.moe"
}

def fileNameLegal (name):
    return name.replace("/", ".").replace(":","-").replace("*", "x").replace("?", " ").replace('"', "'").replace("<", "[").replace(">", "]").replace("|", "-")

src = "H:/spidertarget/"    # 下载路径


i = 1
while i < 100:
    pageUrl = URL + str(i)

    resp = requests.get(url=pageUrl, headers=headers)
    resp.encoding = "utf-8"

    pagesData = resp.json()["response"]["illusts"]

    for data in pagesData:

        # 读取xml文档，获取已下载文件的记录
        xml = ET.parse(src + "Downloaded.xml")
        xml_root = xml.getroot()

        # 检查是否已经下载过
        flag = 0
        if(xml_root.__len__() != 0):
            for child in xml_root:
                if(data["id"] == child.text):
                    flag = 1
        if(flag == 1):
            print("continue")
            continue

        # 下载压缩包
        fileUrl = "https://api.pixiv.moe/v2/illust/" + data["id"] + "/zip"
        zip_file = requests.get(url=fileUrl, headers=headers)
        open(src + "target_file.zip", "wb").write(zip_file.content)
        zip_file.close()

        # 解压文件
        zip_file = zipfile.ZipFile(src + "target_file.zip", "r")
        zip_list = zip_file.namelist()
        zip_file.extract(zip_list[0], src)
        img_name = fileNameLegal(data["id"] + "-" + data["user"]["name"] + " " + data["title"] + zip_list[0][zip_list[0].rindex('.'):])
        zip_file.close()
        os.remove(src + "target_file.zip")

        # 重命名
        os.rename(src + zip_list[0], src + img_name)

        # 将id写入xml
        id = Element("id")
        id.text = data["id"]
        xml_root.append(id)
        xml.write(src + "Downloaded.xml")

        # 打印下载结果
        print(img_name + "  已下载")

    resp.close()
    i += 1

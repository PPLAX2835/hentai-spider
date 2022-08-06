from concurrent.futures.thread import ThreadPoolExecutor

import requests
import urllib.parse
from bs4 import BeautifulSoup
import re
import MySQLdb



# 从页面内得到总页数
def getLastPage(url):
    resp = requests.get(url)
    page_content = resp.text

    bs = BeautifulSoup(page_content, "html.parser")
    paginator = bs.find(text="Last").parent
    href = str(paginator.get("href")) + ""

    obj = re.compile(r"/post/list/.*?/(?P<Lpage>\d+)", re.S)
    result = obj.match(href).group("Lpage")

    resp.close()

    return int(result)

# 将图片id提交到数据库
def recordId(id_str):
    sql = "insert into rule34(id) value ('" + id_str + "');"
    try:
        # 执行sql语句
        cursor.execute(sql)
        # 提交到数据库执行
        db.commit()
    except:
        # 发生错误时回滚
        db.rollback()

# 检查数据库内是否重复
def isDownloaded(id_str):
    sql = "SELECT IFNULL((SELECT TRUE FROM rule34 WHERE id='" + id_str + "' LIMIT 1),FALSE) ;"
    try:
        # 执行sql语句
        cursor.execute(sql)
        if(cursor.fetchall()[0][0] == 1):
            return True

    except:
        # 发生错误时回滚
        db.rollback()
    return False

# 获得文件id
def getId(file_name):
    obj = re.compile(r"(?P<id>^\d+)", re.S)

    for i in obj.finditer(file_name):
        return i.group("id")

# 文件名合法化
def fileNameLegal (name):
    result = name.replace("/", ".").replace(":","-").replace("*", "x").replace("?", " ").replace('"', "'").replace("<", "[").replace(">", "]").replace("|", "-")
    if(len(result) >= 255):
        result = result[0:99] + "......"
    return result

# 下载
def download(href, src):
    file_name = fileNameLegal(urllib.parse.unquote(href.split("/")[-1]))
    id = getId(file_name)
    if isDownloaded(id) == False:
        down = requests.get(href)
        open(src + file_name, "wb").write(down.content)
        print(file_name, " downloaded")
        down.close()
        recordId(id + "")
    else:
        print("continue")

# 从页面中获取所有链接并下载
def pageDownload(url, src):
    page = requests.get(url)
    bs = BeautifulSoup(page.content, "html.parser")
    files = bs.find_all(text="File Only")
    page.close()

    for file in files:
        download(file.parent.get("href"), src)

def test(str):
    print(str)

if __name__ == "__main__":

    # 准备

    password = input("输入数据库密码：\n")
    # 连接数据库
    db = MySQLdb.connect("localhost", "root", password, "SpiderData", charset='utf8')
    # 使用cursor()方法获取操作游标
    cursor = db.cursor()


    # 链接
    kw = input("输入要查找的图片")
    url = "http://rule34.paheal.net/post/list/" + kw + "/1"
    URL = url[:-2]  # URL = "http://rule34.paheal.net/post/list/" + kw
    last_page = getLastPage(url)

    # 目标路径
    src = "H:/m'm/rule34/target/"

    # 创建线程池
    with ThreadPoolExecutor(10) as t:
        for i in range(last_page + 1):
            t.submit(pageDownload(URL + "/" + (i+1).__str__(), src), name=f"线程{i}")


    db.close()




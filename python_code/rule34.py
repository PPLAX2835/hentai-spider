import requests
import urllib.parse
import os
from bs4 import BeautifulSoup

kw = input("输入要查找的图片")
url = "http://rule34.paheal.net/post/list/" + kw + "/1"
URL = url[:-2]             # URL = "http://rule34.paheal.net/post/list/" + kw


headers = {
    "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.114 Safari/537.36 Edg/103.0.1264.62"
}

resp = requests.get(url=url, headers=headers)
soup = BeautifulSoup(resp.content, "html.parser")

paginator = soup.find(text="Last").parent
href = str(paginator.get("href")) + ""

last = int(href[href.rfind("/") + 1:])


i = 1
while( i <= last ):
    page = requests.get(URL + "/" + i.__str__() + "", headers=headers)
    Soup = BeautifulSoup(page.content, "html.parser")
    files = Soup.find_all(text="File Only")

    for file in files:  # 下载该页的所有图片
        resouse = file.parent.get("href")  # 文件地址
        fileName = urllib.parse.unquote(file.parent.get("href").split("/")[-1])  # 文件名
        src = "H:/m'm/rule34/target/" + fileName  # 下载的目标路径
        if(os.listdir("H:/m'm/rule34/target/").__contains__(fileName)):
            print("continue")
            continue

        # 临时
        if(fileName == "4486889 - Aatrox Akshan Aphelios Aurelion_Sol Brand_the_Burning_Vengeance Braum Darius Dr._Mundo Draven Ekko Gangplank Garen_Crownguard Graves Hecarim Jarvan_IV Jayce Kayn League_of_Legends Lee_Sin Maokai Master_Yi Mordekaiser OA_Reeeere Pantheon Rakan Renekton Rengar Sett Shen Singed Swain Sylas Talon Taric Thresh Trundle Tryndamere Varus Viego Viktor Yasuo Yone Yorick ZAC rhaast.jpg"):
            print("continue")
            continue
        if(fileName == "1950877 - Alistar Aurelion_Sol Azir Bard Cho'Gath Galio Galrock Jax Kha'Zix Kog'Maw League_of_Legends Malphite Maokai Merchant Nasus Rammus Renekton Rengar Rumble Tahm_Kench Trundle Twitch_the_Plague_Rat Vel'Koz Volibear Warwick Wukong Xerath ZAC Ziggs yordle.jpg"):
            print("continue")
            continue
        if(fileName == "3858137 - Appleseed Beastars Blazblue Blood_Blockade_Battlefront Briareos Crysis Crysis_2 DC Darksiders Death_Adder Dragaux Fatal_Fury Five_Nights_at_Freddy's Five_Nights_at_Freddy's:_Help_Wanted Gigas Glitchtrap Golden_Axe Green_Lantern_(series) Green_Lantern_Corps Greninja Grillby Grimmsnarl Jon_Talbain Kilowog Laurence_Barnes League_of_Legends Legoshi Machoke Monster_Hunter Porkyman RG01 RingFit_Adventure Star_Fox Strife Surtr Susanoo Tekken Tizoc Tokyo_Afterschool_Summoners Undertale Wolf_O'Donnell ZAC Zed_O'Brien Zinogre crossover.jpg"):
            print("continue")
            continue





        print("Downloading " + fileName)
        download = requests.get(resouse)
        open(src, "wb").write(download.content)
        print("Finished")
        download.close()
    page.close()
    i += 1

print("Finished!")

resp.close()

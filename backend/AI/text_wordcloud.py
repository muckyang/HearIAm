import sys
import io
from wordcloud import WordCloud
from collections import Counter
from konlpy.tag import Okt
import time
import socket

def get_noun(text):
    okt = Okt()
    noun = okt.nouns(text)
    for i,v in enumerate(noun):
        if len(v) < 2:
            noun.pop(i)
    count = Counter(noun)
    noun_list = count.most_common(100)
    best_list = count.most_common(3)
    
    best = dict(best_list).keys()
    for item in best:
        print(item)

    return noun_list

def visualize(noun_list, fileName):
    if(socket.gethostname()[:7] == "DESKTOP"):
        wc = WordCloud(font_path='C:\\Windows\\Fonts\\NGULIM.TTF', background_color='white', width=1000, height=1000, max_font_size=300)
        wc.generate_from_frequencies(dict(noun_list))
        # wc.to_file("frontend/words/"  + fileName)
        wc.to_file("frontend/public/wordcloud/"  + fileName)
    else:
        wc = WordCloud(font_path='/usr/share/fonts/truetype/dejavu/NanumMyeongjo.ttf', background_color='white', width=1000, height=1000, max_font_size=300)
        wc.generate_from_frequencies(dict(noun_list))
        # wc.to_file("/var/lib/jenkins/workspace/front/frontend/words/" + fileName)
        wc.to_file("/var/lib/jenkins/workspace/front/frontend/public/wordcloud/" + fileName)

if __name__ == "__main__":
    sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
    sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
    now = int(round(time.time() * 1000))
    fileName = 'keyword' + str(now) + '.png'
    print(fileName)
    noun_list = get_noun(sys.argv[1])
    visualize(noun_list, fileName)
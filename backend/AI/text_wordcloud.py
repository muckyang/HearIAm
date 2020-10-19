import sys
import io
from wordcloud import WordCloud
from collections import Counter
from konlpy.tag import Okt

def get_noun(text):
    okt = Okt()
    noun = okt.nouns(text)
    for i,v in enumerate(noun):
        if len(v) < 2:
            noun.pop(i)
    count = Counter(noun)
    noun_list = count.most_common(100)
    return noun_list

def visualize(noun_list):
    # print(argv[1])
    wc = WordCloud(font_path='C:\\Windows\\Fonts\\NGULIM.TTF', background_color='white', width=1000, height=1000, max_font_size=300)
    wc.generate_from_frequencies(dict(noun_list))
    wc.to_file('keyword.png')

if __name__ == "__main__":
    sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
    sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
    noun_list = get_noun(sys.argv[1])
    visualize(noun_list)
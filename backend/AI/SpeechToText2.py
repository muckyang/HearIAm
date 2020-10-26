import speech_recognition as sr
import sys
import io

# AUDIO_FILE = "./backend/.mvn/audio/test4.wav"
AUDIO_FILE = "./frontend/src/assets/record/656353320record.webm"

# audio file을 audio source로 사용합니다
r = sr.Recognizer()
with sr.AudioFile(AUDIO_FILE) as source:
    audio = r.record(source)  # 전체 audio file 읽기

# 구글 웹 음성 API로 인식하기 (하루에 제한 50회)
try:
    print("Google Speech Recognition thinks you said : " + r.recognize_google(audio, language='ko'))
except sr.UnknownValueError:
    print("Google Speech Recognition could not understand audio")
except sr.RequestError as e:
    print("Could not request results from Google Speech Recognition service; {0}".format(e))
    

# def main():
#     # AUDIO_FILE = "./frontend/src/assets/record/" + argv[1]
#     AUDIO_FILE = "./frontend/src/assets/record/656341800test.wav" 
#     r= sr.Recognizer()
#     with sr.AudioFile(AUDIO_FILE) as source:
#         audio = r.record(source)
#     try:
#         words = r.recognize_google(audio, language='ko')
#         print("Google Speech Recognition thinks you said : " + words)
#     except sr.UnknownValueError:
#         print("Google Speech Recognition could not understand audio")
#     except sr.RequestError as e:
#         print("Could not request results from Google Speech Recognition service; {0}".format(e))
    
# if __name__ == "__main__":
#     print("start py")
#     sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
#     sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
#     main()
  
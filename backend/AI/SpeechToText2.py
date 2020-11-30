import speech_recognition as sr
import sys
import io
import socket

# AUDIO_FILE = "./backend/.mvn/audio/test4.wav"
# AUDIO_FILE = "./frontend/src/assets/record/656777400record.wav"

# audio file을 audio source로 사용합니다
# r = sr.Recognizer()
# with sr.AudioFile(AUDIO_FILE) as source:
#     audio = r.record(source)  # 전체 audio file 읽기

# # 구글 웹 음성 API로 인식하기 (하루에 제한 50회)
# try:
#     print("Google Speech Recognition thinks you said : " + r.recognize_google(audio, language='ko'))
# except sr.UnknownValueError:
#     print("Google Speech Recognition could not understand audio")
# except sr.RequestError as e:
#     print("Could not request results from Google Speech Recognition service; {0}".format(e))
    

def main(argv):
    if(socket.gethostname()[:7] == "DESKTOP"):
        AUDIO_FILE = "./frontend/public/record/" + argv[1]
    else:
        AUDIO_FILE = "/var/lib/jenkins/workspace/front/frontend/public/record/" + argv[1]

    r= sr.Recognizer()
    with sr.AudioFile(AUDIO_FILE) as source:
        audio = r.record(source)
    try:
        words = r.recognize_google(audio, language='ko')
        print(words)
    except sr.UnknownValueError:
        print("Google Speech Recognition could not understand audio")
    except sr.RequestError as e:
        print("Could not request results from Google Speech Recognition service; {0}".format(e))
    
if __name__ == "__main__":
    sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding = 'utf-8')
    sys.stderr = io.TextIOWrapper(sys.stderr.detach(), encoding = 'utf-8')
    main(sys.argv)
  
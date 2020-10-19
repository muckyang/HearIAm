import speech_recognition as sr
sr.__version__


r = sr.Recognizer()

harvard = sr.AudioFile('./backend/.mvn/audio/test1.mp3')
with harvard as source:
    audio = r.record(source)

r.recognize_google(audio)

with harvard as source:
    audio1 = r.record(source, duration=4)
    audio2 = r.record(source, duration=4)
    audio3 = r.record(source, duration=4)
    audio4 = r.record(source, duration=4)
    audio5 = r.record(source, duration=4)
print(r.recognize_google(audio1))
print(r.recognize_google(audio2))
print(r.recognize_google(audio3))
print(r.recognize_google(audio4))
print(r.recognize_google(audio5))


with harvard as source:
    audio = r.record(source, offset=7, duration=3)
    print(r.recognize_google(audio))

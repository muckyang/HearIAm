// import bufferToWav from "audiobuffer-to-wav";
import bufferToWav from "audiobuffer-to-wav";

export default {
    data() {
        return {
            isSupported: false,
            hasPermission: false,
            isRecording: false,
            isPaused: false,
            chunks: [],
            recordFileList: [],
        }
    },
    methods: {
        async start() {
            if (this.isRecording) {
                return
            }

            try {
                this.$_stream = await this.getStream()
                this.prepareRecorder()
                this.$_mediaRecorder.start()
            } catch (e) {
                this.$emit('error', e)
                    // eslint-disable-next-line
                console.error(e);
            }
        },
        stop() {
            if (!this.isRecording) return
            this.$_mediaRecorder.stop()
            console.log("stop")
            this.$_stream.getTracks().forEach(t => t.stop())
        },
        pause() {
            if (!this.isRecording) return
            this.$_mediaRecorder.pause()
        },
        resume() {
            if (!this.isPaused) return
            this.$_mediaRecorder.resume()
        },

        /**
         * Get the input stream based on constraints and emit the stream event
         * to the parent component so he can use it for processing or show a preview
         */
        async getStream() {
            const stream = await navigator.mediaDevices.getUserMedia(this.constraints)
            console.log(stream)
            this.$_stream = stream
            this.$emit('stream', stream)
            return stream
        },

        /**
         * Create a new media recorder with the user media stream
         * and set some event listeners to update component data
         * and emit events to the parent component
         */
        prepareRecorder() {
            if (!this.$_stream) {
                return
            }

            this.$_mediaRecorder = new MediaRecorder(this.$_stream, {
                mimeType: "video/webm"
            })

            this.$_mediaRecorder.ignoreMutedMedia = true

            this.$_mediaRecorder.addEventListener('start', () => {
                this.isRecording = true
                this.isPaused = false
                this.$emit('start')
            })

            this.$_mediaRecorder.addEventListener('resume', () => {
                this.isRecording = true
                this.isPaused = false
                this.$emit('resume')
            })

            this.$_mediaRecorder.addEventListener('pause', () => {
                this.isPaused = true
                this.$emit('pause')
            })

            // Collect the available data into chunks
            this.$_mediaRecorder.addEventListener('dataavailable', (e) => {
                if (e.data && e.data.size > 0) {
                    this.chunks.push(e.data)
                }
            }, true)

            // On recording stop get the data and emit the result
            // than clear all the recording chunks
            this.$_mediaRecorder.addEventListener('stop', async() => {
                this.$emit('stop')
                    // var wav = bufferToWav(this.chunks);
                    // var blobData = new window.Blob([new DataView(wav)], {
                    //   type: "audio/wav"
                    // });
                let blobData = new Blob(this.chunks, { type: this.$_mediaRecorder.mimeType })
                let audioURL = window.URL.createObjectURL(blobData);
                let arrayBuffer = await (await fetch(audioURL)).arrayBuffer();
                let AudioBuffer = await new AudioContext().decodeAudioData(
                    arrayBuffer
                );
                var wav = bufferToWav(AudioBuffer);
                var blob = new window.Blob([new DataView(wav)], {
                    type: "audio/wav"
                });

                if (blob.size > 0) {
                    this.$emit('result', blob)
                }
                this.chunks = []
                this.isPaused = false
                this.isRecording = false
            }, true)
        },

        //   function blobToFile (theBlob: Blob, fileName:string): File => {
        //     var b: any = theBlob;
        //     //A Blob() is almost a File() - it's just missing the two properties below which we will add
        //     b.lastModifiedDate = new Date();
        //     b.name = fileName;

        //     //Cast to a File() type
        //     return <File>theBlob;
        // }
    },
    mounted() {
        if (!navigator.mediaDevices && !navigator.mediaDevices.getUserMedia) {
            // eslint-disable-next-line
            console.warn('Media Devices are not supported from your browser.')
            return
        }

        // video recorder on Safari is not currently supported
        // TODO: we could use https://github.com/CameraKit/webm-media-recorder
        if (!window.MediaRecorder && this.constraints.video) {
            // eslint-disable-next-line
            console.warn('MediaRecorder for video is not supported from your browser.')
            return
        }

        this.isSupported = true
    }
}
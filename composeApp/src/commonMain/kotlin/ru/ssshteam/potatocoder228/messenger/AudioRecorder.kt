package ru.ssshteam.potatocoder228.messenger

interface AudioRecorder {
    fun record(path: String, stream: Any): String
    fun isRecording(): Boolean
    fun stopRecord()

    fun play(path: String)
    fun isPlaying(): Boolean
    fun stopPlaying()
}
package com.example.joelerry.game

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.example.joelerry.Constants
import com.example.joelerry.R

class GameAudio{

    companion object {
        private var songMap = hashMapOf(Pair("Intro", R.raw.intro), Pair("Game", R.raw.game))
        private var audioList = hashMapOf(
            Pair("Tutorial1", R.raw.larry_intro1),
            Pair("Tutorial2", R.raw.larry_intro2),
            Pair("Tutorial3", R.raw.larry_intro3),
            Pair("JoeComida", R.raw.joe_comida),
            Pair("JoeDerrota", R.raw.joe_derrota),
            Pair("LarryVictoria", R.raw.larry_victoria),
            Pair("LarryIntro", R.raw.larry_pantalla_intro)
        )
    }

    private lateinit var actualSong: MediaPlayer
    private var playingState = 0

    fun selectAudio(context: Context, newSong: String) {
        var songFromList = songMap[newSong]
        var isSong = true
        if (songFromList == null) {
            songFromList = audioList[newSong]
            isSong = false
            if (songFromList == null) {
                Log.e("SOUND", "Invalid song id")
                return
            }
        }
        actualSong = MediaPlayer.create(context, songFromList)
        playingState = 1
        actualSong.setOnCompletionListener { playingState = 0 }
        actualSong.start()
        setVolume(if (isSong) Constants.data.volumenMusica else Constants.data.volumenSonido)
    }

    fun pauseAudio() {
        if (playingState == 1) {
            actualSong.pause()
            playingState = 2
        }
    }

    fun resumeAudio() {
        if (playingState == 2) {
            actualSong.start()
            playingState = 1
        }

    }
    fun stopAudio(){
        actualSong.stop()
    }
    fun bucleAudio(){
        actualSong.isLooping = true
    }

    fun setVolume(volumenMusica: Int) {
        actualSong.setVolume(volumenMusica / 100f, volumenMusica / 100f)
    }

}
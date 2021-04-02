package com.example.joelerry.nivell

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.joelerry.Constants
import com.example.joelerry.Data
import com.example.joelerry.game.GameView

class Nivell: AppCompatActivity() {

    private lateinit var gameView: GameView
    private var level = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()

        super.onCreate(savedInstanceState)

        Constants.MUSIC.stopAudio()
        Constants.MUSIC.selectAudio(this, "Game")
        Constants.MUSIC.bucleAudio()

        level = intent.getIntExtra(Constants.TAG_SELECT_LEVEL, -1)
        gameView = GameView(this, level)
        setContentView(gameView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.NIVELL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null)
                    gameView.gestioOption(data.getSerializableExtra(Constants.TAG_ENDGAME) as Constants.Companion.InGameOptions)
            } else if (resultCode == Activity.RESULT_CANCELED)
                gameView.gestioOption(Constants.Companion.InGameOptions.SelectLevel)
        }
    }

    override fun onStop() {
        Data.writeData(this)
        super.onStop()
    }

    override fun onPause() {
        if (!isFinishing) {
            Constants.MUSIC.pauseAudio()
            Constants.SOUNDS.pauseAudio()
        }
        super.onPause()
    }

    override fun onResume() {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()
        super.onResume()
    }

    override fun onRestart() {
        Constants.MUSIC.stopAudio()
        Constants.MUSIC.selectAudio(this, "Game")
        Constants.MUSIC.bucleAudio()
        super.onRestart()
    }

}
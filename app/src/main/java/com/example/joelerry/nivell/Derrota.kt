package com.example.joelerry.nivell

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.joelerry.Constants
import com.example.joelerry.R

class Derrota : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.derrota)

        Constants.SOUNDS.stopAudio()
        Constants.SOUNDS.selectAudio(this, "JoeDerrota")

        findViewById<Button>(R.id.btnRetryDerrota).setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().also {
                    it.putExtra(
                        Constants.TAG_ENDGAME,
                        Constants.Companion.InGameOptions.Retry
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
            finish()
        }
        findViewById<Button>(R.id.btnSelLvlDerrota).setOnClickListener {
            setResult(
                Activity.RESULT_OK,
                Intent().also {
                    it.putExtra(
                        Constants.TAG_ENDGAME,
                        Constants.Companion.InGameOptions.SelectLevel
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
            finish()
        }
        findViewById<TextView>(R.id.puntuacioDerrota).text = intent.getIntExtra(Constants.TAG_PUNTUACIO, 0).toString()
        findViewById<TextView>(R.id.puntuacioMaxDerrota).text = Constants.data.puntuacionMaxima.toString()
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

    override fun onStop() {
        Constants.SOUNDS.stopAudio()
        super.onStop()
    }
}
package com.example.joelerry

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import com.example.joelerry.logIn_register.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Data.readData(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Constants.MUSIC.selectAudio(applicationContext,"Intro")
        Constants.MUSIC.bucleAudio()
        Constants.SOUNDS.selectAudio(applicationContext,"LarryIntro")
        findViewById<ImageButton>(R.id.imagenPortada).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) }
            )
        }
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
}
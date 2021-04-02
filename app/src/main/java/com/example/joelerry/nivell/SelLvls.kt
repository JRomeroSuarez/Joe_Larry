package com.example.joelerry.nivell

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.joelerry.Constants
import com.example.joelerry.R
class SelLvls :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.sel_lvls)

        findViewById<Button>(R.id.buttonSelLvl1).setOnClickListener {
                startActivity(
                    Intent(
                        this,
                        Nivell::class.java
                    ).also {
                        it.putExtra(
                            Constants.TAG_SELECT_LEVEL,
                            1
                        ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    })
            }


        findViewById<Button>(R.id.buttonSelLvl2).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        2
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl3).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        3
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl4).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        4
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl5).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        5
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl6).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        6
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl7).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        7
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl8).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        8
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
        }
        findViewById<Button>(R.id.buttonSelLvl9).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Nivell::class.java
                ).also {
                    it.putExtra(
                        Constants.TAG_SELECT_LEVEL,
                        9
                    ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                })
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

    override fun onStop() {
        Constants.SOUNDS.stopAudio()
        super.onStop()
    }

    override fun onRestart() {
        Constants.MUSIC.stopAudio()
        Constants.MUSIC.selectAudio(this, "Intro")
        Constants.MUSIC.bucleAudio()
        super.onRestart()
    }
}
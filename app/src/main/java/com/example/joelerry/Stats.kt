package com.example.joelerry

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class Stats : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.stats)

        Constants.stats = this
        updateStats()
    }

    private fun intToTime(time: Int): String {
        val sb = StringBuilder()
        if (time >= 86400)
            sb.append(String.format("%02d", time / 86400)).append(':')
        if (time >= 3600)
            sb.append(String.format("%02d", time / 3600 % 24)).append(':')
        if (time >= 60)
            sb.append(String.format("%02d", time / 60 % 60)).append(':')
        sb.append(String.format("%02d", time % 60))
        if (time < 60)
            sb.append(" s")
        return sb.toString()
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
        Constants.stats = null
        super.onStop()
    }

    fun updateStats(){
        findViewById<TextView>(R.id.puntCapturas).text = Constants.data.capturasExistosas.toString()
        findViewById<TextView>(R.id.puntEscapes).text = Constants.data.escapesNoEvitados.toString()
        findViewById<TextView>(R.id.puntQuesos).text = Constants.data.quesosRecogidos.toString()
        findViewById<TextView>(R.id.puntPuntuaciones).text = Constants.data.puntuacionTotal.toString()
        findViewById<TextView>(R.id.puntPuntosMax).text = Constants.data.puntuacionMaxima.toString()
        findViewById<TextView>(R.id.puntTiempo).text = intToTime(Constants.data.timePlayed)

        findViewById<TextView>(R.id.globalCapturas).text =
            (Constants.allData.values.map { it.capturasExistosas }.max() ?: 0).toString()
        findViewById<TextView>(R.id.globalEscapes).text =
            (Constants.allData.values.map { it.escapesNoEvitados }.max() ?: 0).toString()
        findViewById<TextView>(R.id.globalQuesos).text =
            (Constants.allData.values.map { it.quesosRecogidos }.max() ?: 0).toString()
        findViewById<TextView>(R.id.globalPuntuaciones).text =
            (Constants.allData.values.map { it.puntuacionTotal }.max() ?: 0).toString()
        findViewById<TextView>(R.id.globalPuntosMax).text =
            (Constants.allData.values.map { it.puntuacionMaxima }.max() ?: 0).toString()
        findViewById<TextView>(R.id.globalTiempo).text =
            intToTime(Constants.allData.values.map { it.timePlayed }.max() ?: 0)
    }
}
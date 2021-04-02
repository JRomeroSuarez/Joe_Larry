package com.example.joelerry


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.SeekBar
import android.widget.Switch
import com.example.joelerry.tutorial.Tutorial
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.config.*


class Config : AppCompatActivity() {

    private var mFirebaseAuth = FirebaseAuth.getInstance()!!
    private var mFirebaseUser = mFirebaseAuth.currentUser


    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.config)

        findViewById<SeekBar>(R.id.barVolMusica).also { it.progress = Constants.data.volumenMusica }
            .setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Constants.data.volumenMusica = progress
                    Constants.MUSIC.setVolume(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

        findViewById<SeekBar>(R.id.barVolSonido).also { it.progress = Constants.data.volumenSonido }
            .setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Constants.data.volumenSonido = progress
                    Constants.SOUNDS.setVolume(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })



        findViewById<Switch>(R.id.notifiaciones).also { it.isChecked = Constants.data.notifaciones }
            .setOnCheckedChangeListener { _, isChecked -> Constants.data.notifaciones = isChecked }



        //Para escribir el nombre del usuario en pantalla
        txtUsuario.text = mFirebaseUser?.email


        findViewById<Button>(R.id.btnTuto).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Tutorial::class.java
                ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
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
}
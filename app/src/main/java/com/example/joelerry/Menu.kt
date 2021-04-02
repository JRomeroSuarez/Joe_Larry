package com.example.joelerry

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.example.joelerry.nivell.SelLvls
import com.example.joelerry.tutorial.Tutorial
import com.google.firebase.auth.FirebaseAuth


class Menu : AppCompatActivity() {

    private var fbAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu)

        findViewById<Button>(R.id.btnJugar).setOnClickListener {
            if(!Constants.data.tutorialRealizado) {
                startActivityForResult(
                    Intent(
                        this,
                        Tutorial::class.java
                    ).also {
                        it.putExtra(
                            Constants.TAG_SELECT_LEVEL,
                            1
                        ); it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                    }, Constants.TUTORIAL_REQUEST_CODE)
                Constants.data.tutorialRealizado = true
                Data.writeData(this)
            } else {
                startActivity(
                    Intent(
                        this,
                        SelLvls::class.java
                    ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })

            }
        }
        findViewById<Button>(R.id.btnStats).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Stats::class.java
                ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
        }
        findViewById<Button>(R.id.btnConfig).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    Config::class.java
                ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
        }


        findViewById<Button>(R.id.btnLogOut).setOnClickListener {
            fbAuth.signOut()
        }
        fbAuth.addAuthStateListener {
            if(fbAuth.currentUser == null){
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.exit)
            .setTitle("Seguro que quieres salir?")
            .setPositiveButton("Si") { _, _ -> run { finishAffinity();Constants.MUSIC.pauseAudio(); Constants.SOUNDS.pauseAudio() } }
            .setNegativeButton("No", null)
            .show()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.TUTORIAL_REQUEST_CODE && resultCode == Activity.RESULT_OK)
            findViewById<Button>(R.id.btnJugar).callOnClick()
    }

}

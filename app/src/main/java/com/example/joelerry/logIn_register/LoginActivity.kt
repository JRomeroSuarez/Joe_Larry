package com.example.joelerry.logIn_register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.joelerry.Constants
import com.example.joelerry.Data
import com.example.joelerry.Menu
import com.example.joelerry.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUser: EditText
    private lateinit var txtPassword: EditText
    private lateinit var auth: FirebaseAuth

    companion object {
        const val TAG = "Login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.btnRegistrarse).setOnClickListener {
            startActivity(
                Intent(
                    this,
                    RegisterActivity::class.java
                ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) }
            )
        }
        findViewById<Button>(R.id.btnIniciar).setOnClickListener { loginUser() }

        txtUser = findViewById(R.id.txtUsuario)
        txtPassword = findViewById(R.id.txtPassword)

        auth = FirebaseAuth.getInstance()

        if (FirebaseAuth.getInstance().currentUser != null) {
            iniciarSesion()
        } else {
            Log.d(TAG, "ERROR")
        }
        if (intent.getBooleanExtra("LogIn", false)) {
            finish()
        }

    }

    private fun loginUser() {
        val user: String = txtUser.text.toString()
        val password: String = txtPassword.text.toString()

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {

            auth.signInWithEmailAndPassword(user, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        iniciarSesion()
                    } else {
                        Toast.makeText(this, "Error en el inicio de sesión", Toast.LENGTH_LONG).show()
                    }
                }
        }


    }

    private fun iniciarSesion() {
        Data.readData(this)

        FirebaseDatabase.getInstance().reference.child("Usuarios/").addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(Data::class.java)
                if (data != null) {
                    Constants.allData[snapshot.key!!] = data
                    Constants.stats?.updateStats()
                }

                if (previousChildName != null)
                    Constants.allData.remove(previousChildName)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val data = snapshot.getValue(Data::class.java)
                if (data != null)
                    Constants.allData[snapshot.key!!] = data
                Constants.stats?.updateStats()

                if (previousChildName != null)
                    Constants.allData.remove(previousChildName)

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Es crida nomes quan cambia la prioritat
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                Constants.allData.remove(snapshot.key)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DATA", "onCancelled", databaseError.toException())
            }
        }
        )

        startActivity(Intent(this, Menu::class.java).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
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

package com.example.joelerry.logIn_register

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.joelerry.Constants
import com.example.joelerry.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtApellido: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtConstrasenya: EditText
    private lateinit var dbReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        Constants.MUSIC.resumeAudio()
        Constants.SOUNDS.resumeAudio()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtName = findViewById(R.id.txtNom)
        txtApellido = findViewById(R.id.txtApellido)
        txtEmail = findViewById(R.id.txtCorreo)
        txtConstrasenya = findViewById(R.id.txtContraseña)

        findViewById<Button>(R.id.btnRegistrarse).setOnClickListener { createNewAccount() }


        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("User")


    }

    private fun createNewAccount() {
        val name: String = txtName.text.toString()
        val apellido: String = txtApellido.text.toString()
        val email: String = txtEmail.text.toString()
        val password: String = txtConstrasenya.text.toString()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(
                password
            )
        ) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.exception != null)
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    else if (task.isComplete) {
                        val user: FirebaseUser? = auth.currentUser
                        verifyEmail(user)

                        val userBD = dbReference.child(user?.uid!!)//Puede que las dos exclamaciones esten mal


                        userBD.child("Name").setValue(name)
                        userBD.child("Apellido").setValue(apellido)
                        startActivity(
                            Intent(
                                this,
                                LoginActivity::class.java
                            ).also { it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT) })
                    }

                }
        }


    }

    private fun verifyEmail(user: FirebaseUser?) {
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isComplete) {
                    Toast.makeText(this, "Email enviado", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Error al enviar el correo", Toast.LENGTH_LONG).show()
                }
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

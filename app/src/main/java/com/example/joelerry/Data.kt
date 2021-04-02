package com.example.joelerry

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.*


class Data {
    var correo = ""
    var volumenSonido = 50
    var volumenMusica = 75
    var idioma = 0
    var notifaciones = true
    var tutorialRealizado = false

    //Estadisticas para Stats
    var capturasExistosas = 0
    var escapesNoEvitados = 0
    var quesosRecogidos = 0
    var puntuacionMaxima = 0
    var puntuacionTotal = 0
    var timePlayed = 0

    companion object {
        private const val CONFIG_FILE = "config.txt"
        private const val TAG = "DATA"

        fun readData(context: Context) {
            readDataOnFirebase()
            val data = Data()

            data.correo = FirebaseAuth.getInstance().currentUser?.email ?: data.correo
            try {
                val inputStream = context.openFileInput(CONFIG_FILE)

                if (inputStream != null) {
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)

                    var values = bufferedReader.readLine()
                    if (values != null)
                        data.volumenSonido = values.toIntOrNull() ?: data.volumenSonido

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.volumenMusica = values.toIntOrNull() ?: data.volumenMusica

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.idioma = values.toIntOrNull() ?: data.idioma

                    values = bufferedReader.readLine()
                    if (values != null && (values == "true" || values == "false"))
                        data.notifaciones = values.toBoolean()

                    values = bufferedReader.readLine()
                    if (values != null && (values == "true" || values == "false"))
                        data.tutorialRealizado = values.toBoolean()

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.capturasExistosas = values.toIntOrNull() ?: data.capturasExistosas

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.escapesNoEvitados = values.toIntOrNull() ?: data.escapesNoEvitados

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.quesosRecogidos = values.toIntOrNull() ?: data.quesosRecogidos

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.puntuacionMaxima = values.toIntOrNull() ?: data.puntuacionMaxima

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.puntuacionTotal = values.toIntOrNull() ?: data.puntuacionTotal

                    values = bufferedReader.readLine()
                    if (values != null)
                        data.timePlayed = values.toIntOrNull() ?: data.timePlayed

                    inputStream.close()
                }
            } catch (e: FileNotFoundException) {
                Log.e(TAG, "File not found: $e")
            } catch (e: IOException) {
                Log.e(TAG, "Can not read file: $e")
            }
            Constants.data = data
        }


        fun writeData(context: Context) {
            try {
                val outputStreamWriter = OutputStreamWriter(context.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE))
                outputStreamWriter.write("${Constants.data.volumenSonido}\n")
                outputStreamWriter.write("${Constants.data.volumenMusica}\n")
                outputStreamWriter.write("${Constants.data.idioma}\n")
                outputStreamWriter.write("${Constants.data.notifaciones}\n")
                outputStreamWriter.write("${Constants.data.tutorialRealizado}\n")

                outputStreamWriter.write("${Constants.data.capturasExistosas}\n")
                outputStreamWriter.write("${Constants.data.escapesNoEvitados}\n")
                outputStreamWriter.write("${Constants.data.quesosRecogidos}\n")
                outputStreamWriter.write("${Constants.data.puntuacionMaxima}\n")
                outputStreamWriter.write("${Constants.data.puntuacionTotal}\n")
                outputStreamWriter.write("${Constants.data.timePlayed}\n")
                outputStreamWriter.close()
            } catch (e: IOException) {
                Log.e(TAG, "File write failed: $e")
            }
            Constants.data.saveDataOnFirebase()
        }

        private fun readDataOnFirebase() {
            if (FirebaseAuth.getInstance()!!.currentUser != null) {
                FirebaseDatabase.getInstance().reference.child("Usuarios/")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            Constants.data = dataSnapshot.getValue(Data::class.java) ?: Constants.data
                            Constants.data.correo =
                                FirebaseAuth.getInstance().currentUser?.email ?: Constants.data.correo
                            Constants.data.updateData()
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException())
                        }
                    }
                    )
                FirebaseDatabase.getInstance().reference.child("Usuarios/")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            var data: Data?
                            for (value in dataSnapshot.children) {
                                data = value.getValue(Data::class.java)
                                if (data != null)
                                    Constants.allData[value.key!!] = data
                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException())
                        }
                    }
                    )
            }
        }
    }

    fun clearData(context: Context) {
        Constants.data = Data()
        File(context.filesDir, CONFIG_FILE).delete()
        saveDataOnFirebase()
    }

    fun saveDataOnFirebase() {
        if (FirebaseAuth.getInstance()!!.currentUser != null)
            FirebaseDatabase.getInstance().reference.child("Usuarios/").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(
                this
            )
    }

    fun updateData() {
        Constants.MUSIC.setVolume(volumenMusica)
        Constants.SOUNDS.setVolume(volumenSonido)
    }

}
package com.example.joelerry.game

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.joelerry.Constants
import com.example.joelerry.Facade
import com.example.joelerry.Pausa


class GameView(context: Context, level: Int) : SurfaceView(context), SurfaceHolder.Callback {
    private lateinit var thread: GameThread
    private var facade = Facade(context, level)

    companion object {
        const val TAG = "GameView"
    }

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread = GameThread(super.getHolder(), this)
        thread.running = true
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        var retry = true
        while (retry) {
            try {
                thread.running = false
                thread.join()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
        Constants.MUSIC.stopAudio()
        Constants.MUSIC.selectAudio(context,"Intro")
        Constants.MUSIC.bucleAudio()
    }
    /*Sobreescribimos el onTouchEvent por defecto en la vista para poder acceder a los atributos
    que nos dan la posición x e y con touchX y touchY
     */
    override fun onTouchEvent(event: MotionEvent):Boolean{
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (Pausa.isClickOnPauseButton(event.x, event.y)) {
                openPauseMenu()
                return true
            }
            facade.updateJoeDirection(event.x, event.y)
            return true
        }
        return false
    }

    /*Llamamos a actualizar el movimiento del personaje*/
    fun update() {
        facade.update()
    }


    //Metodo que llama el thread para actualizar graficamente la pantalla
    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null) {
            facade.drawToolBar(canvas)
            facade.drawMap(canvas)
            facade.drawCharacters(canvas)
        }
    }

    //Metodo que pausa el juego y pone el menu de de ingame
    private fun openPauseMenu() {
        setPause(true)
        val bundle = Bundle()
        Pausa(
            context,
            bundle
        ).also {
            it.setOnDismissListener { closePauseMenu(bundle) }; it.setCanceledOnTouchOutside(false)
        }.show()
    }

    //Metodo auxiliar que se llama al cerrar el menu
    private fun closePauseMenu(bundle: Bundle) {
        gestioOption(
            bundle.getSerializable(Constants.MENU_PAUSA) as Constants.Companion.InGameOptions?
                ?: Constants.Companion.InGameOptions.Resume
        )
        setPause(false)
    }

    //Gestiona la opcion pasada por parametro para que haga lo que especifica
    fun gestioOption(inGameOptions: Constants.Companion.InGameOptions?) {
        when (inGameOptions) {
            Constants.Companion.InGameOptions.SelectLevel -> (context as Activity).finish()
            Constants.Companion.InGameOptions.Next -> facade.runLevel(facade.level + 1)
            Constants.Companion.InGameOptions.Retry -> facade.runLevel(facade.level)
            Constants.Companion.InGameOptions.Resume -> setPause(false)
            else -> Log.e(TAG, "Valor de inGameOptions inesperat")
        }
    }

    private fun setPause(state: Boolean) {
        thread.pause = state
        facade.pause = state
    }

}





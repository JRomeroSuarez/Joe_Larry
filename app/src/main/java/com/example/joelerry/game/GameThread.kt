package com.example.joelerry.game

import android.annotation.TargetApi
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.view.SurfaceHolder
import com.example.joelerry.Constants

class GameThread(private var surfaceHolder: SurfaceHolder, private var gameView: GameView) : Thread() {
    var running = false
    var pause = false

    companion object {
        private var canvas: Canvas? = null
        private const val TAG = "GameThread"
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun run() {
        var startTime : Long
        var timeMillis : Long
        var framesSkipped: Int
        var waitTime: Long
        var sumMillis = 0L
        var cicles = 0

        Log.d(TAG, "Inici del game thread")

        while (running){

            canvas = null
            framesSkipped = 0
            startTime = System.nanoTime()
            waitTime = 0

            try{
                if (!pause)
                    gameView.update()
                canvas = surfaceHolder.lockCanvas()
                synchronized(surfaceHolder){
                    gameView.draw(canvas)
                }

            }catch (e: Exception){
                e.printStackTrace()
            }finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }
            timeMillis = (System.nanoTime() - startTime)/ 1000000
            sumMillis += timeMillis
            waitTime += Constants.FRAME_TARGET - timeMillis


            if (waitTime >= 0) {
                try {
                    sleep(waitTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                //Bucle que recupera frames a partir de omitir la actualicacion grafica
                while (waitTime < 0 && framesSkipped++ < Constants.MAX_DROPPED_FRAMES && !pause) {
                    //gameView.update()
                    waitTime += Constants.FRAME_TARGET
                }
               Log.d(TAG, "Se han dropeado $framesSkipped frames")
            }
            if (timeMillis != 0L)
               Log.d(TAG, "FPS=${1000 / timeMillis} AV.FPS=${1000 * ++cicles / sumMillis}")
        }
    }
}

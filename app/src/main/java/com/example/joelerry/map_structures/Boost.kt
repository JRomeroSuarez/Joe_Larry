package com.example.joelerry.map_structures

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import com.example.joelerry.Constants
import com.example.joelerry.R
import com.example.joelerry.pj.Actor
import java.util.*
import kotlin.math.roundToInt

abstract class Boost(resources: Resources, bitmap: Bitmap) : Block(bitmap) {

    companion object {
        protected const val TAG = "BOOST"
    }

    private var bitmapPath = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.camino),
        Constants.SCREEN_WIDTH_BLOCK.roundToInt(),
        Constants.SCREEN_HEIGHT_BLOCK.roundToInt(),
        true
    )

    override fun isCorossalbe(actor: Actor): Boolean {
        return true
    }

    abstract fun eat(actor: Actor)

    protected fun eatAuxilar(actor: Actor, velocity: Float, delay: Long) {
        Timer().schedule(object : TimerTask() {
            private val last = actor.velocity

            override fun run() {
                actor.velocity = last
                Log.d(TAG, "$actor recupero la velocidad de $last bloques/s")
            }
        }, delay)
        actor.velocity = velocity
        Log.d(TAG, "$actor cogio el boost de velocidad $velocity bloques/s")
    }

    override fun draw(canvas: Canvas, x: Int, y: Int) {
        canvas.drawBitmap(
            bitmapPath,
            x * Constants.SCREEN_WIDTH_BLOCK,
            y * Constants.SCREEN_HEIGHT_BLOCK + Constants.SCREEN_HEIGHT_TOOLBAR,
            null
        )
        super.draw(canvas, x, y)
    }
}
package com.example.joelerry.map_structures

import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.joelerry.Constants
import com.example.joelerry.pj.Actor
import kotlin.math.roundToInt

abstract class Block(image: Bitmap) {

    private val image =
        Bitmap.createScaledBitmap(
            image,
            Constants.SCREEN_WIDTH_BLOCK.roundToInt(),
            Constants.SCREEN_HEIGHT_BLOCK.roundToInt(),
            true
        )

    open fun draw(canvas: Canvas, x: Int, y: Int) {
        canvas.drawBitmap(
            image,
            x * Constants.SCREEN_WIDTH_BLOCK,
            y * Constants.SCREEN_HEIGHT_BLOCK + Constants.SCREEN_HEIGHT_TOOLBAR,
            null
        )
    }

    abstract fun isCorossalbe(actor: Actor): Boolean

    abstract fun isEatable(actor: Actor): Boolean
}

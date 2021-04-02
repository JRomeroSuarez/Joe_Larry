package com.example.joelerry.map_structures

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.example.joelerry.Constants
import com.example.joelerry.R
import com.example.joelerry.pj.Actor
import kotlin.math.roundToInt

//TODO: Generar imagen en funcion del valor del queso
class Cheese(resources: Resources, val valor: Int) : Block(BitmapFactory.decodeResource(resources, R.drawable.camino)) {

    private var color = Paint().also { it.color = resources.getColor(R.color.negro); it.textSize = 50f; }
    private var bitmapPath = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(resources, R.drawable.camino),
        Constants.SCREEN_WIDTH_BLOCK.roundToInt(),
        Constants.SCREEN_HEIGHT_BLOCK.roundToInt(),
        true
    )

    override fun draw(canvas: Canvas, x: Int, y: Int) {
        canvas.drawBitmap(
            bitmapPath,
            x * Constants.SCREEN_WIDTH_BLOCK,
            y * Constants.SCREEN_HEIGHT_BLOCK + Constants.SCREEN_HEIGHT_TOOLBAR,
            null
        )
        canvas.drawText(
            valor.toString(),
            (x + 0.5f) * Constants.SCREEN_WIDTH_BLOCK,
            (y + 0.5f) * Constants.SCREEN_HEIGHT_BLOCK + Constants.SCREEN_HEIGHT_TOOLBAR,
            color
        )
    }

    override fun toString(): String {
        return valor.toString()
    }

    override fun isCorossalbe(actor: Actor): Boolean {
        return true
    }

    override fun isEatable(actor: Actor): Boolean {
        return false
    }
}
package com.example.joelerry.pj

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import com.example.joelerry.Constants
import com.example.joelerry.map_structures.Block
import com.example.joelerry.map_structures.Boost
import com.example.joelerry.map_structures.Mapa
import kotlin.math.roundToInt

abstract class Actor(image: Bitmap, var x: Float = 0f, var y: Float = 0f) {
    private var image = Bitmap.createScaledBitmap(
        image,
        Constants.SCREEN_WIDTH_BLOCK.roundToInt(),
        Constants.SCREEN_HEIGHT_BLOCK.roundToInt(),
        false
    )
    var velocity = 1f //Bloques por segundo
    var direction = Constants.Companion.Direction.None


    open fun updateMovement(mapa: Mapa, resources: Resources) {
        val newX = x + direction.x * velocity / Constants.TARGET_FPS
        val newY = y + direction.y * velocity / Constants.TARGET_FPS
        if (checkColisions(mapa, newX, newY))
            direction = Constants.Companion.Direction.None
        else if (direction != Constants.Companion.Direction.None) {
            setCoord(newX, newY)
            val actualBlock = getBlock(mapa, newX + direction.x / 2f + 0.5f, newY + direction.y / 2f + 0.5f)
            if (actualBlock != null && actualBlock.isEatable(this)) {
                (actualBlock as Boost).eat(this)
                mapa.removeEatable(
                    (newX + direction.x / 2f + 0.5f).toInt(),
                    (newY + direction.y / 2f + 0.5f).toInt(),
                    resources
                )
            }

        }

    }

    private fun checkColisions(mapa: Mapa, newX: Float, newY: Float): Boolean {
        return newX !in 0f..(Constants.MAP_SIZE - 1f) || newY !in 0f..(Constants.MAP_SIZE - 1f) ||
                !(getBlock(mapa, newX + direction.x / 2f + 0.5f, newY + direction.y / 2f + 0.5f)?.isCorossalbe(this)
                    ?: true)

    }

    private fun getBlock(mapa: Mapa, x: Float, y: Float): Block? {
        return if (x.toInt() in (0 until Constants.MAP_SIZE) && y.toInt() in (0 until Constants.MAP_SIZE)) mapa.bloques[y.toInt()][x.toInt()] else null
    }


    fun setCoord(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    override fun toString(): String {
        return "($x,$y)"
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(
            image,
            x * Constants.SCREEN_WIDTH_BLOCK,
            y * Constants.SCREEN_HEIGHT_BLOCK + Constants.SCREEN_HEIGHT_TOOLBAR,
            null
        )
    }

    open fun reset() {
        velocity = 1f
        direction = Constants.Companion.Direction.None
        x = 0f
        y = 0f
    }

}

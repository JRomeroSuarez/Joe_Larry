package com.example.joelerry

import android.content.res.Resources.getSystem
import com.example.joelerry.game.GameAudio
import com.example.joelerry.operacions.Division
import com.example.joelerry.operacions.Minus
import com.example.joelerry.operacions.Product
import com.example.joelerry.operacions.Sum
import kotlin.math.abs

class Constants {
    companion object {
        val N_OF_CHEESE_INTERVAL = 5..10
        val N_OF_BOOST_INTERVAL = 2..10
        val CHEESE_VALUES_INTERVAL = 2..25
        val MUSIC = GameAudio()
        val SOUNDS = GameAudio()
        const val MAP_SIZE = 16
        const val MAX_N_OF_LEVELS = 9
        val SCREEN_HEIGHT = getSystem().displayMetrics.heightPixels
        val SCREEN_WIDTH = getSystem().displayMetrics.widthPixels
        val SCREEN_HEIGHT_TOOLBAR = SCREEN_HEIGHT.toFloat() / 15
        val SCREEN_WIDTH_BLOCK = SCREEN_WIDTH.toFloat() / MAP_SIZE
        val SCREEN_HEIGHT_BLOCK = (SCREEN_HEIGHT.toFloat() - SCREEN_HEIGHT_TOOLBAR) / MAP_SIZE
        const val MAX_DROPPED_FRAMES = 5
        const val TARGET_FPS = 24 //FPS, el ratio en el qual refrescaremos el canvas
        const val FRAME_TARGET = (1000 / TARGET_FPS)
        const val TAG_SELECT_LEVEL = "com.example.joelerry.nivell.TAG_SELECT_LEVEL"
        const val NIVELL_REQUEST_CODE = 0
        const val TUTORIAL_REQUEST_CODE =1


        enum class InGameOptions {
            Retry, Next, SelectLevel, Resume
        }

        enum class Direction(val x: Float, val y: Float) {
            None(0f, 0f), Down(0f, 1f), Right(1f, 0f), Up(0f, -1f), Left(-1f, 0f);

            companion object {
                fun getDirection(dx: Float, dy: Float): Direction {
                    if (dx == 0f && dy == 0f)
                        return None
                    if (abs(dx) <= abs(dy)) {
                        if (dy < 0)
                            return Up
                        return Down
                    }
                    if (dx < 0)
                        return Left
                    return Right
                }
            }
        }

        const val TAG_ENDGAME = "com.example.joelerry.nivell.TAG_ENDGAME"
        const val TAG_WIN_FRAGMENT = "com.example.joelerry.nivell.TAG_WIN_FRAGMENT"
        const val MENU_PAUSA = "com.example.joelerry.MENU_PAUSA"
        const val TAG_PUNTUACIO = "com.example.joelerry.TAG_PUNTUACIO"

        val OPERACIONS_PER_NIVELL = mapOf(
            Pair(Sum(), arrayOf(1, 3, 6, 9)),
            Pair(Minus(), arrayOf(2, 3, 6, 9)),
            Pair(Product(), arrayOf(4, 5, 6, 8, 9)),
            Pair(Division(), arrayOf(7, 8, 9))
        )

        lateinit var data: Data
        var allData = HashMap<String, Data>()
        var stats: Stats? = null
    }
}
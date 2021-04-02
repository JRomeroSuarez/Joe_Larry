package com.example.joelerry

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.Window
import android.widget.Button


class Pausa(var parent: Context, private var returnBundle: Bundle) : Dialog(parent) {

    companion object {
        fun isClickOnPauseButton(x: Float, y: Float): Boolean {
            return x in (Constants.SCREEN_WIDTH - Constants.SCREEN_HEIGHT_TOOLBAR)..Constants.SCREEN_WIDTH.toFloat()
                    && y in 0f..Constants.SCREEN_HEIGHT_TOOLBAR
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.menu_pausa)

        findViewById<Button>(R.id.btnContinuar).setOnClickListener { returnValue(Constants.Companion.InGameOptions.Resume) }
        findViewById<Button>(R.id.btnPausaReiniciar).setOnClickListener { returnValue(Constants.Companion.InGameOptions.Retry) }
        findViewById<Button>(R.id.btnVolverMenu).setOnClickListener { returnValue(Constants.Companion.InGameOptions.SelectLevel) }
    }

    override fun onBackPressed() {
        returnValue(Constants.Companion.InGameOptions.SelectLevel)
    }

    private fun returnValue(inGameOptions: Constants.Companion.InGameOptions) {
        returnBundle.putSerializable(
            Constants.MENU_PAUSA,
            inGameOptions
        )
        dismiss()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isClickOnPauseButton(
                event.x + (Constants.SCREEN_WIDTH - (window?.decorView?.width ?: 0)) / 2f,
                event.y + (Constants.SCREEN_HEIGHT - (window?.decorView?.height ?: 0)) / 2f
            )
        )
            returnValue(Constants.Companion.InGameOptions.Resume)
        return super.onTouchEvent(event)
    }
}
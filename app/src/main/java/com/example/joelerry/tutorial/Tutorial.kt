package com.example.joelerry.tutorial

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import com.example.joelerry.Constants
import com.example.joelerry.R

class Tutorial : AppCompatActivity() {

    private val listTutorialLayouts = listOf(
        Pair(R.layout.activity_tutorial1, "Tutorial1"),
        Pair(R.layout.activity_tutorial2, "Tutorial2"),
        Pair(R.layout.activity_tutorial3, "Tutorial3")
    )
    private var indexTutorialList = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(listTutorialLayouts[indexTutorialList].first)
        Constants.SOUNDS.stopAudio()

        Constants.SOUNDS.selectAudio(this, listTutorialLayouts[indexTutorialList].second)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN){
            if (++indexTutorialList < listTutorialLayouts.size) {
                setContentView(listTutorialLayouts[indexTutorialList].first)
                Constants.SOUNDS.stopAudio()

                Constants.SOUNDS.selectAudio(this, listTutorialLayouts[indexTutorialList].second)
            } else {
                setResult(Activity.RESULT_OK)
                finish()
                Constants.SOUNDS.stopAudio()

            }
        }
        return super.onTouchEvent(event)
    }
}


package com.example.joelerry.pj

import android.content.res.Resources
import android.graphics.BitmapFactory


class Joe(resources: Resources) : Actor(BitmapFactory.decodeResource(resources, com.example.joelerry.R.drawable.gato_juego)) {
  
    override fun reset() {
        super.reset()
        velocity = 1.5f
    }
}

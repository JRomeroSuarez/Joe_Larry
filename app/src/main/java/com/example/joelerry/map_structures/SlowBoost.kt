package com.example.joelerry.map_structures

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.joelerry.R
import com.example.joelerry.pj.Actor
import com.example.joelerry.pj.Larry

class SlowBoost(resources: Resources) :
    Boost(resources, BitmapFactory.decodeResource(resources, R.drawable.comida_raton)) {
    override fun eat(actor: Actor) {
        eatAuxilar(actor, 1.5f, 5000)
    }

    override fun isEatable(actor: Actor): Boolean {
        return actor is Larry
    }
}

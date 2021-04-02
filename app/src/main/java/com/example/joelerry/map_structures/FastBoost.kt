package com.example.joelerry.map_structures

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.joelerry.R
import com.example.joelerry.pj.Actor
import com.example.joelerry.pj.Joe

class FastBoost(resources: Resources) :
    Boost(resources, BitmapFactory.decodeResource(resources, R.drawable.comida_gato)) {

    override fun eat(actor: Actor) {
        eatAuxilar(actor, 3f, 5000)
    }

    override fun isEatable(actor: Actor): Boolean {
        return actor is Joe
    }
}

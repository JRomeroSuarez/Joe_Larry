package com.example.joelerry.map_structures

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.joelerry.R
import com.example.joelerry.pj.Actor

class Wall private constructor(resources: Resources) :
    Block(BitmapFactory.decodeResource(resources, R.drawable.pared)) {

    companion object {
        private var instace: Wall? = null
        fun getInstace(resources: Resources): Wall {
            if (instace == null) {
                synchronized(Wall) {
                    if (instace == null)
                        instace = Wall(resources)
                }
            }
            return instace!!
        }
    }

    override fun toString(): String {
        return "W"
    }

    override fun isCorossalbe(actor: Actor): Boolean {
        return false
    }

    override fun isEatable(actor: Actor): Boolean {
        return false
    }
}
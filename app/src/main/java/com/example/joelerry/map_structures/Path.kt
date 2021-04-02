package com.example.joelerry.map_structures


import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.joelerry.R
import com.example.joelerry.pj.Actor


class Path private constructor(resources: Resources) :
    Block(BitmapFactory.decodeResource(resources, R.drawable.camino)) {

    companion object {
        private var instace: Path? = null
        fun getInstace(resources: Resources): Path {
            if (instace == null) {
                synchronized(Path) {
                    if (instace == null)
                        instace = Path(resources)
                }
            }
            return instace!!
        }
    }

    override fun toString(): String {
        return "p"
    }

    override fun isCorossalbe(actor: Actor): Boolean {
        return true
    }

    override fun isEatable(actor: Actor): Boolean {
        return false
    }

}



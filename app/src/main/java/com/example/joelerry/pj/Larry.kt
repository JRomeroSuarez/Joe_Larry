package com.example.joelerry.pj

import android.content.res.Resources
import android.graphics.BitmapFactory
import com.example.joelerry.Constants
import com.example.joelerry.R
import com.example.joelerry.map_structures.Mapa
import java.util.*


class Larry(resources: Resources) : Actor(BitmapFactory.decodeResource(resources, R.drawable.larry)) {

    private val path = Stack<Pair<Int, Int>>()
    fun needsPath() = path.isEmpty()

    override fun updateMovement(mapa: Mapa, resources: Resources) {
        if (!needsPath()) {
            if (path.peek().first - x in -(1.1f * velocity / Constants.TARGET_FPS)..(1.1f * velocity / Constants.TARGET_FPS) && path.peek().second - y in -(1.1f * velocity / Constants.TARGET_FPS)..(1.1f * velocity / Constants.TARGET_FPS)) {
                val pos = path.pop()
                setCoord(pos.first.toFloat(), pos.second.toFloat())
                direction = Constants.Companion.Direction.None
            }
            if (direction == Constants.Companion.Direction.None && !needsPath())
                direction = Constants.Companion.Direction.getDirection(path.peek().first - x, path.peek().second - y)
            super.updateMovement(mapa, resources)
        }

    }

    fun setPath(list: List<Pair<Int, Int>>) {
        path.addAll(list.reversed())
    }

    override fun reset() {
        super.reset()
        velocity = 3f
        path.clear()
    }

}
package com.example.joelerry.ia

import com.example.joelerry.Constants
import com.example.joelerry.map_structures.Block
import com.example.joelerry.pj.Actor
import java.lang.Math.abs

class Grid(private val mapa: Array<Array<Block>>, private val actor: Actor) {

    companion object {
        const val MAX_SCORE = 99999999
        private val validMoves = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
    }

    /* Mètode que calcula la distància heurística*/
    fun heuristicDistance(start: GridPosition, finish: GridPosition): Int {
        val dx = abs(start.first - finish.first)
        val dy = abs(start.second - finish.second)
        return (dx + dy) + (-2) * minOf(dx, dy)
    }

    private fun inBarrier(position: GridPosition) = !mapa[position.second][position.first].isCorossalbe(actor)

    fun getNeighbours(position: GridPosition): List<GridPosition> = validMoves.map {
        GridPosition(position.first + it.first, position.second + it.second) }
        .filter { inGrid(it) }

    private fun inGrid(it: GridPosition) =
        (it.first in 0 until Constants.MAP_SIZE) && (it.second in 0 until Constants.MAP_SIZE)

    fun moveCost(to: GridPosition) = if (inBarrier(to)) MAX_SCORE else 1

}

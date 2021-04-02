package com.example.joelerry.ia

typealias GridPosition = Pair<Int, Int> //Node

class Behavior {

    /**
     * Implementation of the A* Search Algorithm to find the optimum path between 2 points on a grid.
     */

    fun aStarSearch(start: GridPosition, finish: GridPosition, grid: Grid): List<GridPosition> {

        /**
         * Use the cameFrom values to Backtrack to the start position to generate the path
         */
        fun generatePath(currentPos: GridPosition, cameFrom: Map<GridPosition, GridPosition>): List<GridPosition> {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            return path.toList()
        }

        val openVertices = mutableSetOf(start)
        val closedVertices = mutableSetOf<GridPosition>()
        val costFromStart = mutableMapOf(start to 0)
        val estimatedTotalCost = mutableMapOf(start to grid.heuristicDistance(start, finish))

        val cameFrom = mutableMapOf<GridPosition, GridPosition>()  // Used to generate path by back tracking

        while (openVertices.size > 0) {

            val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }!!

            // Check if we have reached the finish
            if (currentPos == finish) {
                // Backtrack to generate the most efficient path
                /* Retormen path, que equival a una llista de parells de enters. */
                return generatePath(currentPos, cameFrom)// First Route to finish will be optimum route
            }

            // Mark the current vertex as closed
            openVertices.remove(currentPos)
            closedVertices.add(currentPos)

            grid.getNeighbours(currentPos)
                .filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
                .forEach { neighbour ->
                    val score = costFromStart.getValue(currentPos) + grid.moveCost(neighbour)
                    if (score < costFromStart[neighbour] ?: Grid.MAX_SCORE) {
                        if (!openVertices.contains(neighbour)) {
                            openVertices.add(neighbour)
                        }
                        cameFrom[neighbour] = currentPos
                        costFromStart[neighbour] = score
                        estimatedTotalCost[neighbour] = score + grid.heuristicDistance(neighbour, finish)
                    }
                }

        }
        //throw IllegalArgumentException("No Path from Start $start to Finish $finish")
        return emptyList()
    }
}
package com.example.joelerry.map_structures

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.util.Log
import com.example.joelerry.Constants
import com.example.joelerry.ia.Grid
import com.example.joelerry.pj.Joe
import com.example.joelerry.pj.Larry
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class Mapa {

    lateinit var bloques: Array<Array<Block>>
    //funcionalitats IA
    lateinit var grid: Grid

    companion object {
        private const val TAG_GENERACIOPJ = "Generacio Persontages"
        private const val TAG_MAP = "Mapa"
    }


    /**
     * Metodo que genera un mapa en funcion de el tamaño de size y de la seed. Este mapa contiene una copia simetrica a
     * 4 lados donde no hay ni caminos sin salida (exceptuando los que sea de 1 de tamaño) y no se generar acumulaciones
     * de mas de 4 paths juntos (exceptuando a veces el centro de la pantalla)
     *
     */
    fun generateMap(context: Context, seed: Long = Random.nextLong()) {
        val random = Random(seed)
        val stack = Stack<Pair<Int, Int>>()
        var node: Pair<Int, Int>
        var nodePrev: Pair<Int, Int>
        var next: Pair<Int, Int>
        val nextNode = ArrayList<Pair<Int, Int>>()
        val prev = Array(Constants.MAP_SIZE / 2) { Array<Pair<Int, Int>?>(Constants.MAP_SIZE / 2) { null } }
        var eliminar = true
        bloques = Array<Array<Block>>(Constants.MAP_SIZE) { Array(Constants.MAP_SIZE) { Wall.getInstace(context.resources) } }



        //Generacio de les portes de forma aleatoria
        var startPos = (0 until Constants.MAP_SIZE / 2).random(random)
        if (startPos == Constants.MAP_SIZE / 2 - 1) {
            bloques[startPos][Constants.MAP_SIZE / 2 - 1] = Path.getInstace(context.resources)
            bloques[Constants.MAP_SIZE / 2 - 1][startPos] = Path.getInstace(context.resources)
            startPos--
        }

        bloques[startPos][Constants.MAP_SIZE / 2 - 1] = Path.getInstace(context.resources)
        bloques[Constants.MAP_SIZE / 2 - 1][startPos] = Path.getInstace(context.resources)
        stack.push(Pair(Constants.MAP_SIZE / 2 - 1, startPos))
        nodePrev = Pair(Constants.MAP_SIZE / 2 - 1, startPos)

        //Algoritme dfs modificat per generar el mapa sense crear camins sense sortida de mes de 1 de distancia
        while (!stack.isEmpty()) {
            node = stack.pop()
            nextNode.clear()
            for (i in arrayOf(Pair(-1, 0), Pair(0, -1), Pair(1, 0), Pair(0, 1))) {
                if (!(node.first + i.first == nodePrev.first && node.second + i.second == nodePrev.second) && insideFirstSquare(
                        node,
                        i,
                        Constants.MAP_SIZE / 2
                    ) && bloques[node.first + i.first][node.second + i.second] is Path
                )
                    eliminar = false
                if (checkIsValidNeighbour(node, i, prev)) {
                    nextNode.add(Pair(node.first + i.first, node.second + i.second))
                    eliminar = true
                }
            }
            if (nextNode.isNotEmpty()) {
                next = nextNode.random(random)
                prev[next.first][next.second] = node
                stack.push(next)
                bloques[next.first][next.second] = Path.getInstace(context.resources)
            } else {
                if (prev[node.first][node.second] != null) {
                    stack.push(prev[node.first][node.second])
                    if (eliminar)
                        bloques[node.first][node.second] = Wall.getInstace(context.resources)
                }
            }
            nodePrev = node
        }

        //Assegurem que tots els camins estiguin conctats
        var dif = 1
        while (++dif <= Constants.MAP_SIZE / 2 && bloques[startPos][Constants.MAP_SIZE / 2 - dif] !is Path)
            bloques[startPos][Constants.MAP_SIZE / 2 - dif] = Path.getInstace(context.resources)

        //Copia del mapa de forma simetrica a les altres 3 parts
        for (i in 0 until Constants.MAP_SIZE / 2) for (j in 0 until Constants.MAP_SIZE / 2) {
            bloques[Constants.MAP_SIZE - 1 - i][j] = bloques[i][j]
            bloques[i][Constants.MAP_SIZE - 1 - j] = bloques[i][j]
            bloques[Constants.MAP_SIZE - 1 - i][Constants.MAP_SIZE - 1 - j] = bloques[i][j]
        }
        printMapa()
    }

    fun generateBoost(context: Context, seed: Long = Random.nextLong()) {
        var nBoost = Constants.N_OF_BOOST_INTERVAL.random()
        val random = Random(seed)

        while (0 < nBoost) {
            val x = (0 until Constants.MAP_SIZE).random(random)
            val y = (0 until Constants.MAP_SIZE).random(random)
            if (bloques[y][x] is Path) {
                bloques[y][x] = if (random.nextBoolean()) FastBoost(context.resources) else SlowBoost(context.resources)
                nBoost--
            }

        }
    }

    fun generateCheese(
        context: Context,
        seed: Long = Random.nextLong()
    ): Map<Cheese, Pair<Int, Int>> {
        val cheeses = HashMap<Cheese, Pair<Int, Int>>()
        val random = Random(seed)
        val nCheeses = Constants.N_OF_CHEESE_INTERVAL.random(random)
        val possibleValues = Constants.CHEESE_VALUES_INTERVAL.toMutableList()

        while (cheeses.size < nCheeses) {
            val x = (0 until Constants.MAP_SIZE).random(random)
            val y = (0 until Constants.MAP_SIZE).random(random)
            if (bloques[y][x] is Path) {
                val value = possibleValues.random(random)
                possibleValues.remove(value)
                val cheese = Cheese(context.resources, value)
                cheeses[cheese] = Pair(x, y)
                bloques[y][x] = cheese
            }

        }
        printMapa()
        Log.d("Quesos [valor, pos()]!", cheeses.toString())
        return cheeses

    }

    //Metode auxilar que comproba si un node te un fill valid en al direccio i.
    private fun checkIsValidNeighbour(
        node: Pair<Int, Int>,
        i: Pair<Int, Int>,
        prev: Array<Array<Pair<Int, Int>?>>
    ): Boolean {
        return insideFirstSquare(node, i, Constants.MAP_SIZE / 2 - 1)
                && prev[node.first + i.first][node.second + i.second] == null
                && !(insideFirstSquare(node, Pair(i.second, i.first), Constants.MAP_SIZE / 2 - 1) && insideFirstSquare(
            node,
            Pair(i.second + i.first, i.second + i.first),
            Constants.MAP_SIZE / 2 - 1
        )
                && bloques[node.first + i.second][node.second + i.first] is Path && bloques[node.first + i.second + i.first][node.second + i.second + i.first] is Path)
                && !(insideFirstSquare(
            node,
            Pair(-i.second, -i.first),
            Constants.MAP_SIZE / 2 - 1
        ) && insideFirstSquare(
            node,
            Pair(i.first - i.second, i.second - i.first),
            Constants.MAP_SIZE / 2 - 1
        )
                && bloques[node.first - i.second][node.second - i.first] is Path && bloques[node.first + i.first - i.second][node.second + i.second - i.first] is Path)
    }

    //Metode auxilar per poder comprobar si el node mes la direccio i es troba en el rang de 0 a max-1
    private fun insideFirstSquare(node: Pair<Int, Int>, i: Pair<Int, Int>, max: Int): Boolean {
        return node.first + i.first in 0 until max && node.second + i.second in 0 until max
    }

    //Metode auxilar per imprimir el mapa
    private fun printMapa() {
        val str = StringBuilder(" \n")
        for (i in bloques) {
            for (j in i) {
                str.append("$j ")
            }
            str.append("\n")
        }
        Log.d(TAG_MAP, str.toString())
    }

    /**
     * Inicializa los dos actores en posciones opuestas priorizando las casillas centrales de cada subcuadrado
     */
    fun initActores(joe: Joe, larry: Larry){
        joe.reset()
        larry.reset()
        grid = Grid(bloques, larry)
        val posInt = Pair(Constants.MAP_SIZE / 4, Constants.MAP_SIZE / 4)
        var size = -1
        while (++size < Constants.MAP_SIZE / 2) {
            for (i in -size..size) for (j in -size..size) {
                if (insideFirstSquare(
                        posInt,
                        Pair(i, j),
                        Constants.MAP_SIZE / 2
                    ) && bloques[posInt.second + j][posInt.first + i] is Path
                    && bloques[Constants.MAP_SIZE - (posInt.second + j + 1)][posInt.first + i] is Path
                    && bloques[posInt.second + j][Constants.MAP_SIZE - (posInt.first + i + 1)] is Path
                    && bloques[Constants.MAP_SIZE - (posInt.second + j + 1)][Constants.MAP_SIZE - (posInt.first + i + 1)] is Path
                ) {
                    val esquinaJoe = (0..3).random()
                    val x = posInt.first + i
                    val y = posInt.second + j
                    joe.setCoord(
                        (if (esquinaJoe / 2 == 0) x else Constants.MAP_SIZE - x - 1).toFloat(),
                        (if (esquinaJoe % 2 == 0) y else Constants.MAP_SIZE - y - 1).toFloat()
                    )
                    larry.setCoord(
                        (if ((3 - esquinaJoe) / 2 == 0) x else Constants.MAP_SIZE - x - 1).toFloat(),
                        (if ((3 - esquinaJoe) % 2 == 0) y else Constants.MAP_SIZE - y - 1).toFloat()
                    )
                    Log.d(TAG_GENERACIOPJ, "posInt=$posInt i=$i j=$j esquinaJoe=$esquinaJoe| Joe=$joe Larry=$larry")
                    Log.d(TAG_GENERACIOPJ, "Personatges ubicats correctament")
                    return
                }
            }
        }
        Log.e(TAG_GENERACIOPJ, "No se ha trobat ningun espai on posar els personatges")
    }

    //Dibuja todas las casilla del mapa usando el metodo de bloque de draw
    fun draw(canvas: Canvas) {
        for (y in 0 until Constants.MAP_SIZE) for (x in 0 until Constants.MAP_SIZE) {
            bloques[y][x].draw(canvas, x, y)
            }
        }

    fun removeEatable(x: Int, y: Int, resources: Resources) {
        bloques[y][x] = Path.getInstace(resources)
    }
}

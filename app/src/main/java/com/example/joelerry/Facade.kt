package com.example.joelerry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.Log
import com.example.joelerry.ia.Behavior
import com.example.joelerry.map_structures.Cheese
import com.example.joelerry.map_structures.Mapa
import com.example.joelerry.nivell.Derrota
import com.example.joelerry.nivell.Victoria
import com.example.joelerry.operacions.Operation
import com.example.joelerry.pj.Actor
import com.example.joelerry.pj.Joe
import com.example.joelerry.pj.Larry
import kotlin.math.roundToInt

class Facade(private var context: Context, var level: Int) {
    var pause = false
    private var playing = false
    private var mapa = Mapa()
    private var actores: Array<Actor>
    private var operations = ArrayList<Operation>(4)
    private var quesos = HashMap<Cheese, Pair<Int, Int>>()
    private var behavior = Behavior()

    private lateinit var actualOperation: Operation
    private lateinit var quesoARealizarOpIzquierda: Cheese
    private lateinit var quesoARealizarOpDerecha: Cheese
    private lateinit var quesoAEncontrar: Cheese
    private var colorQueso = Paint().also { it.color = context.resources.getColor(R.color.amarillo); it.textSize = 48f }


    private val toolBarColor = Paint().also { it.color = ContextCompat.getColor(context, R.color.gris) }
    private val toolBarRect =
        Rect(0, 0, Resources.getSystem().displayMetrics.widthPixels, Constants.SCREEN_HEIGHT_TOOLBAR.roundToInt())
    private val pauseBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_media_pause),
        Constants.SCREEN_HEIGHT_TOOLBAR.roundToInt(),
        Constants.SCREEN_HEIGHT_TOOLBAR.roundToInt(),
        true
    )
    private val resumeBitmap = Bitmap.createScaledBitmap(
        BitmapFactory.decodeResource(context.resources, R.drawable.ic_media_play),
        Constants.SCREEN_HEIGHT_TOOLBAR.roundToInt(),
        Constants.SCREEN_HEIGHT_TOOLBAR.roundToInt(),
        true
    )

    private var initialTime = 0L

    companion object {
        private const val TAG = "Facade"
    }

        init{
        val joe = Joe(context.resources)
        val larry = Larry(context.resources)
        actores = arrayOf(joe,larry)

        runLevel(level)
    }

    fun drawMap(canvas: Canvas) {
        mapa.draw(canvas)
    }

    fun updateJoeDirection(newX: Float, newY: Float) {
        actores[0].direction = Constants.Companion.Direction.getDirection(
            newX / Constants.SCREEN_WIDTH_BLOCK - actores[0].x - 0.5f,
            (newY - Constants.SCREEN_HEIGHT_TOOLBAR) / Constants.SCREEN_HEIGHT_BLOCK - actores[0].y - 0.5f
        )
    }

    fun update() {
        if (playing) {
            actores[0].updateMovement(mapa, context.resources)
            updateLarryCharacterMovement()
        }
    }

    private fun updateLarryCharacterMovement() {
        if ((actores[1] as Larry).needsPath()) {
            nextCheese()
            if (!playing)
                return
            val path = behavior.aStarSearch(
                Pair(actores[1].x.roundToInt(), actores[1].y.roundToInt()),
                Pair(quesos[quesoAEncontrar]!!.first, quesos[quesoAEncontrar]!!.second),
                mapa.grid
            )
            Log.d("*******PATH******", path.toString())
            (actores[1] as Larry).setPath(path)
        }
        actores[1].updateMovement(mapa, context.resources)
        if (checkColisionCharacher(actores[0], actores[1])) {
            Constants.data.capturasExistosas++
            playing = false
            runWinFragment()
        }
    }

    private fun checkColisionCharacher(actor: Actor, actor1: Actor): Boolean {
        return (actor.x + if (actor.x < actor1.x) 1 else 0) in actor1.x..(actor1.x + 1) &&
                (actor.y + if (actor.y < actor1.y) 1 else 0) in actor1.y..(actor1.y + 1)
    }


    fun drawCharacters(canvas: Canvas) {
        actores.forEach { it.draw(canvas) }
    }

    fun drawToolBar(canvas: Canvas) {
        canvas.drawRect(toolBarRect, toolBarColor)
        canvas.drawBitmap(
            if (pause) resumeBitmap else pauseBitmap,
            Constants.SCREEN_WIDTH - Constants.SCREEN_HEIGHT_TOOLBAR,
            0f,
            null
        )
        canvas.drawText(
            quesoARealizarOpIzquierda.toString(),
            0.5f * Constants.SCREEN_HEIGHT_TOOLBAR,
            0.5f * Constants.SCREEN_HEIGHT_TOOLBAR,
            colorQueso
        )
        canvas.drawText(
            actualOperation.toString(),
            Constants.SCREEN_WIDTH / 2f,
            0.5f * Constants.SCREEN_HEIGHT_TOOLBAR,
            colorQueso
        )
        canvas.drawText(
            quesoARealizarOpDerecha.toString(),
            0.5f * Constants.SCREEN_HEIGHT_TOOLBAR + Constants.SCREEN_WIDTH / 2,
            0.5f * Constants.SCREEN_HEIGHT_TOOLBAR,
            colorQueso
        )
    }

    //Metodo que se ha de llamar para poner en pantalla el activity de Derrota
    //Tambien desactiva el boton de siguiente nivel si supera el ultimo nivel
    private fun runLoseFragment() {
        (context as Activity).startActivityForResult(
            Intent(
                context,
                Derrota::class.java
            ).also {
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                it.putExtra(Constants.TAG_PUNTUACIO, calcularPuntuacion())
            },
            Constants.NIVELL_REQUEST_CODE
        )

    }

    //Metodo que se ha de llamar para poner en pantalla el activity de Victoria
    private fun runWinFragment() {
        (context as Activity).startActivityForResult(
            Intent(
                context,
                Victoria::class.java
            ).also {
                it.putExtra(Constants.TAG_WIN_FRAGMENT, level < Constants.MAX_N_OF_LEVELS)
                it.putExtra(Constants.TAG_PUNTUACIO, calcularPuntuacion())
                it.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            },
            Constants.NIVELL_REQUEST_CODE
        )
    }

    /**
     *  Metodo que incializa el nivel especificado generando todo
     */
    fun runLevel(level: Int) {
        operations.clear()
        quesos.clear()
        //Metodo que comprueba que operaciones se relizaran en el nivel especificado
        for (i in Constants.OPERACIONS_PER_NIVELL) {
            if (level in i.value)
                operations.add(i.key)
        }
        mapa.generateMap(context)
        mapa.generateBoost(context) //Actualemente no genera nada
        quesos.putAll(mapa.generateCheese(context))
        mapa.initActores(actores[0] as Joe, actores[1] as Larry)
        nextCheese(true)
        (actores[1] as Larry).setPath(
            behavior.aStarSearch(
                Pair(actores[1].x.roundToInt(), actores[1].y.roundToInt()),
                Pair(quesos[quesoAEncontrar]!!.first, quesos[quesoAEncontrar]!!.second),
                mapa.grid
            )
        )
        playing = true
        initialTime = System.currentTimeMillis()

        Log.d(TAG, "Nivell $level generat amb operacions $operations correctament")
    }

    /**
     * Metodo que esocge aleatoriamente el siguiente queso y lo borra de la lista de quesos. Tambien escoge una operacion
     * aleatoria dentro de las posibles por el nivel. Finalmente se generan aleatoriamente los dos valores de arriba.
     * Tambien se ocupa de borrar el queso y comprobar si se ha acabado la partida
     */
    private fun nextCheese(firstTime: Boolean = false) {
        Constants.SOUNDS.selectAudio(context,"JoeComida")
        if (quesos.size == 1) {
            runLoseFragment()
            Constants.data.escapesNoEvitados++
            quesos.remove(quesoAEncontrar)
            playing = false
        } else {
            if (!firstTime) {
                val position = quesos.remove(quesoAEncontrar)!!
                mapa.removeEatable(position.first, position.second, context.resources)
                Constants.data.quesosRecogidos++
            }
            actualOperation = operations.random()
            quesoAEncontrar = quesos.keys.random()

            quesoARealizarOpIzquierda =
                Cheese(context.resources, actualOperation.randomValidNumber(quesoAEncontrar.valor))
            quesoARealizarOpDerecha = Cheese(
                context.resources,
                actualOperation.inverseOperation(quesoARealizarOpIzquierda.valor, quesoAEncontrar.valor)
            )
                      Log.d("QuesoRealizarOp Derecha", quesoARealizarOpDerecha.toString())
                      Log.d("QuesoRealizarOp Izq", quesoARealizarOpIzquierda.toString())
                      Log.d("Queso a encontrar:", quesoAEncontrar.valor.toString())
            Log.d(TAG, "$quesoARealizarOpIzquierda$actualOperation$quesoARealizarOpDerecha=$quesoAEncontrar ")
        }

    }

    private fun calcularPuntuacion(): Int {
        val timePlayed = System.currentTimeMillis() - initialTime
        var puntuacioNivell = 400 * level + quesos.keys.flatMapTo(
            mutableListOf(),
            { listOf(2 * (500 - it.valor)) }).sum() - ((timePlayed) / 10).toInt()
        if (puntuacioNivell < 0)
            puntuacioNivell = 0
        if (Constants.data.puntuacionMaxima < puntuacioNivell)
            Constants.data.puntuacionMaxima = puntuacioNivell
        Constants.data.puntuacionTotal += puntuacioNivell
        Constants.data.timePlayed += (timePlayed / 1000).toInt()
        return puntuacioNivell
    }

}

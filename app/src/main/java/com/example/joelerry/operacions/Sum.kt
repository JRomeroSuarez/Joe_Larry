package com.example.joelerry.operacions

import kotlin.random.Random

class Sum : Operation() {

    override val puntuacio = 250

    /*
    Override de operación para declarar nuestra operación división,
    (valor1+valor2)
     */
    override fun operation(value1:Int,value2:Int): Int {
        return (value1+value2)
    }

    override fun inverseOperation(valorIzquierda: Int, valorObjetivo: Int): Int {
        return valorObjetivo - valorIzquierda
    }

    override fun randomValidNumber(valorObjetivo: Int, seed: Long): Int {
        return (1 until valorObjetivo).random(Random(seed))
    }

    override fun toString(): String {
        return "+"
    }
}
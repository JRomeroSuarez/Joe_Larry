package com.example.joelerry.operacions

import kotlin.random.Random

class Division : Operation() {

    override val puntuacio = 100

    /*
    Override de operación para declarar nuestra operación división,
    (valor1/valor2)
     */
    override fun operation(value1:Int,value2:Int): Int {
        return (value1/value2)
    }

    override fun inverseOperation(valorIzquierda: Int, valorObjetivo: Int): Int {
        return valorIzquierda / valorObjetivo
    }

    override fun randomValidNumber(valorObjetivo: Int, seed: Long): Int {
        return (1..5).random(Random(seed)) * valorObjetivo
    }

    override fun toString(): String {
        return "/"
    }
}
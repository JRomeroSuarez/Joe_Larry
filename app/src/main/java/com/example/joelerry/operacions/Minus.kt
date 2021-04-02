package com.example.joelerry.operacions

import kotlin.random.Random

class Minus : Operation() {

    override val puntuacio = 200

    /*
    Override de operación para declarar nuestra operación resta,
    (valor1-valor2)
     */
    override fun operation(value1:Int,value2:Int): Int {
        return (value1-value2)
    }

    override fun inverseOperation(valorIzquierda: Int, valorObjetivo: Int): Int {
        return valorIzquierda - valorObjetivo
    }

    override fun randomValidNumber(valorObjetivo: Int, seed: Long): Int {
        return (valorObjetivo + 1..(2 * valorObjetivo)).random(Random(seed))
    }

    override fun toString(): String {
        return "-"
    }
}
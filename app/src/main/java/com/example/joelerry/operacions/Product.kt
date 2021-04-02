package com.example.joelerry.operacions

import kotlin.random.Random

class Product : Operation() {

    override val puntuacio = 150

    /*
    Override de operación para declarar nuestra operación multiplicación,
    (valor1*valor2)
     */
    override fun operation(value1:Int,value2:Int): Int {
        return (value1*value2)
    }

    override fun inverseOperation(valorIzquierda: Int, valorObjetivo: Int): Int {
        return valorObjetivo / valorIzquierda
    }

    override fun randomValidNumber(valorObjetivo: Int, seed: Long): Int {
        val factors = mutableListOf<Int>()
        (1..valorObjetivo / 2)
            .filter { valorObjetivo % it == 0 }
            .forEach { factors.add(it) }
        factors.add(valorObjetivo)
        return factors.random(Random(seed))
    }

    override fun toString(): String {
        return "*"
    }
}
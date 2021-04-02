package com.example.joelerry.operacions

import kotlin.random.Random

abstract class Operation {

    abstract val puntuacio: Int

    /*Tendremos una única función abstracta operation con dos valores que
    utilizaremos en todas las subclases derivadas de la interfaz para poder
    hacer el cálculo aritmético que deseemos
     */
    abstract fun operation(value1: Int, value2: Int): Int

    abstract fun randomValidNumber(valorObjetivo: Int, seed: Long = Random.nextLong()): Int

    abstract fun inverseOperation(valorIzquierda: Int, valorObjetivo: Int): Int


}
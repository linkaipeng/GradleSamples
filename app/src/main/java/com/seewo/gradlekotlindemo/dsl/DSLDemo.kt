package com.seewo.gradlekotlindemo.dsl

/**
 * Created by linkaipeng on 2019-08-03.
 *
 */
class DSLDemo {


    fun sum0(x: Int, y: Int): Int {
        return x + y
    }

    fun sum1(x: Int, y: Int): Int = x + y


    val sum2 = {x: Int, y: Int -> x + y }

    fun printSum(sum: (Int, Int) -> Int) {
        val result = sum(1, 1)
        println("result = $result")
    }

    fun test() {
        printSum(sum2)
    }


}
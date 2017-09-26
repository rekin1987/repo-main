package basics

import intermediate.isOdd

fun ifElse() {

    val a = 2
    val b = 3

    // no more '<condition> ? <if-true> : <else>
    val min = if (a < b) a else b

    val max = if (a > b) {
        print("Choose a")
        a
    } else {
        print("Choose b")
        b
    }
}

fun forLoop() {
    val ints: IntArray = intArrayOf(1, 2, 3)
    for (item: Int in ints) {
        // ...
    }

    for ((index, value) in ints.withIndex()) {
        println("the element at $index is $value")
    }
}

fun whileLoop() {
    var a = 5
    while (a > 0) {
        //
        a--
    }
}

// 'Any' is a superclass for every object - like 'Object' in Java
fun whenExpr(x: Any) {

    // works like switch, but more extended
    when (x) {
        1 -> print("x == 1")
        2 -> print("x == 2")
        in 3..9 -> print("x between 3 and 9")
        is String -> print("input is String type")
        is Int -> if (x.isOdd()) print("x is odd") else print("x is even")
        else -> { // Note the block
            print("x is neither 1 nor 2")
        }
    }
}


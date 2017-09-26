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


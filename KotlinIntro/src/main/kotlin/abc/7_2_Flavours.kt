package abc

fun flavours1() {
    val arrayInt = listOf(1, 2, 3, 4, 5)
    var arrayStr = listOf("one", "two", "three", "four", "five")

    arrayInt.forEach { it * 2 /* it +=2 can't assign the value, can print out */ }
    var doubleArrayInt = arrayInt.map { it * 2 /* can't assign the value */ }

    var boolVal = arrayInt.any { it>3 }
    boolVal = arrayInt.none { it>3 }

    var arrayStr2 = arrayStr.map { it + "aa" }

    arrayStr2.forEach { println(it) }

    arrayStr2 = arrayStr.sortedBy { it.length }

    arrayStr2.forEach { println(it) }

    arrayStr2 = arrayStr.filter { it.startsWith("f") }

    arrayStr2.forEach { println(it) }

}


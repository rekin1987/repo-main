package basics

fun printToConsole() {
    val intVal: Int = 7
    val floatNum = 5.5f // inferred type
    val longPrice = 1_000_000_000L
    val stringItem = "string with int val is $intVal"

    println("print string = $stringItem and long = $longPrice and even float = ${if (floatNum > 10) "large" else "small"}")
}

fun readFromConsole() {
    val line = readLine()
    // elvis expression
    println("line is ${line?.length ?: "NULL"}")
}

fun main(args: Array<String>){
    readFromConsole()
    printToConsole()
}
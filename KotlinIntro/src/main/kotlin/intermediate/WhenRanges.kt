package intermediate


// 'Any' is a superclass for every object - like 'Object' in Java
fun whenExpr(x: Any) {

    // works like switch, but more extended
    when (x) {
        1 -> print("x == 1")
        2 -> print("x == 2")
        in 3..9 -> print("x between 3 and 9") // range
        is String -> print("input is String type")
        is Int -> if (x.isOdd()) print("x is odd") else print("x is even")
        else -> { // Note the block
            print("x is neither 1 nor 2")
        }
    }
}

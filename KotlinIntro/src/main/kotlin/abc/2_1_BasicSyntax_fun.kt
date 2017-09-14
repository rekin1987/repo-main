package abc

fun fun1() {
}

fun fun2(name: String) {
}

fun fun22(name: String = "defaultName") {
}

fun fun3(value: Int): String {
    return "something"
}

fun fun4(value: Int) = "something"  // inferred return type


// ------------------------------------
// extension functions for Int class:
// ------------------------------------

// complete syntax
fun Int.isOdd() : Boolean {
    // mod operation to check if int is odd
   return this % 2 == 1
//    return this.rem(2) == 1
}

fun Int.isEven() = this % 2 == 0 // mod operation to check if int is even


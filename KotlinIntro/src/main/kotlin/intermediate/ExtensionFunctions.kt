package intermediate

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
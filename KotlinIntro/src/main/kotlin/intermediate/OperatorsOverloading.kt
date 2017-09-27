package intermediate

class Gold(var amount: Int) {

    operator fun plus(that: Gold) = Gold(this.amount + that.amount)
}

fun testGold(){
    val g1 = Gold(5)
    val g2 = Gold(7)
    val g3 = g1 + g2

    println("gold = ${g3.amount}")
}


// new operators as extension functions
operator fun String.minus(reduceBy: Int) : String{
    val newLength = this.length - reduceBy
    return this.substring(0, newLength)
}

fun testStringMinus(){
    val str1 = "String1"
    val str = str1 - 1
    str == "String"
}

fun testCollection(){
    val list = mutableListOf("one", "two")
    list += "three"
}
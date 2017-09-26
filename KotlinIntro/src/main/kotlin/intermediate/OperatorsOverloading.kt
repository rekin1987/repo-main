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

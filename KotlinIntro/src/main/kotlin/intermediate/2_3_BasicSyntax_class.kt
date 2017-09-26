package intermediate

// class can be declared as:
//open
//sealed
//inner
//abstract
//data


class A private constructor(private val ii: Int) {

    private constructor(a: Long) : this(a.toInt()) {
    }

    constructor(a: Int, b: String) : this(a) {
    }

}

class Aaaa1(amount: Int) {
    var amount = amount
}

class Aaaa2(var amount: Int) {
}

sealed class B()

private open class C : B(), In, InA

interface In {
}

interface InA {
}

interface InB : In, InA {
    fun execute()
    fun run(a: Int): String {
        return (a * 2).toString()
    }
}

enum class Consts {
    MAX, MIN, AVERAGE
}

class D : InB {
    override fun execute() {
        run(5)
    }
}

class E {
    companion object : InA {
        val name = "aaa"
    }

    object B : InA {
        val name = "bbb"
    }
}

fun testClasses() {
    E.B.name // bbb
    E.name // aaa
}
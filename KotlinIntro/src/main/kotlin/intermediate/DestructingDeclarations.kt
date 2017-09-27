package intermediate

class APerson(val name: String, val age: Int, val male: Boolean) {
    operator fun component1() = name
    operator fun component2() = age
    operator fun component3() = male
}

fun declarationDestructing() {
    val person = APerson("abc", 18, true)
    val (pName, pAge, pMale) = person

    pAge == person.age
}

fun declarationDestructing2() {
    val myMap = mapOf(1 to "one", 2 to "two", 3 to "three")

    for ((k, v) in myMap) {
        println("key=$k, value=$v")
    }
}

// function can return more than one value
fun returningPerson(): APerson {
    return APerson("abc", 18, true)
}

fun testMultipleReturn(){
    val (pName, pAge, pMale) = returningPerson()
}
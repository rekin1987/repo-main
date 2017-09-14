package abc

class MyClass {
    fun myFun(param1: Int, param2: String = "default_text"): Unit {

    }
}

object MySingleton {
    var callCount = 0

    fun callMe(){
        ++callCount
    }
}

fun usage(){
    val cl = MyClass()
    cl.myFun(5)

    MySingleton.callMe() // this is NOT a static method call
}
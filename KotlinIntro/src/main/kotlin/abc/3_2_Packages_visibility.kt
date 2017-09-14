package abc


open class VisClass {

    internal var pp = 9;

    var privateSetterVar = 5
    private set

    protected fun myProtectedMethod() {

    }
}


fun checkProtected() {
    val vis = VisClass()
//    vis.myProtectedMethod() // not visible
    vis.pp = 99
}

class Another {
    fun testVis() {
        val vis = VisClass()
//        vis.myProtectedMethod() // not visible

        val field = vis.privateSetterVar

//        vis.privateSetterVar = 88 // setter is private
    }
}
package pl.emget.kotlingame

object Log {

    fun d(tag: String = "Log", message: String) {
        println("DEBUG >> $tag >> $message")
    }

}
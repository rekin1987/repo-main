package pl.emget.textgamekotlin

object Log {

    fun d(tag: String = "Log", message: String) {
        println("DEBUG >> $tag >> $message")
    }

}
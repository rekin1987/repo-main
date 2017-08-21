package pl.emget.textgamekotlin

object Log {

    fun d(tag: String, message: String) {
        println("DEBUG >> $tag >> $message")
    }

}
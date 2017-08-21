package pl.emget.textgame

object Log {

    fun d(tag: String, message: String) {
        println("DEBUG >> $tag >> $message")
    }

}
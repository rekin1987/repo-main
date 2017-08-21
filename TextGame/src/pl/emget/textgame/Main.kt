package pl.emget.textgame

private val TAG = "Main"

fun main(args: Array<String>){
    Log.d(TAG, "Starting game")

    RegexMatcher.isNameAndSurname("Pawel Suszek")
    RegexMatcher.isNameAndSurname("Urlich von Jungingen")

    RegexMatcher.isUrl("www.wp.pl")
    RegexMatcher.isUrl("http://www.wp.com")
    RegexMatcher.isUrl("https://www.wp.pl")
    RegexMatcher.isUrl("www.gooog.com.pl")

    RegexMatcher.isReg("rek abc")


}
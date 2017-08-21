package pl.emget.textgame

object RegexMatcher {

    fun isNameAndSurname(nameAndSurname: String) = Regex("[A-Z][a-z]+\\s[a-z]*\\s*[A-Z][a-z]+").matches(nameAndSurname)


    fun isUrl(url:String) = Regex("(http://|https://)?(www.)?\\w+.(com|pl).?(com|pl)?").matches(url)

    fun isReg(exp:String) = Regex("[abc]+").matches(exp)

}


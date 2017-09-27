package intermediate

fun checkSmartCasts() {

    val mayBeNull: String? = null
    val mayBeNull2: String? = "not null"

//    val len = mayBeNull.length
//    val len2 = mayBeNull2.length
    val len3 = mayBeNull?.length
    val len4 = mayBeNull2?.length

    val len5 = mayBeNull!!.length

    if (mayBeNull != null) {
        val len6 = mayBeNull.length // smart cast String? -> String
    }

    val anyString: Any = "any"

    when (anyString) {
        is String -> print(anyString.length) // smart cast Any -> String
        "any" -> print("string contains text 'any'")
    }

}
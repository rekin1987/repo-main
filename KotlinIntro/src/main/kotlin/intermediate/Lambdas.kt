package intermediate

val VERSION = "2.1-release"

/*
inline VS not inline
inline just puts the implementation in place of call
not inline will create temp object with a function in place of call
*/

inline fun onlyReleaseVersion(functionToExecute: () -> Unit) {
    if (VERSION.contains("release")) {
        functionToExecute()
    }
}

fun useFun1() {
    onlyReleaseVersion({ println("we are releasing") })
    onlyReleaseVersion() { println("we are releasing"); println("second in the same line line") }
    onlyReleaseVersion { println("we are releasing") }
    onlyReleaseVersion {
        println("we are releasing")
        println("we are releasing")
    }
}


//inline fun onlyLollipop(functionToExecute: () -> Unit) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//        functionToExecute()
//    }
//}
//onlyLollipop {
//    toast("aa")
//}


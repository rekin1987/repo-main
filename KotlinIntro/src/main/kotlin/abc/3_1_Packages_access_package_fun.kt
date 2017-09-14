package abc

// we are in different package, so need to import or use fully qualified name
import com.example.globalFun
// import with alias
import com.example.globalFun2 as gf2

fun myFun() {
    globalFun()
    gf2()
    com.example.globalFun() // when using fully qualified name we don't need to import
}

fun myFun2() = globalFun() // this is NOT a function pointer assignment!
//this is:
//fun myFun2() {
//    globalFun()
//}

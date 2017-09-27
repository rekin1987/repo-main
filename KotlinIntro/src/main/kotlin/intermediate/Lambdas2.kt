package intermediate

interface OnClickListener {
    fun onClick(view: View)
}

class View {
    fun setOnClickListener(listener: OnClickListener){
        // register callback
    }
    fun setOnClickListener(listener: (View) -> Unit){
        // register callback
    }
}

fun toast(msg: String){
    // show toast
}

// using lambda as anonymous class
fun useLambdaAsAnimClass() {
    val view = View()
    // method 1
    view.setOnClickListener(object : OnClickListener {
        override fun onClick(view: View) {
            toast("clicked!")
        }
    })

    // method 2
    view.setOnClickListener({ view -> toast("clicked!") })

    view.setOnClickListener({ toast("clicked!") })

    view.setOnClickListener() { toast("clicked!") }

    view.setOnClickListener { toast("clicked!") }

}

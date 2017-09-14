package abc


fun nullOrNotNull() {
    val mightBeNull: Float? = null
    val mightBeAnotherNull = null // type 'Nothing?'

    // val notNullLong: Long = null // can't assign null

    // val vv: Int = mightBeAnotherNull
}
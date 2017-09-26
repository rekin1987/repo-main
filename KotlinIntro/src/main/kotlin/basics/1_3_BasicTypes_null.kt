package basics


fun nullOrNotNull() {
    val mightBeNull: Float? = null
    val mightBeAnotherNull = null // type 'Nothing?'

    // val notNullLong: Long = null // can't assign null

    // val vv: Int = mightBeAnotherNull // require type Int, but have type Nothing?
}
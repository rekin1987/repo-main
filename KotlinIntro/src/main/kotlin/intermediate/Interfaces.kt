package intermediate


private interface InternalInt1
private interface InternalInt2


private interface Int3 : InternalInt1, InternalInt2 {
    fun execute()
    fun run(a: Int): String {
        return (a * 2).toString()
    }
}

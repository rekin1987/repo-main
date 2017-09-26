package intermediate


fun createTree(radius: Int = 2, height: Int = 7, type: String = "Pine") : String {
 return type
}

fun testDefaultParams() {
    // note the radius and height are taken as default!
    val myOakTreeType = createTree(type = "Oak")

}
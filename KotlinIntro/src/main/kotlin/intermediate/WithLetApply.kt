package intermediate

class Tree(var radius: Int = 2, var height: Int = 7, var type: String = "Pine") {

    var subTree = Tree()

}

class Gardener {

    fun waterTree(tree: Tree) : Tree{
        // watering tree...
        return tree // watering completed successfully, return the watered tree
    }

}

/////// testing below

fun testWith() {
    // note the radius and height are taken as default!
    val myOakTree = Tree(type = "Oak")

    myOakTree.subTree.height = 45
    myOakTree.subTree.radius = 11
    myOakTree.subTree.type = "OldOak"

    with(myOakTree){
        // we are referring to 'myOakTree' object
        height = 450
        radius = 110
        type = "VeryOldOak"
    }

    with(myOakTree.subTree){
        // we are referring to 'myOakTree.subTree' object
        height = 43
        radius = 13
        type = "MyBigSubTree"
    }
}

fun testLet() {
    var myTree : Tree? = null

    var wateredTree = if(myTree!= null) Gardener().waterTree(myTree) else null

    wateredTree = myTree?.let {Gardener().waterTree(it)}
}

fun testApply(){
    val tree = Tree().apply {
        height = 11
        radius = 9
        type = "SomePineTree"
    }

//    val textView = TextView(context).apply{
//        text = "Hello"
//        hint = "Hint"
//        textColor = android.R.color.white
//    }
}



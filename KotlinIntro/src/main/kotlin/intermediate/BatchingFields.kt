package intermediate

class Person {

    var name: String = ""
        get() = field.toLowerCase()
        set(value) {
            field = "Name = " + value.capitalize()
        }

    var age: Int? = null
        private set
}
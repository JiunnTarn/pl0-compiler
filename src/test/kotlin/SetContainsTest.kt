import model.*

fun main() {
    val firstSet = object : HashSet<Expression>() {
        override fun contains(element: Expression): Boolean {
            forEach {
                if (it.id == element.id) {
                    return true
                }
            }
            return false
        }
    }
    val id = Expression(TokenId.IDN, "id", true, ArrayList())
    val idE = Expression(TokenId.IDN, "a", true, ArrayList())
    firstSet.add(id)
    println(firstSet.contains(idE))
}
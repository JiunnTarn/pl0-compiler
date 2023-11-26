package model

class FirstSet : HashSet<Expression>() {
    override fun contains(element: Expression): Boolean {
        forEach {
            if (it.id == element.id || it.id == ExpressionId.EPSILON || it.id == ExpressionId.EMPTY) {
                return true
            }
        }
        return false
    }
}
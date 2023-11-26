package model

data class Production(
    val left: Expression,
    val right: List<Expression>
) {
    init {
        check(left.isTerminal.not())
        check(right.isNotEmpty())
    }

    override fun toString(): String {
        return "$left -> ${right.joinToString(" ")}"
    }

    fun isEpsilon(): Boolean {
        return right.isEmpty()
    }

    fun firstSet(): Set<Expression> {
        val firstSet = FirstSet()
        firstSet.addAll(right.first().firstSet())
        return firstSet
    }
}

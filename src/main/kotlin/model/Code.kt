package model

data class Code(
    val f: String,
    val l: Int,
    val a: Int
) {
    override fun toString() = "$f\t$l\t$a"
}
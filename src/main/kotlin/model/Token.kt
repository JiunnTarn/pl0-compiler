package model

class Token(
    private val id: Int,
    private val value: String?
) {
    override fun toString(): String {
        return "Token(id=$id, value=$value)"
    }
}
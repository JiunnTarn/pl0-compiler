package model

open class Token(
    val id: Int,
    val value: String?,
    val line: Int? = null
) {
    override fun toString(): String {
        return "Token(id=$id, value=$value, line=$line)"
    }
}

class ErrorToken(line: Int) : Token(TokenId.ERR, null, line)
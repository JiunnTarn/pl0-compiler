package model

data class Symbol(
    val name: String,
    val type: SymbolType,
    val level: Int,
    val address: Int?,
    val value: Int? = null
) {
    override fun toString() = StringBuilder("name: $name\ttype: $type\tlevel: $level\taddress: $address").run { value?.let { append("\tvalue: $it") }; toString() }
}

enum class SymbolType {
    CONSTANT, VARIABLE, PROCEDURE
}
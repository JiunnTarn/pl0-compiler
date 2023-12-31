package model

class Transition(
    var from: State,
    val via: Char,
    var tos: List<State>
) {
    override fun toString(): String {
        return "Transition(from=$from, via=$via, tos=$tos)"
    }
}

class CompoundTransition(
    val from: CompoundState,
    val via: Char,
    val tos: List<State>
) {
    override fun toString(): String {
        return "CompoundTransition(from=$from, via=$via, tos=$tos)"
    }
}
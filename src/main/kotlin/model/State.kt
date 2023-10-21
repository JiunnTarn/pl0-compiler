package model

class State(
    val tag: String,
    var accepted: Boolean,
) {
    companion object {
        val START = State("S", false)

    }

    constructor(compoundState: CompoundState) : this(compoundState.tag, compoundState.accepted)

    override fun toString(): String {
        return "S$tag" + if (accepted) "*" else ""
    }
}

class CompoundState(
    val tag: String,
    val accepted: Boolean,
    val states: List<State>,
) {
    constructor(state: State) : this(state.tag, state.accepted, listOf(state))
    constructor(states: List<State>) : this(
        states.joinToString("") { it.tag },
        states.any { it.accepted },
        states
    )

    override fun toString(): String {
        return "CS$tag"
    }
}
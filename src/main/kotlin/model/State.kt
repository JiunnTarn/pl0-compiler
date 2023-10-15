package model

class State(
    val id: String,
    val accepted: Boolean,
    val token: Token?,
) {
    companion object {
        val START = State("S", false, null)
    }

    constructor(compoundState: CompoundState) : this(compoundState.id, compoundState.accepted, compoundState.token)

    override fun toString(): String {
        return "S$id"
    }
}

class CompoundState(
    val id: String,
    val accepted: Boolean,
    val token: Token?,
    val states: List<State>,
) {
    constructor(state: State) : this(state.id, state.accepted, state.token, listOf(state))
    constructor(states: List<State>) : this(
        states.joinToString("") { it.id },
        states.any { it.accepted },
        //TODO 有多个接受态怎么办
        states.find { it.accepted }?.token,
        states
    )

    override fun toString(): String {
        return "CS$id"
    }
}
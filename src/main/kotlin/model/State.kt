package model

class State(
    val tag: String,
    var accepted: Boolean,
    var tokenId: Int? = null
) {
    constructor(compoundState: CompoundState) : this(compoundState.tag, compoundState.accepted, compoundState.tokenId)

    override fun toString(): String {
        return "S$tag" + if (accepted) "*" else ""
    }
}

class CompoundState(
    val tag: String,
    val accepted: Boolean,
    val states: List<State>,
    var tokenId: Int? = null
) {
    constructor(states: List<State>) : this(
        states.joinToString("") { it.tag },
        states.any { it.accepted },
        states,
        states.firstOrNull { it.tokenId != null }?.tokenId
    )

    override fun toString(): String {
        return "CS$tag"
    }
}
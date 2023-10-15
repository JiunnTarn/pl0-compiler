package model

import java.util.*

class FA(
    val startState: State, val states: List<State>, var transitions: List<Transition>
) {
    private fun hasMultipleTos(): Boolean {
        transitions.forEach { transition ->
            if (transition.tos.size > 1) {
                return true
            }
        }
        return false
    }

    private fun hasEpsilonSide(): Boolean {
        return transitions.any { it.via == 'ε' }
    }

    fun isNFA(): Boolean {
        return !(hasMultipleTos() || hasEpsilonSide())
    }

    private fun insertTransition(transition: Transition) {
        val newTransitions = transitions.toMutableList()

        val matchingTransition = newTransitions.find { it.from == transition.from && it.via == transition.via }
        if (matchingTransition != null) {
            val newTos = matchingTransition.tos.toMutableList()
            newTos.addAll(transition.tos)
            matchingTransition.tos = newTos.toList()
        } else {
            newTransitions.add(transition)
        }

        transitions = newTransitions

    }

    private fun mergeEpsilon() {
        states.forEach {  flattenEpsilonNode(it, it) }
        cleanupEpsilon()
    }

    private fun flattenEpsilonNode(root: State, entry: State) {
        val tosFromRoot = transitions.filter { it.from == root }
        val epsilonTosFromEntry = HashSet<State>()

        transitions.filter { it.from == entry && it.via == 'ε' }.forEach { transition ->
            transition.tos.forEach { to ->
                epsilonTosFromEntry.add(to)
            }
        }
        for (epsilonTo in epsilonTosFromEntry) {
            tosFromRoot.groupBy { it.via }.filter { it.key != 'ε' }.forEach { (_, group) ->
                val t = Transition(from = root, via = group[0].via, tos = listOf(epsilonTo))
                insertTransition(t)
            }
            if (epsilonTo != root) {
                transitions.filter { it.from == epsilonTo && it.via != 'ε' }.forEach {
                    val t = Transition(from = root, via = it.via, tos = it.tos)
                    insertTransition(t)
                    flattenEpsilonNode(root, epsilonTo)
                }
            }
        }
    }

    private fun cleanupEpsilon() {
        transitions = transitions.filter { it.via != 'ε' }
    }

    fun buildDFA(): FA {
        if (hasEpsilonSide()) {
            mergeEpsilon()
        }

        if (hasMultipleTos()) {
            val newStartState = startState
            val compoundStartState = CompoundState(newStartState)

            val newCompoundStates = mutableListOf<CompoundState>()
            newCompoundStates.add(compoundStartState)
            val newCompoundTransitions = mutableListOf<CompoundTransition>()

            val compoundStateQueue: Queue<CompoundState> = LinkedList()
            compoundStateQueue.add(compoundStartState)

            while (compoundStateQueue.isNotEmpty()) {
                val compoundState = compoundStateQueue.poll()

                val transitionFromState = transitions.filter {
                    var f = false
                    for (s in compoundState.states) {
                        f = f || it.from == s
                    }
                    f
                }.groupBy {
                    it.via
                }.map { (_, group) ->
                    Transition(group[0].from, group[0].via, group.flatMap { it.tos }.distinct())
                }

                for (transition in transitionFromState) {
                    var toState = CompoundState(
                        id = transition.tos.joinToString("") { it.id },
                        accepted = transition.tos.any { it.accepted },
                        //TODO 有多个接受态怎么办
                        token = transition.tos.find { it.accepted }?.token,
                        states = transition.tos
                    )
                    if (!newCompoundStates.any { it.states == toState.states }) {
                        newCompoundStates.add(toState)
                        compoundStateQueue.add(toState)
                    } else {
                        toState = newCompoundStates.find { it.states == toState.states }!!
                    }
                    val newCompoundTransition = CompoundTransition(
                        from = compoundState, via = transition.via, tos = listOf(toState)
                    )
                    newCompoundTransitions.add(newCompoundTransition)
                }
            }

            println("newCompoundStates: $newCompoundStates")
            println("newCompoundTransitions: $newCompoundTransitions")

            val stateMap: MutableMap<CompoundState, State> = HashMap()
            val newStates = newCompoundStates.map { compoundState ->
                val s = State(compoundState)
                stateMap[compoundState] = s
                s
            }
            val newTransitions = newCompoundTransitions.map { compoundTransition ->
                Transition(
                    from = stateMap[compoundTransition.from]!!,
                    via = compoundTransition.via,
                    tos = listOf(stateMap[compoundTransition.tos[0]]!!)
                )
            }
            return FA(
                startState = newStartState,
                states = newStates,
                transitions = newTransitions
            )
        }
        return this
    }

    override fun toString(): String {
        return "FA(startState=$startState, states=$states, transitions=$transitions)"
    }
}
package model

import util.TagUtil
import java.util.*
import kotlin.collections.HashSet

class FA(
    var startState: State, var states: List<State>, var transitions: List<Transition>
) {

    companion object {
        fun literal(via: Char): FA {
            val startState = State(tag = TagUtil.getNewTag().toString(), accepted = false)
            val acceptingState =
                State(tag = TagUtil.getNewTag().toString(), accepted = true)
            val transition = Transition(from = startState, via = via, tos = listOf(acceptingState))

            return FA(startState, listOf(startState, acceptingState), listOf(transition))
        }

        fun epsilonLoop(): FA {
            val startState = State(tag = TagUtil.getNewTag().toString(), accepted = false)
            val acceptingState =
                State(tag = TagUtil.getNewTag().toString(), accepted = true)
            val transition1 = Transition(from = startState, via = 'ε', tos = listOf(acceptingState))
            val transition2 = Transition(from = acceptingState, via = 'ε', tos = listOf(startState))

            return FA(startState, listOf(startState, acceptingState), listOf(transition1, transition2))
        }
    }

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

    private fun hasMultipleAcceptingStates(): Boolean {
        return states.count { it.accepted } > 1
    }

    private fun getAcceptingState(): State {
        check(!hasMultipleAcceptingStates())

        return states.first { it.accepted }
    }

    fun isNFA(): Boolean {
        return !(hasMultipleTos() || hasEpsilonSide())
    }

    fun union(fa2: FA): FA {
        val acceptingState = getAcceptingState()
        val fa2AcceptingState = fa2.getAcceptingState()

        val newStartState = State(TagUtil.getNewTag().toString(), false)
        val newTransition = Transition(from = newStartState, via = 'ε', tos = listOf(startState, fa2.startState))

        val newAcceptingState = State(TagUtil.getNewTag().toString(), true)
        val transition1 = Transition(from = acceptingState, via = 'ε', tos = listOf(newAcceptingState))
        val transition2 = Transition(from = fa2AcceptingState, via = 'ε', tos = listOf(newAcceptingState))

        fa2.transitions.forEach { insertTransition(it) }

        states.forEach { it.accepted = false }

        fa2.states.forEach { it.accepted = false }

        startState = newStartState

        states = states.toMutableList().apply { addAll(fa2.states) }.apply { addAll(listOf(newStartState, newAcceptingState)) }.toList()

        insertTransition(newTransition)
        insertTransition(transition1)
        insertTransition(transition2)
//
//        states = states.toMutableList()
//            .apply { addAll(fa2.states.filter { it != fa2StartState && it != fa2AcceptingState }) }.toList()
//
//        for (transition in fa2.transitions) {
//            if (transition.from == fa2StartState) {
//                transition.from = startState
//            }
//            if (transition.from == fa2AcceptingState) {
//                transition.from = acceptingState
//            }
//            transition.tos = transition.tos.toMutableList().apply {
//                replaceAll {
//                    when (it) {
//                        fa2StartState -> {
//                            startState
//                        }
//                        fa2AcceptingState -> {
//                            acceptingState
//                        }
//                        else -> {
//                            it
//                        }
//                    }
//                }
//            }.toList()
//            insertTransition(transition)
//        }



        return this
    }

    fun concatenate(fa2: FA): FA {
        val acceptingState = getAcceptingState()
        val fa2StartState = fa2.startState

        states = states.filter { it != acceptingState }.toMutableList()
            .apply { addAll(fa2.states) }.toList()

        for (transition in transitions) {
            if (transition.from == acceptingState) {
                transition.from = fa2StartState
            }
            transition.tos = transition.tos.toMutableList().apply {
                replaceAll {
                    if (it == acceptingState) {
                        fa2StartState
                    } else {
                        it
                    }
                }
            }.toList()
        }

        fa2.transitions.forEach { insertTransition(it) }

        return this
    }

    fun closure(): FA {
        val acceptingState = getAcceptingState()

        val newStartState = State(TagUtil.getNewTag().toString(), false)
        val newAcceptingState = State(TagUtil.getNewTag().toString(), true)

        val transition1 = Transition(from = newStartState, via = 'ε', tos = listOf(newAcceptingState, startState))
        val transition2 = Transition(from = acceptingState, via = 'ε', tos = listOf(newAcceptingState))
        val transition3 = Transition(from = acceptingState, via = 'ε', tos = listOf(newAcceptingState, startState))

        states.forEach { it.accepted = false }

        startState = newStartState
        states = states.toMutableList().apply { addAll(listOf(newStartState, newAcceptingState)) }.toList()

        insertTransition(transition1)
        insertTransition(transition2)
        insertTransition(transition3)

        return this

//        val epsilonLoop = epsilonLoop()
//        return union(epsilonLoop)
    }

    private fun insertTransition(transition: Transition) {
        val newTransitions = transitions.toMutableList()

        val matchingTransition = newTransitions.find { it.from == transition.from && it.via == transition.via }
        if (matchingTransition != null) {
            val newTos = matchingTransition.tos.toMutableSet()
            newTos.addAll(transition.tos)
            matchingTransition.tos = newTos.toList()
        } else {
            newTransitions.add(transition)
        }

        transitions = newTransitions

    }

    private fun mergeEpsilon() {
        val visited = HashSet<State>()
        states.forEach { flattenEpsilon(it, it, visited) }
        cleanupEpsilon()
    }

    private fun flattenEpsilon(root: State, entrance: State, visited: MutableSet<State>) {
        visited.add(root)
        val epsilonTosFromEntrance = HashSet<State>()

        transitions.filter { it.from == entrance && it.via == 'ε' }.forEach { transition ->
            transition.tos.forEach { to ->
                epsilonTosFromEntrance.add(to)
            }
        }

        for (epsilonTo in epsilonTosFromEntrance) {
            transitions.filter { it.via != 'ε' && it.tos.contains(entrance) }.forEach {
                val t = Transition(from = it.from, via = it.via, tos = listOf(epsilonTo))
                insertTransition(t)
            }
            if (!visited.contains(epsilonTo)) {
                transitions.filter { it.from == epsilonTo && it.via != 'ε' }.forEach {
                    val t = Transition(from = root, via = it.via, tos = it.tos)
                    insertTransition(t)
                    flattenEpsilon(root, epsilonTo, visited)
                }
            }
        }
    }

    private fun cleanupEpsilon() {
        transitions = transitions.filter { it.via != 'ε' }
    }

    private fun searchStartStates(): MutableList<State> {
        val startStates: MutableList<State> = ArrayList()
        val stack: Stack<State> = Stack()
        stack.add(startState)

        while (stack.isNotEmpty()) {
            val state = stack.pop()
            if (!startStates.contains(state)) {
                startStates.add(state)
            }
            transitions.filter { it.from == state && it.via == 'ε' }.forEach {
                for (s in it.tos) {
                    if (!startStates.contains(s)) {
                        stack.push(s)
                    }
                }
            }
        }
        return startStates
    }

    fun buildDFA(): FA {
        lateinit var newCompoundStartState: CompoundState

        if (hasEpsilonSide()) {
            newCompoundStartState = CompoundState(searchStartStates())
            mergeEpsilon()
        } else {
            newCompoundStartState = CompoundState(startState)
        }

        if (hasMultipleTos()) {
            val newCompoundStates = mutableListOf<CompoundState>()
            newCompoundStates.add(newCompoundStartState)
            val newCompoundTransitions = mutableListOf<CompoundTransition>()

            val compoundStateQueue: Queue<CompoundState> = LinkedList()
            compoundStateQueue.add(newCompoundStartState)

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
                    var toState = CompoundState(transition.tos)
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

//            println("newCompoundStates: $newCompoundStates")
//            println("newCompoundTransitions: $newCompoundTransitions")

            val newStartState = State(newCompoundStartState)
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
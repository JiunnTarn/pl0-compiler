package model

import util.TagUtil
import java.util.*

class FA(
    var startState: State, var states: List<State>, var transitions: List<Transition>
) {
    private val alphabet: List<Char>
        get() {
            return transitions.filter { it.via != 'ε' }.groupBy { it.via }.keys.toList()
        }

    companion object {
        fun literal(via: Char): FA {
            val startState = State(tag = TagUtil.getNewTag().toString(), accepted = false)
            val acceptingState =
                State(tag = TagUtil.getNewTag().toString(), accepted = true)
            val transition = Transition(from = startState, via = via, tos = listOf(acceptingState))

            return FA(startState, listOf(startState, acceptingState), listOf(transition))
        }

        fun fromRE(re: String, tokenId: Int? = null): FA {
            val faStack = Stack<FA>()
            val opStack = Stack<Char>()
            var subRE = ""
            var cnt = 0
            var i = 0

            while (i < re.length) {
                var c = re[i]
                if (subRE.isEmpty()) {
                    when (c) {
                        '~' -> {
                            c = re[++i]
                            if (faStack.isNotEmpty() && (opStack.isEmpty() || opStack.peek() != '|')) {
                                opStack.push('-')
                            }
                            faStack.push(literal(c))
                        }

                        '*' -> {
                            val fa = faStack.pop()
                            val newFA = fa.closure()
                            faStack.push(newFA)
                        }

                        '|' -> {
                            if (opStack.isNotEmpty() && opStack.peek() == '-') {
                                var newFA: FA = faStack.pop()
                                while (opStack.isNotEmpty() && faStack.isNotEmpty() && opStack.peek() == '-') {
                                    opStack.pop()
                                    newFA = faStack.pop().concatenate(newFA)
                                }
                                faStack.push(newFA)
                            }
                            opStack.push(c)
                        }

                        '(' -> {
                            ++cnt
                            subRE = "("
                        }

                        else -> {
                            if (faStack.isNotEmpty() && (opStack.isEmpty() || opStack.peek() != '|')) {
                                opStack.push('-')
                            }
                            faStack.push(literal(c))
                        }
                    }
                } else {
                    subRE += c
                    if (c == '(') ++cnt
                    if (c == ')') --cnt
                    if (cnt == 0) {
                        val subFA = fromRE(subRE.substring(1..<subRE.length - 1))
                        if (faStack.isNotEmpty() && (opStack.isEmpty() || opStack.peek() != '|')) {
                            opStack.push('-')
                        }
                        faStack.push(subFA)
                        cnt = 0
                        subRE = ""
                    }
                }
                ++i
            }
            if (opStack.isNotEmpty()) {
                if (opStack.peek() == '-') {
                    var newFA: FA = faStack.pop()
                    while (opStack.isNotEmpty() && faStack.isNotEmpty() && opStack.peek() == '-') {
                        opStack.pop()
                        newFA = faStack.pop().concatenate(newFA)
                    }
                    faStack.push(newFA)
                }
                var newFA: FA = faStack.pop()
                while (opStack.isNotEmpty() && faStack.isNotEmpty() && opStack.peek() == '|') {
                    opStack.pop()
                    newFA = faStack.pop().union(newFA)
                }
                faStack.push(newFA)
            }


            val f = faStack.pop()
            f.states.filter { it.accepted }.forEach { it.tokenId = tokenId }
            return f
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

    private fun isNFA(): Boolean {
        return hasMultipleTos() || hasEpsilonSide()
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
        states = states.toMutableList().apply { addAll(fa2.states) }
            .apply { addAll(listOf(newStartState, newAcceptingState)) }.toList()

        insertTransition(newTransition)
        insertTransition(transition1)
        insertTransition(transition2)

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


    private fun epsilonClosure(entry: State): List<State> {
        val epsilonStates: MutableList<State> = ArrayList()
        val stack: Stack<State> = Stack()
        stack.add(entry)

        while (stack.isNotEmpty()) {
            val state = stack.pop()
            if (!epsilonStates.contains(state)) {
                epsilonStates.add(state)
            }
            transitions.filter { it.from == state && it.via == 'ε' }.forEach {
                for (s in it.tos) {
                    if (!epsilonStates.contains(s)) {
                        stack.push(s)
                    }
                }
            }
        }
        return epsilonStates
    }

    private fun epsilonClosure(entries: List<State>): List<State> {
        val epsilonStates: MutableList<State> = ArrayList()
        epsilonStates.addAll(entries)
        val stack: Stack<State> = Stack()
        stack.addAll(entries)

        while (stack.isNotEmpty()) {
            val s = stack.pop()
            transitions.filter { it.from == s && it.via == 'ε' }.forEach {
                for (to in it.tos) {
                    if (!epsilonStates.contains(to)) {
                        epsilonStates.add(to)
                        stack.add(to)
                    }
                }
            }
        }

        return epsilonStates
    }

    private fun move(entries: List<State>, via: Char): List<State> {
        val destinationStates: MutableSet<State> = HashSet()
        val viaTransitions = transitions.filter { it.via == via }

        for (entry in entries) {
            viaTransitions.filter { it.from == entry }.forEach {
                destinationStates.addAll(it.tos)
            }
        }

        return destinationStates.toList()
    }

    fun buildDFA(): FA {
        val dStates: MutableList<List<State>> = ArrayList()
        val newCompoundStates: MutableList<CompoundState> = ArrayList()
        val newStartStates = epsilonClosure(startState)
        val queue: Queue<List<State>> = LinkedList()
        val newCompoundTransitions: MutableList<CompoundTransition> = ArrayList()
        queue.add(newStartStates)

        while (queue.isNotEmpty()) {
            val t = queue.poll()
            val cs = CompoundState(t)
            newCompoundStates.add(cs)

            for (a in alphabet) {
                val u = epsilonClosure(move(t, a))
                if (u.isEmpty()) continue
                if (!dStates.contains(u)) {
                    dStates.add(u)
                    queue.add(u)
                }
                newCompoundTransitions.add(CompoundTransition(from = cs, via = a, tos = u))
            }
        }

        val stateMap: MutableMap<List<State>, State> = HashMap()
        val newStates = newCompoundStates.map { compoundState ->
            val s = State(compoundState)
            stateMap[compoundState.states] = s
            s
        }
        val newStartState = stateMap[newStartStates]!!
        val newTransitions = newCompoundTransitions.map { compoundTransition ->
            Transition(
                from = stateMap[compoundTransition.from.states]!!,
                via = compoundTransition.via,
                tos = listOf(stateMap[compoundTransition.tos]!!)
            )
        }
        return FA(
            startState = newStartState,
            states = newStates,
            transitions = newTransitions
        )
    }

    fun match(input: String): List<Token> {
        var i = 0

        val tokens = ArrayList<Token>()
        var longestMatch: Token? = null
        var longestPosition = 0
        var lastPosition = 0

        var pattern = this
        if (pattern.isNFA()) {
            pattern = pattern.buildDFA()
        }

        var currentState = pattern.startState
        while (i < input.length) {
            val c = input[i]
            val nextStates = pattern.move(listOf(currentState), c)
            if (nextStates.size != 1) {
                if (longestMatch == null) {
                    return listOf(ErrorToken())
                } else {
                    tokens.add(longestMatch)
                    longestMatch = null
                    lastPosition = i
                    currentState = pattern.startState
                    i = longestPosition + 1
                    continue
                }
            }
            currentState = nextStates[0]
            if (currentState.accepted) {
                longestMatch = currentState.tokenId?.let { Token(it, input.substring(lastPosition, i+1)) }
                longestPosition = i
            }
            ++i
        }
        if (longestPosition == input.length - 1) {
            if (longestMatch == null) {
                return listOf(ErrorToken())
            } else {
                tokens.add(longestMatch)
            }
        }
        return tokens
    }

    override fun toString(): String {
        return "FA(startState=$startState, states=$states, transitions=$transitions)"
    }
}
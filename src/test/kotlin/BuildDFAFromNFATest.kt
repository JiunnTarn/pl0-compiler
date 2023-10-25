//import model.*
//import model.TokenId
//
//fun main() {
//    val state0 = State(
//        tag = "A",
//        accepted = false,
//    )
//    val state1 = State(
//        tag = "B",
//        accepted = false,
//    )
//    val state2 = State(
//        tag = "C",
//        accepted = true,
//    )
//    val states = listOf(
//        state0,
//        state1,
//        state2,
//    )
//    val transition0 = Transition(
//        from = state0,
//        via = '0',
//        tos = listOf(
//            state0
//        )
//    )
//    val transition1 = Transition(
//        from = state0,
//        via = 'ε',
//        tos = listOf(
//            state1
//        )
//    )
//    val transition2 = Transition(
//        from = state1,
//        via = '1',
//        tos = listOf(
//            state1
//        )
//    )
//    val transition3 = Transition(
//        from = state1,
//        via = 'ε',
//        tos = listOf(
//            state2
//        )
//    )
//    val transition4 = Transition(
//        from = state2,
//        via = '2',
//        tos = listOf(
//            state2
//        )
//    )
//
//    val transitions = listOf(
//        transition0,
//        transition1,
//        transition2,
//        transition3,
//        transition4,
//    )
//    val nfa: FA = FA(
//        startState = state0,
//        states = states,
//        transitions = transitions
//    )
//    val dfa = nfa.buildDFA()
//    println(dfa)
//}

import model.FA
import model.State
import model.Transition

fun main() {
    val state0 = State(
        tag = "0",
        accepted = false,
    )
    val state1 = State(
        tag = "1",
        accepted = false,
    )
    val state2 = State(
        tag = "2",
        accepted = false,
    )
    val state3 = State(
        tag = "3",
        accepted = false,
    )
    val state4 = State(
        tag = "4",
        accepted = false,
    )
    val state5 = State(
        tag = "5",
        accepted = true,
    )
    val states = listOf(
        state0,
        state1,
        state2,
        state3,
        state4,
        state5,
    )
    val transition0 = Transition(
        from = state0,
        via = 'a',
        tos = listOf(
            state1
        )
    )
    val transition1 = Transition(
        from = state1,
        via = 'b',
        tos = listOf(
            state2
        )
    )
    val transition2 = Transition(
        from = state1,
        via = 'c',
        tos = listOf(
            state3
        )
    )
    val transition3 = Transition(
        from = state2,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition4 = Transition(
        from = state3,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition5 = Transition(
        from = state1,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition6 = Transition(
        from = state4,
        via = 'ε',
        tos = listOf(
            state1
        )
    )
    val transition7 = Transition(
        from = state4,
        via = 'ε',
        tos = listOf(
            state5
        )
    )
    val transitions = listOf(
        transition0,
        transition1,
        transition2,
        transition3,
        transition4,
        transition5,
        transition6,
        transition7,
    )
    val nfa = FA(
        startState = state0,
        states = states,
        transitions = transitions
    )
    val dfa = nfa.buildDFA()
    println("finish")
    println(dfa)
}
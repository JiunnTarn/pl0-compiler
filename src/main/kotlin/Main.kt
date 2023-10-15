import model.*
import model.TokenId

fun main(args: Array<String>) {
    val state0: State = State(
        id = "0",
        accepted = false,
        token = null,
    )
    val state1: State = State(
        id = "1",
        accepted = false,
        token = null,
    )
    val state2: State = State(
        id = "2",
        accepted = false,
        token = null,
    )
    val state3: State = State(
        id = "3",
        accepted = false,
        token = null,
    )
    val state4: State = State(
        id = "4",
        accepted = false,
        token = null,
    )
    val state5: State = State(
        id = "5",
        accepted = true,
        token = Token(
            id = TokenId.IDN,
            value = "ab"
        ),
    )
    val states = listOf(
        state0,
        state1,
        state2,
        state3,
        state4,
        state5,
    )
    val transition0: Transition = Transition(
        from = state0,
        via = 'a',
        tos = listOf(
            state1
        )
    )
    val transition1: Transition = Transition(
        from = state1,
        via = 'b',
        tos = listOf(
            state2
        )
    )
    val transition2: Transition = Transition(
        from = state1,
        via = 'c',
        tos = listOf(
            state3
        )
    )
    val transition3: Transition = Transition(
        from = state2,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition4: Transition = Transition(
        from = state3,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition5: Transition = Transition(
        from = state1,
        via = 'ε',
        tos = listOf(
            state4
        )
    )
    val transition6: Transition = Transition(
        from = state4,
        via = 'ε',
        tos = listOf(
            state1
        )
    )
    val transition7: Transition = Transition(
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
    val nfa: FA = FA(
        startState = state0,
        states = states,
        transitions = transitions
    )
    val dfa = nfa.buildDFA()
    println("finish")
    println(dfa)
}


//import model.*
//import model.TokenId
//
//fun main(args: Array<String>) {
//    val state0: State = State(
//        id = "A",
//        accepted = false,
//        token = null,
//    )
//    val state1: State = State(
//        id = "B",
//        accepted = false,
//        token = null,
//    )
//    val state2: State = State(
//        id = "C",
//        accepted = true,
//        token = null,
//    )
//    val states = listOf(
//        state0,
//        state1,
//        state2,
//    )
//    val transition0: Transition = Transition(
//        from = state0,
//        via = '0',
//        tos = listOf(
//            state0
//        )
//    )
//    val transition1: Transition = Transition(
//        from = state0,
//        via = 'ε',
//        tos = listOf(
//            state1
//        )
//    )
//    val transition2: Transition = Transition(
//        from = state1,
//        via = '1',
//        tos = listOf(
//            state1
//        )
//    )
//    val transition3: Transition = Transition(
//        from = state1,
//        via = 'ε',
//        tos = listOf(
//            state2
//        )
//    )
//    val transition4: Transition = Transition(
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
//    println("finish")
//    println(dfa)
//}
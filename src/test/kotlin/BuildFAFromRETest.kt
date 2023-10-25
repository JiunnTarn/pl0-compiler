import model.FA
import model.RE

//// (a|b)*ab|c*
//fun main() {
//    val re = "(a|b)*ab|c*"
//    val fa1 = FA.fromRE(re)
//
//    println(fa1)
//
//    val fa2 = fa1.buildDFA()
//    println(fa2)
//
//}

//// a(b|c)*
//fun main() {
//    val re = "a(b|c)*"
//    val fa1 = FA.fromRE(re)
//
//    println(fa1)
//
//    val fa2 = fa1.buildDFA()
//    println(fa2)
//
//}

// IDN
fun main() {
    val re = RE.IDN
    val fa1 = FA.fromRE(re)

//    println(fa1)

    val fa2 = fa1.buildDFA()
    println(fa2)

}


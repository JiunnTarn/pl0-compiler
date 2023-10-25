import model.FA
import model.RE
import model.TokenId

//// (a|b)*ab|c*
//fun main() {
//    val re = "(a|b)*ab|c*"
//    val fa1 = FA.fromRE(re)
//    val fa2 = fa1.buildDFA()
//
//
//    println(fa2.match("ababab"))
//    println(fa2.match("aaaa"))
//    println(fa2.match("ab"))
//    println(fa2.match("cccc"))
//    println(fa2.match("abc"))
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

//// IDN
//fun main() {
//    val re = RE.IDN
//    val fa1 = FA.fromRE(re)
//
//    val fa2 = fa1.buildDFA()
//    println(fa2.match("hello"))
//    println(fa2.match("123"))
//    println(fa2.match("i1"))
//    println(fa2.match("2_d"))
//    println(fa2.match("d_2"))
//
//}

//// IDN
//fun main() {
//    val re = RE.NUM
//    val fa1 = FA.fromRE(re)
//
//    val fa2 = fa1.buildDFA()
//    println(fa2.match("9999"))
//    println(fa2.match("123"))
//    println(fa2.match("1230"))
//    println(fa2.match("0"))
//    println(fa2.match("d_2"))
//
//}

// IDN
fun main() {
    val fa = FA.fromRE(RE.IDN, TokenId.IDN)
    val fa0 = FA.fromRE(RE.NUM, TokenId.NUM)
    val fa1 = FA.fromRE(RE.OP_GT, TokenId.OP_GT)
    val fa2 = FA.fromRE(RE.OP_GTE, TokenId.OP_GTE)
//    println(fa2)

    val fa3 = fa.union(fa0).union(fa1).union(fa2).buildDFA()

//    println(fa3)
    println(fa3.match(">"))
    println(fa3.match(">="))
    println(fa3.match("1230"))
    println(fa3.match("0"))
    println(fa3.match("2d_2"))

}


import model.FA

// (a|b)*ab|c*
fun main() {
    val fa1 = FA.literal('a')
    val fa2 = FA.literal('b')
    val fa3 = fa1.union(fa2)
    val fa4 = fa3.closure()
    val fa5 = FA.literal('c')
    val fa6 = fa5.closure()
    val fa7 = FA.literal('a')
    val fa8 = FA.literal('b')
    val fa9 = fa4.concatenate(fa7)
    val fa10 = fa9.concatenate(fa8)
    val fa11 = fa10.union(fa6)

    println(fa11)

    val fa12 = fa11.buildDFA()
    println(fa12)

}

//// a(b|c)*
//fun main() {
//    val fa1 = FA.literal('a')
//    val fa2 = FA.literal('b')
//    val fa3 = FA.literal('c')
//    val fa4 = fa2.union(fa3)
//    val fa5 = fa4.closure()
//    val fa6 = fa1.concatenate(fa5)
//
//    println(fa6)
//
//    val fa7 = fa6.buildDFA()
//    println(fa7)
//}

//// (a|b)*abb
//fun main() {
//    val fa1 = FA.literal('a')
//    val fa2 = FA.literal('b')
//    val fa3 = fa1.union(fa2)
//    val fa4 = fa3.closure()
//    val fa5 = FA.literal('a')
//    val fa6 = FA.literal('b')
//    val fa7 = FA.literal('b')
//
//    val fa8 = fa4.concatenate(fa5).concatenate(fa6).concatenate(fa7)
//
//    println(fa8)
//
//    val fa9 = fa8.buildDFA()
//    println(fa9)
//}

//// a|b, ab, a*
//fun main() {
//    val fa1 = FA.literal('a')
//    val fa2 = FA.literal('b')
//
//    println(fa1)
//    println(fa2)
//
//    println(fa1.union(fa2))
////    println(fa1.concatenate(fa2))
////    println(fa1.closure())
//}
import model.*

fun main() {
    val expression1 = Expression(1, "const declare", false, ArrayList())
    val expression2 = Expression(TokenId.KW_CST, "const", true, ArrayList())
    val expression3 = Expression(2, "const define", false, ArrayList())
    val expression4 = Expression(TokenId.IDN, "c", true, ArrayList())
    val expression5 = Expression(TokenId.OP_EQ, "=", true, ArrayList())
    val expression6 = Expression(TokenId.NUM, "0", true, ArrayList())
    val expression7 = Expression(ExpressionId.TEMP, "E1", false, ArrayList())
    val expression8 = Expression(TokenId.DEL_COM, ",", true, ArrayList())
    val expression9 = Expression(3, "const define", false, ArrayList())
    val expression10 = Expression(TokenId.IDN, "a", true, ArrayList())
    val expression11 = Expression(TokenId.OP_EQ, "=", true, ArrayList())
    val expression12 = Expression(TokenId.NUM, "5", true, ArrayList())
    val expression13 = Expression(ExpressionId.TEMP, "E1", false, ArrayList())
    val expression14 = Expression(ExpressionId.EPSILON, "ε", true, ArrayList())
    val expression15 = Expression(TokenId.DEL_STM, ";", true, ArrayList())

    val parseTreeNode1  = ParseTreeNode(expression1, ArrayList(), null)
    val parseTreeNode2  = ParseTreeNode(expression2, ArrayList(), null)
    val parseTreeNode3  = ParseTreeNode(expression3, ArrayList(), null)
    val parseTreeNode4  = ParseTreeNode(expression4, ArrayList(), null)
    val parseTreeNode5  = ParseTreeNode(expression5, ArrayList(), null)
    val parseTreeNode6  = ParseTreeNode(expression6, ArrayList(), null)
    val parseTreeNode7  = ParseTreeNode(expression7, ArrayList(), null)
    val parseTreeNode8  = ParseTreeNode(expression8, ArrayList(), null)
    val parseTreeNode9  = ParseTreeNode(expression9, ArrayList(), null)
    val parseTreeNode10 = ParseTreeNode(expression10, ArrayList(), null)
    val parseTreeNode11 = ParseTreeNode(expression11, ArrayList(), null)
    val parseTreeNode12 = ParseTreeNode(expression12, ArrayList(), null)
    val parseTreeNode13 = ParseTreeNode(expression13, ArrayList(), null)
    val parseTreeNode14 = ParseTreeNode(expression14, ArrayList(), null)
    val parseTreeNode15 = ParseTreeNode(expression15, ArrayList(), null)

    parseTreeNode1.children = arrayListOf(parseTreeNode2, parseTreeNode3, parseTreeNode7, parseTreeNode15)
    parseTreeNode3.children = arrayListOf(parseTreeNode4, parseTreeNode5, parseTreeNode6)
    parseTreeNode7.children = arrayListOf(parseTreeNode8, parseTreeNode9, parseTreeNode13)
    parseTreeNode9.children = arrayListOf(parseTreeNode10, parseTreeNode11, parseTreeNode12)
    parseTreeNode13.children = arrayListOf(parseTreeNode14)

    val parseTree = ParseTree(parseTreeNode1)

    println(parseTree)

    println()

    parseTree.cleanup()

    println(parseTree)
}
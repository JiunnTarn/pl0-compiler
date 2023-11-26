import model.*
import model.ExpressionId
import model.PRODUCTIONS
import util.TagUtil
import java.util.*

class Parser {
    private var programExpression: Expression

    init {
        val queue: Queue<String> = LinkedList()
        queue.addAll(PRODUCTIONS.productions)
        val expressionMap = HashMap<String, Expression>()
        while (queue.isNotEmpty()) {
            val productionString = queue.poll()
            val parts = productionString.split("→").map { it.trim() }
            val leftString = parts[0].substring(1, parts[0].length - 1)
            val rightString = parts[1]
            var leftExpression = expressionMap[leftString]
            if (leftExpression == null) {
                leftExpression = Expression(TagUtil.getNewTag(), leftString, false, ArrayList<Production>())
                expressionMap[leftString] = leftExpression
            }
            val right = ArrayList<Expression>()
            var i = 0
            while (i < rightString.length) {
                val c = rightString[i++]
                when (c) {
                    '<' -> {
                        var t = ""
                        while (i < rightString.length && rightString[i] != '>') {
                            t += rightString[i++]
                        }
                        var e = expressionMap[t]
                        if (e == null) {
                            e = Expression(TagUtil.getNewTag(), t, false, ArrayList<Production>())
                            expressionMap[t] = e
                        }
                        right.add(e)
                        if (rightString[i] == '>') {
                            i++
                        }
                    }

                    '\$' -> {
                        var t = ""
                        while (i < rightString.length && (t.isEmpty() || (!listOf('<', '\$', '|', '[', '{').contains(rightString[i])))) {
                            t += rightString[i++]
                        }
                        val e = Expression.parse(t)
                        right.add(e)
                    }

                    '|' -> {
                        val s = "<$leftString>→${rightString.substring(i)}"
                        queue.add(s)
                        break
                    }

                    '[' -> {
                        var t = ""
                        while (i < rightString.length && rightString[i] != ']') {
                            t += rightString[i++]
                        }
                        val e = Expression(ExpressionId.TEMP, "E${TagUtil.getNewTag()}", false, ArrayList<Production>())
                        right.add(e)
                        expressionMap[e.tag] = e
                        queue.add("<${e.tag}>→$t")
                        queue.add("<${e.tag}>→ε")
                        if (rightString[i] == ']') {
                            i++
                        }
                    }

                    '{' -> {
                        var t = ""
                        while (i < rightString.length && rightString[i] != '}') {
                            t += rightString[i++]
                        }
                        val e = Expression(ExpressionId.TEMP, "E${TagUtil.getNewTag()}", false, ArrayList<Production>())
                        right.add(e)
                        expressionMap[e.tag] = e
                        queue.add("<${e.tag}>→$t<${e.tag}>")
                        queue.add("<${e.tag}>→ε")
                        if (rightString[i] == '}') {
                            i++
                        }
                    }

                    'ε' -> {
                        val e = Expression.EPSILON
                        right.add(e)
                        i++
                    }
                }
            }


            leftExpression.productions.add(Production(leftExpression, right))
        }

        programExpression = expressionMap["PROGRAM"]!!
    }

    fun parse(input: List<Token>): ParseTree {
        val tokenStack = Stack<Expression>()
        input.reversed().forEach { tokenStack.push(Expression(it)) }


        val root = programExpression.buildParseTreeNode(tokenStack)
        val parseTree = ParseTree(root)
        parseTree.cleanup()

        return parseTree
    }
}
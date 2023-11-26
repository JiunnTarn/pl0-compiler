package model

import util.first
import java.util.*

data class Expression(
    val id: Int,
    var tag: String,
    val isTerminal: Boolean,
    val productions: ArrayList<Production>,
    val line: Int? = null
) {
    val isTemp: Boolean get() = id == ExpressionId.TEMP
    val isEpsilon: Boolean get() = id == ExpressionId.EPSILON
    private val isEmpty: Boolean get() = id == ExpressionId.EMPTY

    override fun toString(): String = StringBuilder(tag).run { line?.let { append("[#$it]") }; toString() }


    constructor(token: Token) : this(token.id, token.value!!, true, ArrayList<Production>(), token.line)

    fun firstSet(): Set<Expression> {
        val firstSet = FirstSet()
        if (isTerminal) {
            firstSet.add(this)
            return firstSet
        }
        productions.forEach {
            firstSet.addAll(it.firstSet())
        }
        return firstSet
    }

    fun buildParseTreeNode(input: Stack<Expression>): ParseTreeNode {
        if (isTerminal) {
            if(input.peek().id != id && !(isEpsilon || isEmpty)) {
                throw Exception("语法错误: ${input.peek()}")
            }
            val e = this.copy(line = input.peek().line)
            if (!(isEpsilon || isEmpty)) {
                if (id == TokenId.IDN || id == TokenId.NUM) {
                    e.tag = input.peek().tag
                }
                input.pop()
            }
            return ParseTreeNode(e)
        }
        val node = ParseTreeNode(this)
        val production = try {
             productions.first({ it.firstSet().contains(input.peek()) }, { it.isEpsilon() })
        } catch (e: NoSuchElementException) {
            println(node)
            throw Exception("语法错误: ${input.peek()}")
        }
        for (e in production.right) {
            val t = e.buildParseTreeNode(input)
            node.addChild(t)
        }
        return node
    }

    companion object {
        val EPSILON = Expression(ExpressionId.EPSILON, "ε", true, ArrayList(), null)
        val EMPTY = Expression(ExpressionId.EMPTY, "empty", true, ArrayList(), null)

        fun parse(expression: String): Expression {
            return EXPRESSIONS.expressions[expression] ?: throw Exception()
        }
    }
}


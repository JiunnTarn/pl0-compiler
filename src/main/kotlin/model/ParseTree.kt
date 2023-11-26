package model

data class ParseTree(
    val root: ParseTreeNode
) {
    fun cleanup() {
        root.cleanup()
    }

    override fun toString(): String {
        return root.print(1)
    }
}

data class ParseTreeNode(
    val expression: Expression,
    var children: ArrayList<ParseTreeNode>,
    val line: Int?
) {
    constructor(expression: Expression) : this(expression, ArrayList(), expression.line)

    fun addChild(child: ParseTreeNode) {
        children.add(child)
    }

    fun cleanup() {
        for (i in children.indices.reversed()) {
            val child = children[i]
            child.cleanup()

            if (child.expression.isTemp) {
                children.addAll(i + 1, child.children)
                children.removeAt(i)
            }
            if (child.expression.isEpsilon) {
                children.removeAt(i)
            }
        }
    }

    fun print(depth: Int): String {
        var res = "$expression"

        var indent = ""
        for (i in 1..<depth) {
            indent += "│   "
        }
        indent += "├───"
        if (children.isNotEmpty()) {
            for (child in children) {
                res += "\n$indent${child.print(depth + 1)}"
            }
        }
        return res
    }
}
package model

import CodeGenerator

data class ParseTree(
    val root: ParseTreeNode
) {
    fun cleanup() {
        root.cleanup()
    }

    fun compile() {
        root.compile(0)
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

    fun compile(curLevel: Int) {
        when (expression.tag) {
            "SUB_PROGRAM" -> {
                children.forEach { it.compile(curLevel) }
                CodeGenerator.addCode(Code("OPR", 0, 0))
            }

            "CONSTANT_DECLARE" -> {
                children.forEach {
                    if (it.expression.tag == "CONSTANT_DEFINE") {
                        check(it.children[0].expression.id == TokenId.IDN && it.children[2].expression.id == TokenId.NUM)
                        CodeGenerator.declareConst(
                            it.children[0].expression,
                            curLevel,
                            Integer.parseInt(it.children[2].expression.tag)
                        )
                    }
                }
            }

            "VARIABLE_DECLARE" -> {
                children.forEach {
                    if (it.expression.id == TokenId.IDN) {
                        CodeGenerator.allocateMemory(it.expression, curLevel)
                    }
                }
            }

            "PROCEDURE_DECLARE" -> {
                check(children[0].expression.tag == "PROCEDURE_HEAD")
                CodeGenerator.declareProc(children[0].children[1].expression, curLevel)

                children.forEach {
                    it.compile(curLevel + 1)
                }
            }

            "PROGRAM_BODY" -> {
                CodeGenerator.addCode(Code("INT", 0, CodeGenerator.getVariableCount(curLevel) + 3))
                children.forEach { it.compile(curLevel) }
            }

            "ASSIGNMENT" -> {
                children[2].compile(curLevel)
                val s = CodeGenerator.findSymbol(children[0].expression.tag)
                val l = curLevel - s.level
                CodeGenerator.addCode(Code("STO", l, s.address!!))
            }

            "CONDITION" -> {
                if (children.size == 3) {
                    children[0].compile(curLevel)
                    children[2].compile(curLevel)
                    CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(children[1].children[0].expression.id)))
                } else {
                    children[1].compile(curLevel)
                    CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(TokenId.KW_ODD)))
                }
            }

            "EXPRESSION" -> {
                var i = 0
                while (i < children.size) {
                    if (children[i].expression.tag == "ITEM") {
                        children[i].compile(curLevel)
                        i++
                    } else {
                        children[i + 1].compile(curLevel)
                        CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(children[i].children[0].expression.id)))
                        i += 2
                    }
                }
            }

            "ITEM" -> {
                var i = 0
                while (i < children.size) {
                    if (children[i].expression.tag == "FACTOR") {
                        children[i].compile(curLevel)
                        i++
                    } else {
                        children[i + 1].compile(curLevel)
                        CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(children[i].children[0].expression.id)))
                        i += 2
                    }
                }
            }

            "FACTOR" -> {
                if (children.size == 1) {
                    val child = children[0]
                    if (child.expression.id == TokenId.IDN) {
                        val s = CodeGenerator.findSymbol(child.expression.tag)
                        if (s.type == SymbolType.VARIABLE) {
                            CodeGenerator.addCode(Code("LOD", curLevel - s.level, s.address!!))
                        } else {
                            CodeGenerator.addCode(Code("LIT", curLevel - s.level, s.value!!))
                        }
                    } else {
                        CodeGenerator.addCode(Code("LIT", 0, Integer.parseInt(child.expression.tag)))
                    }
                } else {
                    children[1].compile(curLevel)
                }
            }

            "CALL_SENTENCE" -> {
                val s = CodeGenerator.findSymbol(children[1].expression.tag)
                CodeGenerator.addCode(Code("CAL", 0, s.address!!))
            }

            "READ_SENTENCE" -> {
                children.forEach {
                    if (it.expression.id == TokenId.IDN) {
                        CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(TokenId.KW_RD)))
                        val s = CodeGenerator.findSymbol(it.expression.tag)
                        CodeGenerator.addCode(Code("STO", curLevel - s.level, s.address!!))
                    }
                }
            }

            "WRITE_SENTENCE" -> {
                children.forEach {
                    if (it.expression.tag == "EXPRESSION") {
                        it.compile(curLevel)
                        CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(TokenId.KW_WR)))
                        CodeGenerator.addCode(Code("OPR", 0, CodeOpr.getOpr(TokenId.KW_WR) + 1))
                    }
                }
            }

            "WHILE_SENTENCE" -> {
                val entry = CodeGenerator.nextCodeIndex()
                children[1].compile(curLevel)
                val bodyEntry = CodeGenerator.nextCodeIndex()
                children[3].compile(curLevel)
                CodeGenerator.addCode(Code("JMP", 0, entry))
                val exit = CodeGenerator.nextCodeIndex() + 1
                CodeGenerator.insertCode(bodyEntry - 1, Code("JPC", 0, exit))
            }

            else -> {
                children.forEach { it.compile(curLevel) }
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
import model.Code
import model.Expression
import model.Symbol
import model.SymbolType
import javax.naming.NameNotFoundException

class CodeGenerator {
    fun printSymbolTable() {
        symbolTable.forEach { println(it) }
    }

    fun printCode() {
        for (i in codes.indices) {
            val instruction = codes[i]
            println("${i + 1}\t$instruction")
        }
    }

    companion object {
        private val symbolTable = mutableListOf<Symbol>()
        private val codes = mutableListOf<Code>()
        private var nextMemoryAddress = 3

        fun allocateMemory(expression: Expression, level: Int) {
            symbolTable.add(Symbol(expression.tag, SymbolType.VARIABLE, level, nextMemoryAddress))
            nextMemoryAddress++
        }

        fun getVariableCount(level: Int) = symbolTable.count { it.type == SymbolType.VARIABLE && it.level == level }

        fun declareConst(expression: Expression, level: Int, value: Int) {
            symbolTable.add(Symbol(expression.tag, SymbolType.CONSTANT, level, null, value))
        }

        fun declareProc(expression: Expression, level: Int): Int {
            val entry = codes.size + 1
            symbolTable.add(Symbol(expression.tag, SymbolType.PROCEDURE, level, entry))
            return entry
        }

        fun findSymbol(procName: String): Symbol {
            return symbolTable.find { it.name == procName }
                ?: throw NameNotFoundException("函数未定义")
        }

        fun addCode(code: Code): Int {
            codes.add(code)
            return codes.size
        }

        fun insertCode(index: Int, code: Code) {
            codes.add(index, code)
        }

        fun nextCodeIndex() = codes.size + 1
    }
}
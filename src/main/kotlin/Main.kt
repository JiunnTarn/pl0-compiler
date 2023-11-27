import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

fun main() {
    val lexer = Lexer()
    val parser = Parser()
    val codeGenerator = CodeGenerator()
    val interpreter = Interpreter()

    val inputFilePath = "./lexer_test.txt"
    val inputFile = File(inputFilePath)
    val inputStream: InputStream = inputFile.inputStream()
    val input = inputStream.readBytes().toString(Charset.defaultCharset())

    val tokenList = lexer.analyze(input)
    val parseTree = parser.parse(tokenList)
    parseTree.compile()

//    codeGenerator.printSymbolTable()
//    codeGenerator.printCodes()

    interpreter.interpret(codeGenerator.getCodes())
}
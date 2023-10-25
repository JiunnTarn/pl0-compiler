import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

fun main() {
    val lexer = Lexer()

    val inputFilePath = "./lexer_test.txt"
    val inputFile = File(inputFilePath)
    val inputStream: InputStream = inputFile.inputStream()
    val input = inputStream.readBytes().toString(Charset.defaultCharset())

    println(lexer.parse(input))
}
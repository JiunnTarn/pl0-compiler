package model

internal object TokenId {

    /**
     * KW: 保留字
     */
    const val KW_BGN = 100
    const val KW_END = 101
    const val KW_CLL = 102
    const val KW_RD = 103
    const val KW_WR = 104
    const val KW_WHI = 105
    const val KW_DO = 106
    const val KW_IF = 107
    const val KW_THN = 108
    const val KW_ELS = 109
    const val KW_CST = 110
    const val KW_VAR = 111
    const val KW_PROC = 112
    const val KW_REP = 113
    const val KW_UNT = 114
    const val KW_ODD = 115

    /**
     * DEL: 界符
     */
    const val DEL_COM = 200
    const val DEL_DEC = 201
    const val DEL_STM = 202
    const val DEL_LPAR = 203
    const val DEL_RPAR = 204
    const val DEL_LCBR = 205
    const val DEL_RCBR = 206

    /**
     * OP: 运算符
     */
    const val OP_ADD = 300
    const val OP_SUB = 301
    const val OP_MUL = 302
    const val OP_DIV = 303
    const val OP_INC = 304
    const val OP_DEC = 305
    const val OP_AST = 306
    const val OP_NEQ = 307
    const val OP_EQ = 308
    const val OP_GT = 309
    const val OP_GTE = 310
    const val OP_LT = 311
    const val OP_LTE = 312

    /**
     * NUM: 常数
     */
    const val NUM = 400

    /**
     * IDN: 标识符
     */
    const val IDN = 500

    /**
     * ERR: 解析失败
     */
    const val ERR = 0

}

internal object RE {
    private const val DIGIT = "0|1|2|3|4|5|6|7|8|9"
    private const val LETTER_ = "A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z|a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z|_"


    /**
     * KW: 保留字
     */
    const val KW_BGN = "begin"
    const val KW_END = "end"
    const val KW_CLL = "call"
    const val KW_RD = "read"
    const val KW_WR = "write"
    const val KW_WHI = "while"
    const val KW_DO = "do"
    const val KW_IF = "if"
    const val KW_THN = "then"
    const val KW_ELS = "else"
    const val KW_CST = "const"
    const val KW_VAR = "var"
    const val KW_PROC = "procedure"
    const val KW_REP = "repeat"
    const val KW_UNT = "until"
    const val KW_ODD = "odd"

    /**
     * DEL: 界符
     */
    const val DEL_COM = ","
    const val DEL_DEC = "."
    const val DEL_STM = ";"
    const val DEL_LPAR = "~("
    const val DEL_RPAR = "~)"
    const val DEL_LCBR = "{"
    const val DEL_RCBR = "}"

    /**
     * OP: 运算符
     */
    const val OP_ADD = "+"
    const val OP_SUB = "-"
    const val OP_MUL = "~*"
    const val OP_DIV = "/"
    const val OP_INC = "++"
    const val OP_DEC = "--"
    const val OP_AST = ":="
    const val OP_NEQ = "#"
    const val OP_EQ = "="
    const val OP_GT = ">"
    const val OP_GTE = ">="
    const val OP_LT = "<"
    const val OP_LTE = "<="

    /**
     * NUM: 常数
     */
    const val NUM = "(1|2|3|4|5|6|7|8|9)($DIGIT)*|0"

    /**
     * IDN: 标识符
     */
    const val IDN = "($LETTER_)($LETTER_|$DIGIT)*"
}
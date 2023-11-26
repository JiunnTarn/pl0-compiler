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

    // 以下的 '\' 为转义字符
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
    const val DEL_LPAR = "\\("
    const val DEL_RPAR = "\\)"
    const val DEL_LCBR = "{"
    const val DEL_RCBR = "}"

    /**
     * OP: 运算符
     */
    const val OP_ADD = "+"
    const val OP_SUB = "-"
    const val OP_MUL = "\\*"
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

internal object ExpressionId {
    /**
     * EPSILON: ε
     */
    const val EPSILON = 1000

    /**
     * EMPTY: 空语句
     */
    const val EMPTY = 1001

    /**
     * TEMP: 临时表达式
     */
    const val TEMP = 1002
}


internal object PRODUCTIONS {

    // <程序>	    →	<分程序>.
    // <分程序>	    →	[<常量说明部分>][<变量说明部分>][<过程说明部分>]<语句>
    // <常量说明部分>	→	CONST<常量定义>{,<常量定义>};
    // <常量定义>	    →	<标识符>=<无符号整数>
    // <无符号整数>	→	<数字>{<数字>}
    // <变量说明部分>	→	VAR<标识符>{,<标识符>};
    // <过程说明部分>	→	<过程首部><分程序>;[<过程说明部分>]
    // <过程首部>	    →	PROCEDURE<标识符>;
    // <语句>	    →	<赋值语句>|<条件语句>|<当型循环语句>|<过程调用语句>|<读语句>|<写语句>|<复合语句>|<空语句>
    // <赋值语句>	    →	<标识符>:=<表达式>
    // <复合语句>	    →	BEGIN<语句>{;<语句>}END
    // <条件>	    →	<表达式><关系运算符><表达式>|ODD<表达式>
    // <表达式>	    →	[<加减运算符>]<项>{<加减运算符><项>}
    // <项>	        →	<因子>{<乘除运算符><因子>}
    // <因子>	    →	<标识符>|<无符号整数>|(<表达式>)
    // <加减运算符>	→	+ | -
    // <乘除运算符>	→	* | /
    // <关系运算符>	→	= | # | < | <= | > | >=
    // <条件语句>	    →	IF<条件>THEN<语句>
    // <过程调用语句>	→	CALL<标识符>
    // <当型循环语句>	→	WHILE<条件>DO<语句>
    // <读语句>	    →	READ(<标识符>{,<标识符>})
    // <写语句>	    →	WRITE(<标识符>{,<标识符>})
    // <空语句>	    →	epsilon

    val productions = listOf(
        "<PROGRAM>→<SUB_PROGRAM>\$.",
        "<SUB_PROGRAM>→[<CONSTANT_DECLARE>][<VARIABLE_DECLARE>][<PROCEDURE_DECLARE>]<SENTENCE>",
        "<CONSTANT_DECLARE>→\$CONST<CONSTANT_DEFINE>{\$,<CONSTANT_DEFINE>}\$;",
        "<CONSTANT_DEFINE>→\$ID\$=\$NUM",
        "<VARIABLE_DECLARE>→\$VAR\$ID{\$,\$ID}\$;",
        "<PROCEDURE_DECLARE>→<PROCEDURE_HEAD><SUB_PROGRAM>\$;[<PROCEDURE_DECLARE>]",
        "<PROCEDURE_HEAD>→\$PROCEDURE\$ID\$;",
        "<SENTENCE>→<ASSIGNMENT>|<IF_SENTENCE>|<WHILE_SENTENCE>|<CALL_SENTENCE>|<READ_SENTENCE>|<WRITE_SENTENCE>|<COMBINED>|\$EMPTY",
        "<ASSIGNMENT>→\$ID\$:=<EXPRESSION>",
        "<COMBINED>→\$BEGIN<SENTENCE>{\$;<SENTENCE>}\$END",
        "<CONDITION>→<EXPRESSION><RELATIONAL_OPERATOR><EXPRESSION>|\$ODD<EXPRESSION>",
        "<EXPRESSION>→[<ADD_SUB_OPERATOR>]<ITEM>{<ADD_SUB_OPERATOR><ITEM>}",
        "<ITEM>→<FACTOR>{<MUL_DIV_OPERATOR><FACTOR>}",
        "<FACTOR>→\$ID|\$NUM|\$(<EXPRESSION>\$)",
        "<ADD_SUB_OPERATOR>→\$+|\$-",
        "<MUL_DIV_OPERATOR>→\$*|\$/",
        "<RELATIONAL_OPERATOR>→\$=|\$#|\$<|\$<=|\$>|\$>=",
        "<IF_SENTENCE>→\$IF<CONDITION>\$THEN<SENTENCE>",
        "<CALL_SENTENCE>→\$CALL\$ID",
        "<WHILE_SENTENCE>→\$WHILE<CONDITION>\$DO<SENTENCE>",
        "<READ_SENTENCE>→\$READ\$(<EXPRESSION>{\$,<EXPRESSION>}\$)",
        "<WRITE_SENTENCE>→\$WRITE\$(<EXPRESSION>{\$,<EXPRESSION>}\$)"
    )
}

internal object EXPRESSIONS {
    val expressions = mapOf(
        "." to Expression(Token(TokenId.DEL_DEC, ".")),
        "CONST" to Expression(Token(TokenId.KW_CST, "const")),
        "," to Expression(Token(TokenId.DEL_COM, ",")),
        ";" to Expression(Token(TokenId.DEL_STM, ";")),
        "ID" to Expression(Token(TokenId.IDN, "id")),
        "NUM" to Expression(Token(TokenId.NUM, "number")),
        "VAR" to Expression(Token(TokenId.KW_VAR, "var")),
        "PROCEDURE" to Expression(Token(TokenId.KW_PROC, "procedure")),
        "EMPTY" to Expression.EMPTY,
        ":=" to Expression(Token(TokenId.OP_AST, ":=")),
        "BEGIN" to Expression(Token(TokenId.KW_BGN, "begin")),
        "END" to Expression(Token(TokenId.KW_END, "end")),
        "ODD" to Expression(Token(TokenId.KW_ODD, "odd")),
        "(" to Expression(Token(TokenId.DEL_LPAR, "(")),
        ")" to Expression(Token(TokenId.DEL_RPAR, ")")),
        "+" to Expression(Token(TokenId.OP_ADD, "+")),
        "-" to Expression(Token(TokenId.OP_SUB, "-")),
        "*" to Expression(Token(TokenId.OP_MUL, "*")),
        "/" to Expression(Token(TokenId.OP_DIV, "/")),
        "=" to Expression(Token(TokenId.OP_EQ, "=")),
        "#" to Expression(Token(TokenId.OP_NEQ, "#")),
        "<" to Expression(Token(TokenId.OP_LT, "<")),
        "<=" to Expression(Token(TokenId.OP_LTE, "<=")),
        ">" to Expression(Token(TokenId.OP_GT, ">")),
        ">=" to Expression(Token(TokenId.OP_GTE, ">=")),
        "IF" to Expression(Token(TokenId.KW_IF, "if")),
        "THEN" to Expression(Token(TokenId.KW_THN, "then")),
        "CALL" to Expression(Token(TokenId.KW_CLL, "call")),
        "WHILE" to Expression(Token(TokenId.KW_WHI, "while")),
        "DO" to Expression(Token(TokenId.KW_DO, "do")),
        "READ" to Expression(Token(TokenId.KW_RD, "read")),
        "WRITE" to Expression(Token(TokenId.KW_WR, "write"))
    )
}
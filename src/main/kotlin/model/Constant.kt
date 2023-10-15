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

}
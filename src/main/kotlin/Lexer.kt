import model.FA
import model.RE
import model.Token
import model.TokenId
import java.util.regex.Pattern

class Lexer {
    companion object {
        private val fa = FA.fromRE(RE.KW_BGN, TokenId.KW_BGN)
            .union(FA.fromRE(RE.KW_END, TokenId.KW_END))
            .union(FA.fromRE(RE.KW_CLL, TokenId.KW_CLL))
            .union(FA.fromRE(RE.KW_RD, TokenId.KW_RD))
            .union(FA.fromRE(RE.KW_WR, TokenId.KW_WR))
            .union(FA.fromRE(RE.KW_WHI, TokenId.KW_WHI))
            .union(FA.fromRE(RE.KW_DO, TokenId.KW_DO))
            .union(FA.fromRE(RE.KW_IF, TokenId.KW_IF))
            .union(FA.fromRE(RE.KW_THN, TokenId.KW_THN))
            .union(FA.fromRE(RE.KW_ELS, TokenId.KW_ELS))
            .union(FA.fromRE(RE.KW_CST, TokenId.KW_CST))
            .union(FA.fromRE(RE.KW_VAR, TokenId.KW_VAR))
            .union(FA.fromRE(RE.KW_PROC, TokenId.KW_PROC))
            .union(FA.fromRE(RE.KW_REP, TokenId.KW_REP))
            .union(FA.fromRE(RE.KW_UNT, TokenId.KW_UNT))
            .union(FA.fromRE(RE.KW_ODD, TokenId.KW_ODD))

            .union(FA.fromRE(RE.DEL_COM, TokenId.DEL_COM))
            .union(FA.fromRE(RE.DEL_DEC, TokenId.DEL_DEC))
            .union(FA.fromRE(RE.DEL_STM, TokenId.DEL_STM))
            .union(FA.fromRE(RE.DEL_LPAR, TokenId.DEL_LPAR))
            .union(FA.fromRE(RE.DEL_RPAR, TokenId.DEL_RPAR))
            .union(FA.fromRE(RE.DEL_LCBR, TokenId.DEL_LCBR))
            .union(FA.fromRE(RE.DEL_RCBR, TokenId.DEL_RCBR))

            .union(FA.fromRE(RE.OP_ADD, TokenId.OP_ADD))
            .union(FA.fromRE(RE.OP_SUB, TokenId.OP_SUB))
            .union(FA.fromRE(RE.OP_MUL, TokenId.OP_MUL))
            .union(FA.fromRE(RE.OP_DIV, TokenId.OP_DIV))
            .union(FA.fromRE(RE.OP_INC, TokenId.OP_INC))
            .union(FA.fromRE(RE.OP_DEC, TokenId.OP_DEC))
            .union(FA.fromRE(RE.OP_AST, TokenId.OP_AST))
            .union(FA.fromRE(RE.OP_NEQ, TokenId.OP_NEQ))
            .union(FA.fromRE(RE.OP_EQ, TokenId.OP_EQ))
            .union(FA.fromRE(RE.OP_GT, TokenId.OP_GT))
            .union(FA.fromRE(RE.OP_GTE, TokenId.OP_GTE))
            .union(FA.fromRE(RE.OP_LT, TokenId.OP_LT))
            .union(FA.fromRE(RE.OP_LTE, TokenId.OP_LTE))

            .union(FA.fromRE(RE.NUM, TokenId.NUM))

            .union(FA.fromRE(RE.IDN, TokenId.IDN))
            .buildDFA()
    }

    fun analyze(input: String): List<Token> {
        val tokens = ArrayList<Token>()
        val lines = input.split("\n")
        for (l in lines.indices) {
            val line = lines[l]
            line.split(regex = Pattern.compile("[ \r\t]+")).forEach {
                tokens.addAll(fa.match(it, l + 1))
            }
        }
        return tokens
    }

}
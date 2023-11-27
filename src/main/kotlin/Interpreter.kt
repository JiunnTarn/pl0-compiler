import model.Code
import java.util.*
import kotlin.collections.ArrayList

class Interpreter {
    private lateinit var codes: List<Code>
    private var stack: ArrayList<Int> = ArrayList()

    init {
        for (i in 0..30) {
            stack.add(0)
        }
    }

    // 基址寄存器
    private var b = 0

    // 栈顶寄存器，指向栈顶的位置
    private var t = 0

    // 指令寄存器，存放的是当前要执行的中间代码
    private lateinit var i: Code

    // 存放下一条指令的列表下标，通过 mid_code [p] 获取指令
    private var p = 1


    private fun findAddress(b: Int, level: Int, adr: Int): Int {
        var res = b
        for (i in 1..level) {
            res = stack[res]
        }
        return res + adr
    }

    fun interpret(codes: List<Code>) {
        this.codes = codes

        while (p < codes.size) {
            i = codes[p-1]
            when (i.f) {
                "JMP" -> {
                    p = i.a
                    continue
                }

                "JPC" -> {
                    if (stack[t] == 0) {
                        p = i.a
                        continue
                    }
                    t--
                }

                "INT" -> {
                    t += i.a - 1
                }

                "LOD" -> {
                    stack[++t] = stack[findAddress(b, i.l, i.a)]
                }

                "LIT" -> {
                    stack[++t] = i.a
                }

                "STO" -> {
                    stack[findAddress(b, i.l, i.a)] = stack[t--]
                }

                "CAL" -> {
                    stack[++t] = b
                    b = t
                    stack[++t] = t - 2
                    stack[++t] = p
                    p = i.a
                }

                "OPR" -> {
                    when (i.a) {
                        1 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = x - y
                        }

                        2 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = x + y
                        }

                        4 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = x * y
                        }

                        5 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = x / y
                        }

                        6 -> {
                            val x = stack[t--]
                            stack[++t] = x % 2
                        }

                        8 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x == y) 1 else 0
                        }

                        9 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x != y) 1 else 0
                        }

                        10 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x < y) 1 else 0
                        }

                        11 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x >= y) 1 else 0
                        }

                        12 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x > y) 1 else 0
                        }

                        13 -> {
                            val y = stack[t--]
                            val x = stack[t--]
                            stack[++t] = if (x <= y) 1 else 0
                        }

                        14 -> {
                            println(stack[t--])
                        }

                        16 -> {
                            val read = Scanner(System.`in`)
                            stack[++t] = read.nextInt()
                        }

                        0 -> {
                            t = stack[b + 1]
                            p = stack[b + 2]
                            b = stack[b]
                        }
                    }
                }
            }
            p++
        }
    }
}
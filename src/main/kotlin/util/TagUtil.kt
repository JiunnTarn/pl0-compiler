package util

object TagUtil {
    private var cur = 0

    fun getNewTag(): Int {
        return ++cur
    }
}
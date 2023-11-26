package util

inline fun <T> Iterable<T>.first(predicate: (T) -> Boolean, lagWhen: (T) -> Boolean): T {
    var res: T? = null
    for (element in this) {
        if (predicate(element)) {
            if (!lagWhen(element)) {
                return element
            } else {
                if (res == null)
                    res = element
            }
        }
    }
    if (res != null) {
        return res
    }

    throw NoSuchElementException("Collection contains no element matching the predicate.")
}
//fun stringToMap(first: String, second: String, third: String) =
//    mapOf(first to first.length, second to second.length, third to third.length)


fun main() {
    val a = intArrayOf(9, 8, 3, 1, 5, 4)
    a.forEachIndexed { i, it ->
        if (it % 2 == 0)
            a[i] += 1
        else
            a[i] -= 1
    }

    println(a.joinToString(" "))
//    println(b.joinToString(" "))
}
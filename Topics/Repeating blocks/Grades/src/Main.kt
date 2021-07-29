fun main() {
    val values = Array(4) { 0 }
    for (n in 1..readLine()!!.toInt()) {
        values[readLine()!!.toInt() - 2]++
    }
    println(values.joinToString(" "))
}
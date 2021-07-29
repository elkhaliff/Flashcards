fun summator(map: Map<Int, Int>): Int {
    var count = 0
    map.forEach {
        if (it.key % 2 == 0) count += it.value
    }
    return count
}
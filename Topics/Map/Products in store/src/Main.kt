fun bill(priceList: Map<String, Int>, shoppingList: Array<String>): Int {
    var count = 0
    shoppingList.forEach {
        if (priceList.containsKey(it)) count += priceList[it]!!
    }
    return count
}
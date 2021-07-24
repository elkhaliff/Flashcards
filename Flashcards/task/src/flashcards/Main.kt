package flashcards

data class MemoryCard(val question: String, val answer: String) {
    fun test(str: String) =
        "Your answer is ${if (this.answer.equals(str, true)) "right!" else "wrong..."}"
}

class TestSystem(val cntCard: Int) {
    val arrCards: Array<MemoryCard> = arrayOf()

    private fun loadCard(i: Int) {
        val question = getString("Card #${i + 1}:")
        var answer = getString("The definition for card #${i + 1}:")
        arrCards[i] = MemoryCard(question, answer)
    }

    fun create() {
        for (i in 0..cntCard) loadCard(i)
    }

    fun testing() {
        arrCards.forEach { card ->
            val str = getString("Print the definition of \"${card.question}\":")
            println(card.test(str))
        }
    }
}

fun getString(vText: String): String {
    println(vText)
    return readLine()!!
}

fun main() {
    val cnt = getString("Input the number of cards:").toInt()
    val ts = TestSystem(cnt)
    ts.create()
    ts.testing()
}

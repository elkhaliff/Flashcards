package flashcards

import java.io.File
import java.lang.Exception
import kotlin.random.Random

data class MemoryCard(var question: String, var answer: String, var error: Int = 0) {
    fun testAnswer(str: String) = answer.equals(str, true)
    fun testQuestion(str: String) = question.equals(str, true)
}

class TestSystem {
    var exportFile = ""
    private val arrCards: MutableMap<Int, MemoryCard> = mutableMapOf()
    private val logList: MutableList<String> = mutableListOf()
    private val separator = ":"


    private fun String.log(list: MutableList<String>): String {
        list.add(this)
        return this
    }

    private fun getString(vText: String): String {
        println(vText.log(logList))
        val out = readLine()!!
        out.log(logList)
        return out
    }

    private fun getMaxId(): Int {
        var retId = 0
        arrCards.forEach { (id, _) ->
            if (id > retId) retId = id
        }
        return retId
    }

    private fun loadCard() {
        val question = getString("The card:")
        if (getIdByQuestion(question) != null) {
            println("The card \"$question\" already exists".log(logList))
            return
        }
        val answer = getString("The definition of the card:")
        if (getIdByAnswer(answer) != null) {
            println("The definition \"$answer\" already exists.".log(logList))
            return
        }
        arrCards[getMaxId() + 1] = MemoryCard(question, answer)
        println("The pair (\"$question\":\"$answer\") has been added.".log(logList))
    }

    private fun getIdByAnswer(str: String): Int? {
        arrCards.forEach { (id, card) ->
            if (card.testAnswer(str)) return id
        }
        return null
    }

    private fun getIdByQuestion(str: String): Int? {
        arrCards.forEach { (id, card) ->
            if (card.testQuestion(str)) return id
        }
        return null
    }

    private fun testing(cnt: Int) {
        var id: Int
        val defaultGenerator = Random.Default
        for (i in 1..cnt) {
            id = -1
            while (!arrCards.containsKey(id)) id = defaultGenerator.nextInt(0, arrCards.size + 1)
            val card = arrCards[id]!!
            val str = getString("Print the definition of \"${card.question}\":")
            val wrong = "Wrong. The right answer is \"${card.answer}\""
            if (card.testAnswer(str)) println("Correct!".log(logList))
            else {
                val idRight: Int? = getIdByAnswer(str)
                val strOut = if (idRight != null) "$wrong, but your definition is correct for \"${arrCards[idRight]!!.question}\"." else "$wrong."
                card.error++
                println(strOut.log(logList))
            }
        }
    }

    private fun removeCard() {
        val question = getString("Which card?")
        val id = getIdByQuestion(question)
        if (id == null) println("Can't remove \"$question\": there is no such card.".log(logList))
        else {
            arrCards.remove(id)
            println("The card has been removed.".log(logList))
        }
    }

    fun import(fileName_: String = "") {
        val fileName = if (fileName_ == "") getString("File name:") else fileName_
        var cnt = 0
        try {
            val layout = File(fileName).readLines()
            for (row in layout) {
                val question = row.substringBefore(separator)
                val temp = row.substringAfter(separator)
                val answer = temp.substringBefore(separator)
                val error = temp.substringAfter(separator).toInt()
                val id = getIdByQuestion(question)
                if (id != null) {
                    arrCards[id]!!.answer = answer
                    arrCards[id]!!.error = error
                    cnt++
                } else {
                    arrCards[getMaxId() + 1] = MemoryCard(question, answer, error)
                    cnt++
                }
            }
            println("$cnt cards have been loaded.".log(logList))
        } catch (e: Exception) {
            println("File not found.".log(logList))
        }
    }

    private fun export(fileName_: String = "") {
        val fileName = if (fileName_ == "") getString("File name:") else fileName_
        File(fileName).writeText("")
        arrCards.forEach { (_, card) ->
            File(fileName).appendText("${card.question}$separator${card.answer}$separator${card.error}\n")
        }
        println("${arrCards.size} cards have been saved.".log(logList))
    }

    private fun ask() =
        testing(getString("How many times to ask?").toInt())

    fun getMenu() {
        var doIt = true
        while (doIt) {
            when (getString("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")) {
                "add" -> loadCard()
                "remove" -> removeCard()
                "import" -> import()
                "export" -> export()
                "ask" -> ask()
                "log" -> log()
                "hardest card" -> hardestCard()
                "reset stats" -> resetStats()
                "exit" -> {
                    if (exportFile != "") export(exportFile)
                    doIt = false
                }
            }
        }
        println("Bye bye!")
    }

    private fun log() {
        val fileName = getString("File name:")
        File(fileName).writeText("")
        logList.forEach {
            File(fileName).appendText("$it\n")
        }
        println("The log has been saved.".log(logList))
    }

    private fun hardestCard() {
        var error = 0
        val errList: MutableList<String> = mutableListOf()
        arrCards.forEach { (_, card) ->
            if (card.error > error) {
                errList.clear()
                errList.add("\"${card.question}\"")
                error = card.error
            } else if (card.error != 0 && card.error == error) {
                errList.add("\"${card.question}\"")
            }
        }
        when {
            error == 0 -> println("There are no cards with errors.".log(logList))
            errList.size > 1 -> println("The hardest cards are ${errList.joinToString(", ")}." +
                    " You have $error errors answering them.".log(logList))
            else -> println("The hardest card is ${errList[0]}. You have $error errors answering it.".log(logList))
        }
    }

    private fun resetStats() {
        arrCards.forEach { (_, card) ->
            card.error = 0
        }
        println("Card statistics have been reset.".log(logList))
    }
}

fun main(args: Array<String>) {
    val ts = TestSystem()
    if (args.size > 1) {
        args.forEachIndexed { index, s ->
            if (index == 0 || index == 2) {
                when (s) {
                    "-import" -> ts.import(args[index+1])
                    "-export" -> ts.exportFile = args[index+1]
                }

            }
        }
    }
    ts.getMenu()
}
package me.camdenorrb.timorforest.tree

/*
import kotlin.math.pow

val trainingData0 = listOf(
    listOf("Green", 3, "Apple"),
    listOf("Yellow", 3, "Apple"),
    listOf("Red", 1, "Grape"),
    listOf("Red", 1, "Grape"),
    listOf("Yellow", 3, "Lemon")
)

val trainingData1 = listOf(
    listOf("Black", 2, "Cat"),
    listOf("Brown", 3, "Duck"),
    listOf("White", 4, "Dog")

)


fun main() {

    val tree = buildTree(trainingData1)

    //println(tree.data(listOf("dwedewwe", 3)))
    printTree(tree)
}


fun buildTree(rows: List<List<Any>>): Tree {

    val (gain, question) = findBestSplit(rows)

    if (gain == 0.0) {
        return Leaf(classCounts(rows))
    }


    val (trueRows, falseRows) = rows.partition { question!!.match(it) }

    val trueBranch = buildTree(trueRows)
    val falseBranch = buildTree(falseRows)


    return Node(question!!, trueBranch, falseBranch)
}


fun findBestSplit(rows: List<List<Any>>): Pair<Double, Question?> {

    var bestGain = 0.0
    var bestQuestion: Question? = null


    val currentUncertainty = gini(rows)

    val nFeatures = rows[0].size - 1


    for (column in 0 until nFeatures) {
        val values = rows.fold(mutableListOf<Any>()) { list, next ->
            list.add(next[column])
            list
        }


        for (value in values) {

            val question = Question(column, value)

            val (trueRows, falseRows) = rows.partition { question.match(it) }

            if (trueRows.isEmpty() || falseRows.isEmpty()) {
                continue
            }

            val gain = infoGain(trueRows, falseRows, currentUncertainty)


            if (gain >= bestGain) {
                bestGain = gain
                bestQuestion = question
            }

        }

    }


    return bestGain to bestQuestion
}

fun infoGain(trueRows: List<List<Any>>, falseRows: List<List<Any>>, currentUncertainty: Double): Double {
    val p = trueRows.size.toDouble() / (trueRows.size.toDouble() + falseRows.size.toDouble())

    return currentUncertainty - (p * gini(trueRows)) - (1 - p) * gini(falseRows)
}

fun gini(rows: List<List<Any>>): Double {

    val counts = classCounts(rows)

    var impurity = 1.0

    for (value in counts.values) {

        val probOfLabel = value.toDouble() / rows.size.toDouble()
        impurity -= probOfLabel.pow(2.0)
    }

    return impurity
}

fun classCounts(rows: List<List<Any>>): Map<String, Int> {

    val counts = mutableMapOf<String, Int>()

    for (row in rows) {
        counts.compute(row[row.size - 1] as String) { s: String, i: Int? ->
            (i ?: 0) + 1
        }
    }

    return counts
}


fun printTree(tree: Tree, spacing: String = " ")
{
    when(tree)
    {
        is Leaf ->
        {
            println("${spacing}Predict: ${tree.predictions}")
        }
        is Node ->
        {
            println("${spacing}${tree.question}")

            println("${spacing}-> True:")
            printTree(tree.trueBranch, "$spacing  ")

            println("${spacing}-> False:")
            printTree(tree.falseBranch, "$spacing  ")
        }
    }
}

fun printLeaf(counts: Map<String, Int>): Map<String, String>
{

    var total = 0.0

    for (value in counts.values) {
        total += value
    }

    val probs = mutableMapOf<String, String>()

    for ((k, v) in counts) {
        probs[k] = "${v / total * 100}%"
    }

    return probs
}

class Leaf(val predictions: Map<String, Int>) : Tree {

    override fun data(rows: List<Any>): Map<String, Int> {
        return predictions
    }
}

class Node(val question: Question, val trueBranch: Tree, val falseBranch: Tree) : Tree {

    override fun data(rows: List<Any>): Map<String, Int> {
        return if (question.match(rows)) {
            trueBranch.data(rows)
        } else {
            falseBranch.data(rows)
        }
    }

}

data class Question(val column: Int, val value: Any) {

    fun match(rows: List<Any>): Boolean {

        val value = rows[column]

        if (value is Number) {
            return value.toInt() >= this.value as Int
        }

        return value == this.value
    }

}

interface Tree {

    fun data(rows: List<Any>): Map<String, Int>

}*/
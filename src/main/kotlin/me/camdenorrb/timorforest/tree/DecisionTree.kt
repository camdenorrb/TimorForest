package me.camdenorrb.timorforest.tree

import me.camdenorrb.timorforest.ext.partition
import me.camdenorrb.timorforest.node.base.NodeBase
import kotlin.math.pow

// TODO: Change comparable to string, you can use .toDoubleOrNull to determine if it's a number
class DecisionTree {

    private lateinit var root: NodeBase<*>

    var isTrained = false
        private set


    /**
     * Trains the decision tree with fortune and knowledge!
     *
     * @param inputs Declares the input values [[value, value, label]]
     */
    fun train(inputs: List<List<Comparable<*>>>) {

        check(!isTrained) {
            "This decision tree has already been trained!"
        }

        root = buildTree(inputs)

        isTrained = true
    }

    /**
     * Attempts to predict the label for the [row]
     *
     * @param row Declares the row of information to inspect
     *
     * @return The label the tree suspects
     */
    fun predict(row: List<Comparable<*>>): Leaf {

        var node = root

        while (true) {

            when(node) {

                is Leaf -> return node
                is Node -> node = if (node.value.match(row)) node.trueBranch else node.falseBranch

                else -> error("Invalid node type ${node::class.simpleName}")
            }

        }
    }


    private fun buildTree(inputs: List<List<Comparable<*>>>): NodeBase<*> {

        val (gain, question) = findBestSplit(inputs)

        if (gain == 0.0) {
            return Leaf(countLabels(inputs))
        }

        val (trueRows, falseRows) = partition(inputs, question)

        val trueBranch = buildTree(trueRows)

        val falseBranch = buildTree(falseRows)

        return Node(question, trueBranch, falseBranch)
    }

    private fun findBestSplit(inputs: List<List<Comparable<*>>>): Pair<Double, Question> {

        var bestGain = 0.0

        var bestQuestion: Question? = null

        val uncertainty = gini(inputs)


        // toSet to avoid duplicates
        val columns = (0..inputs[0].size - 2).map { index -> inputs.map { it[index] }.toSet() }

        columns.forEachIndexed { column, values ->

            println(values)

            values.forEach { value ->

                val question = Question(column, value)

                val (trueRows, falseRows) = partition(inputs, question)

                if (trueRows.isEmpty() || falseRows.isEmpty()) {
                    return@forEachIndexed
                }

                val gain = infoGain(trueRows, falseRows, uncertainty)

                if (gain >= bestGain) {
                    bestGain = gain
                    bestQuestion = question
                }
            }
        }

        return bestGain to bestQuestion!!
    }

    private fun countLabels(rows: List<List<Comparable<*>>>): Map<Comparable<*>, Int> {

        val labelCount = mutableMapOf<Comparable<*>, Int>()

        rows.forEach {
            val last = it.last()
            labelCount[last] = labelCount.getOrDefault(last, 0) + 1
        }

        return labelCount
    }

    /**
     * Use [partition] to call this
     */
    private fun infoGain(trueRows: List<List<Comparable<*>>>, falseRows: List<List<Comparable<*>>>, uncertainty: Double): Double {
        val score = trueRows.size.toDouble() / (trueRows.size + falseRows.size)
        return uncertainty - score * gini(trueRows) - (1 - score) * gini(falseRows)
    }


    /**
     *
     *
     * @param rows
     * @param question
     * @return
     */
    private fun partition(rows: List<List<Comparable<*>>>, question: Question?): Pair<List<List<Comparable<*>>>, List<List<Comparable<*>>>> {
        return rows.partition { question?.match(it) ?: false }
    }


    /**
     * TODO
     *
     * @property column
     * @property value
     */
    data class Question(val column: Int, val value: Comparable<*>) {

        fun match(example: List<Comparable<*>>): Boolean {

            val exampleValue = example[column]

            if (value is Number) {
                return (exampleValue as Number).toDouble() >= value.toDouble()
            }

            return exampleValue == value
        }

    }


    private fun prettyText(node: NodeBase<*>, indent: String = ""): String {
        return when(node) {

            is Leaf -> "Predict: ${node.value}".prependIndent(indent)
            is Node -> "${node.value} \n--> True: \n${prettyText(node.trueBranch, "$indent  ")} \n--> False: \n${prettyText(node.falseBranch, "$indent  ")}".prependIndent(indent)

            else -> error("Unknown node type!")
        }
    }

    private fun gini(rows: List<List<Comparable<*>>>): Double {

        var impurity = 1.0

        val labelCount = countLabels(rows)

        val rowSize = rows.size.toDouble()

        labelCount.forEach { (_, count) ->
            val probOfLabel = count / rowSize
            impurity -= probOfLabel.pow(2.0)
        }

        return impurity
    }


    override fun toString(): String {
        return prettyText(root)
    }


    data class Leaf(override val value: Map<Comparable<*>, Int>) : NodeBase<Map<Comparable<*>, Int>>

    data class Node(override val value: Question, val trueBranch: NodeBase<*>, val falseBranch: NodeBase<*>) : NodeBase<Question>

}

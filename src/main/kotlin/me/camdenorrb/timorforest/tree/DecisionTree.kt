package me.camdenorrb.timorforest.tree

import me.camdenorrb.timorforest.ext.partition
import me.camdenorrb.timorforest.node.base.NodeBase
import kotlin.math.pow

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
    fun predict(row: List<Comparable<*>>): List<String> {

        var node = root

        while (true) {

            when(node) {

                is Leaf -> return node.value
                is Node -> node = if (node.value.match(row)) node.trueBranch else node.falseBranch

                else -> error("Invalid node type ${node::class.simpleName}")
            }

        }
    }


    private fun buildTree(inputs: List<List<Comparable<*>>>): NodeBase<*> {

        val (gain, question) = bestSplitFor(inputs)

        if (gain == 0.0) {
            return Leaf(inputs.flatten().map { "$it" })
        }

        val (trueRows, falseRows) = partition(inputs, question)

        val trueBranch = buildTree(trueRows)

        val falseBranch = buildTree(falseRows)

        return Node(question, trueBranch, falseBranch)
    }

    private fun bestSplitFor(inputs: List<List<Comparable<*>>>): Pair<Double, Question> {

        var bestGain = 0.0

        var bestQuestion: Question? = null

        val uncertainty = Impurity.GINI(inputs)


        inputs.forEachIndexed { column, values ->

            // toSet to remove duplicates
            values.toSet().forEach { value ->

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

    /**
     * Use [partition] to call this
     */
    private fun infoGain(trueRows: List<List<Comparable<*>>>, falseRows: List<List<Comparable<*>>>, uncertainty: Double): Double {
        val score = trueRows.size / (trueRows.size + falseRows.size)
        return uncertainty - score * Impurity.GINI(trueRows) - (1 - score) * Impurity.GINI(falseRows)
    }


    /**
     *
     *
     * @param rows
     * @param question
     * @return
     */
    private fun partition(rows: List<List<Comparable<*>>>, question: Question): Pair<List<List<Comparable<*>>>, List<List<Comparable<*>>>> {
        return rows.partition { question.match(it) }
    }


    /**
     * TODO
     *
     * @property column
     * @property value
     */
    data class Question(val column: Int, val value: Comparable<*>) {

        fun match(example: List<Comparable<*>>): Boolean {

            if (value is Number) {
                return (example[column] as Number).toDouble() >= value.toDouble()
            }

            return example[column] == value
        }

    }


    data class Leaf(override val value: List<String>) : NodeBase<List<String>>

    data class Node(override val value: Question, val trueBranch: NodeBase<*>, val falseBranch: NodeBase<*>) : NodeBase<Question>


    // Types used to calculate impurity for a list of rows
    enum class Impurity {

        GINI {

            override fun invoke(inputs: List<List<Comparable<*>>>): Double {

                var impurity = 1.0

                val rowSize = inputs.size

                val labelSizes = inputs.map { it.size.toDouble() }

                labelSizes.forEach { size ->
                    val probOfLabel = size / rowSize // Should be the same?
                    impurity -= probOfLabel.pow(2.0)
                }

                return impurity
            }

        };


        abstract operator fun invoke(inputs: List<List<Comparable<*>>>): Double

    }

}

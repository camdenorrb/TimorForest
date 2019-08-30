package me.camdenorrb.timorforest.tree

import me.camdenorrb.timorforest.node.base.NodeBase
import kotlin.math.pow

class DecisionTree(val header: List<String>, val trainingData: List<List<Any>>) {

    var root: NodeBase<*>


    init {
        root = buildTree(trainingData)
    }


    fun classCounts(rows: List<List<Any>>): MutableMap<Any, Int> {

        val labelCount = mutableMapOf<Any, Int>()

        rows.forEach {
            val label = it.last()
            labelCount[label] = labelCount.getOrDefault(label, 0) + 1
        }

        return labelCount
    }

    inner class Question(val column: Int, val value: Any) {

        fun match(example: List<Any>): Boolean {

            val exampleValue = example[column]

            return if (value is Number) {
                (exampleValue as Number).toInt() >= value.toInt()
            }
            else {
                value == exampleValue
            }

        }

        override fun toString(): String {
            val condition = if (value is Number) ">=" else "=="
            return "Is ${header[column]} $condition $value"
        }

    }


    fun gini(rows: List<List<Any>>): Double {

        val counts = classCounts(rows)
        var impurity = 1.0

        counts.forEach { lbl, count ->
            val probOfLbl = count / rows.size.toDouble()
            impurity -= probOfLbl.pow(2)
        }

        return impurity
    }

    fun infoGain(left: List<List<Any>>, right: List<List<Any>>, uncertainty: Double): Double {
        val p = left.size.toDouble() / (left.size + right.size)
        return uncertainty - p * gini(left) - (1 - p) * gini(right)
    }

    fun findBestSplit(rows: List<List<Any>>): Pair<Double, Question?> {

        var bestGain = 0.0
        var bestQuestion: Question? = null
        val uncertainty = gini(rows)
        val nFeatures = rows[0].size - 1

        for (col in 0 until nFeatures) {

            rows.map { it[col] }.toSet().forEach { value ->

                val question = Question(col, value)

                val (trueRows, falseRows) = rows.partition { question.match(it) }

                if (trueRows.isEmpty() || falseRows.isEmpty()) {
                    return@forEach
                }

                val gain = infoGain(trueRows, falseRows, uncertainty)

                if (gain >= bestGain) {
                    bestGain = gain
                    bestQuestion = question
                }
            }

        }

        return bestGain to bestQuestion
    }


    private fun buildTree(inputs: List<List<Any>>): NodeBase<*> {

        val (gain, question) = findBestSplit(inputs)

        if (gain == 0.0 || question == null) {
            return Leaf(classCounts(inputs))
        }

        val (trueRows, falseRows) = inputs.partition { question.match(it) }

        val trueBranch = buildTree(trueRows)

        val falseBranch = buildTree(falseRows)

        return Node(question, trueBranch, falseBranch)
    }

    /*
    fun predict(row: List<Comparable<*>>): Leaf {

        var node = root

        while (true) {

            when(node) {

                is Leaf -> return node
                is Node -> node = if (node.value.match(row)) node.trueBranch else node.falseBranch

                else -> error("Invalid node type ${node::class.simpleName}")
            }

        }
    }*/

    fun predict(row: List<Any>): Leaf {

        var node = root

        while (true) {

            when(node) {

                is Leaf -> return node
                is Node -> node = if (node.value.match(row)) node.trueBranch else node.falseBranch

                else -> error("Invalid node type ${node::class.simpleName}")
            }

        }
    }

    private fun prettyText(node: NodeBase<*>, indent: String = ""): String {
        return when(node) {

            is Leaf -> "Predict: ${node.value}".prependIndent(indent)
            is Node -> "${node.value} \n--> True: \n${prettyText(node.trueBranch, "$indent  ")} \n--> False: \n${prettyText(node.falseBranch, "$indent  ")}".prependIndent(indent)

            else -> error("Unknown node type!")
        }
    }

    override fun toString(): String {
        return prettyText(root)
    }


    data class Leaf(override val value: Map<Any, Int>) : NodeBase<Map<Any, Int>>

    data class Node(override val value: Question, val trueBranch: NodeBase<*>, val falseBranch: NodeBase<*>) : NodeBase<Question>

}
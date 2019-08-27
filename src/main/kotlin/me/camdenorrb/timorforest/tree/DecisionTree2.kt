package me.camdenorrb.timorforest.tree

import me.camdenorrb.timorforest.node.base.NodeBase
import kotlin.math.pow

class DecisionTree2(val header: List<String>, val trainingData: List<List<Any>>) {

    fun uniqueVals(rows: List<List<Any>>, column: Int): Set<Any> {
        return rows.map { it[column] }.toSet()
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
                value.toDouble() >= (exampleValue as Number).toDouble()
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

    fun partition(rows: List<List<Any>>, question: Question): Pair<List<List<Any>>, List<List<Any>>> {
        return rows.partition { question.match(it) }
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

    fun findBestSplit(rows: List<List<Any>>) {

        var bestGain = 0
        var bestQuestion: Question? = null

    }


    data class Leaf(override val value: Map<Any, Int>) : NodeBase<Map<Any, Int>>

    data class Node(override val value: DecisionTree.Question, val trueBranch: NodeBase<*>, val falseBranch: NodeBase<*>) : NodeBase<DecisionTree.Question>

}
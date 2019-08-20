package me.camdenorrb.timorforest.tree

import kotlin.math.pow

/**
 * TODO
 *
 * @property columnLabels
 * @property nodeSize
 * @property maxNodes
 */
// https://github.com/random-forests/tutorials/blob/master/decision_tree.ipynb
// https://github.com/random-forests/tutorials/blob/master/decision_tree.py
class DecisionTree(val columnLabels: List<String>, val minLeafs: Int = 1, val maxNodes: Int = 100) {

    // TODO: Find what the root value should be
    //private val root = Node(columnLabels)

    // (Type --> Values)
    private val trainedData = mutableMapOf<String, MutableList<DoubleArray>>()

    var isBuilt = false
        private set

    var sampleRate = 1.0
        set(value) {

            check(value in 0..1) {
                "Sample rate needs to be between 0 and 1"
            }

            field = value
        }

    // The weights for all the columnLabels
    // Between [0, 1]
    var weights = List(columnLabels.size) { 1.0 }
        set(value) {

            check(value.size == columnLabels.size) {
                "The amount of weights should equal the amount of [columnLabels]"
            }

            field = value
        }


    init {
        check(maxNodes >= 2) {
            "A tree needs at least 2 nodes"
        }
        check(minLeafs >= 1) {
            "A tree needs at least 1 leaf"
        }
    }


    /**
     * Trains the decision tree with fortune and knowledge!
     *
     * @param inputs Declares the input values (Type --> Values)
     * //@param outputs Declares the [outputs] for these [inputs]
     */
    fun train(inputs: Map<String, DoubleArray>) {

        // Early fail if invalid input size
        check(inputs.values.none { it.size != columnLabels.size }) {
            "The amount of values needs to equal the amount of [columnLabels]"
        }

        inputs.forEach { (type, data) ->
            trainedData.getOrPut(type, { mutableListOf() }).add(data)
        }
        // TODO: Use sets to remove duplicates
        // TODO: Add all the data to the rootNode

        build()
    }

    /**
     * Attempts to predict the expected output for the [inputs]
     *
     * @param inputs Declares the known information to look for
     *
     * @return The label the tree suspects
     */
    fun predict(inputs: DoubleArray): String {

        check(isBuilt) {
            "The tree has to be built in order to predict, please train data"
        }

        return TODO()
    }


    /**
     * Builds the root node for the tree, needs to be called before use
     */
    private fun build() {

        trainedData.values.toSet().partition {  }

        // Don't check if it's built here since it could be for rebuilding
        isBuilt = true
    }
}

/*
private fun bestSplitFor(inputs: List<DoubleArray>): Double {

    var bestGain = 0.0

    val uncertainty = Impurity.GINI(inputs)

    inputs.forEachIndexed { column, values ->

        val (trueRows, falseRows) = values.toSet().partition { column >= it }

        if (trueRows.isEmpty() || falseRows.isEmpty()) {
            return@forEachIndexed
        }

        val gain = infoGain(trueRows, falseRows, uncertainty)

        if (gain >=bestGain) {
            bestGain = gain
        }
    }

    return bestGain
}*/

private fun infoGain(trueRows: List<Double>, falseRows: List<Double>, uncertainty: Double): Double {
    val score = trueRows.size / (trueRows.size + falseRows.size)
    return uncertainty - score * Impurity.GINI(trueRows) - (1 - score) * Impurity.GINI(falseRows)
}


// Types used to calculate impurity for a list of rows
enum class Impurity {

    GINI {

        override fun invoke(vararg inputs: List<Double>): Double {

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


    internal abstract operator fun invoke(vararg inputs: List<Double>): Double

}

/*
enum class Question(val operator: String, vararg val compareResults: Int) {

    NONE(""),
    IS_LESS_THAN("<", -1),
    IS_EQUAL_OR_LESS_THAN("<=", -1, 0),
    IS_EQUAL_TO("==", 0),
    IS_EQUAL_OR_GREATER_THAN(">=", 0, 1),
    IS_GREATER_THAN(">", 1);

    override fun toString(): String {
        return operator
    }

    companion object {

        fun <T : Comparable<T>> checkIf(`this`: T, question: Question, other: T): Boolean {
            return question.compareResults.contains(`this`.compareTo(other))
        }

    }

}*/


/*

fun split(splitMethod: SplitMethod) {

    check(!isSplit) {
        "The node is already split"
    }


    val queue = LinkedList<Node>().apply { add(this@Node) }

    while (queue.isNotEmpty()) {

        val next = queue.pop()

        next.leftSplit?.let { queue.push(it) }
        next.rightSplit?.let { queue.push(it) }

        println("Should go through all nodes")
    }

}*/
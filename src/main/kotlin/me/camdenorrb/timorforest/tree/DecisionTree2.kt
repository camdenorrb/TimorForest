package me.camdenorrb.timorforest.tree

import me.camdenorrb.timorforest.node.base.NodeBase

class DecisionTree2(val columnLabels: List<String>/*, val minLeafs: Int = 1, val maxNodes: Int = 100*/) {

    // TODO: Find what the root value should be
    //private val root = Node(columnLabels)

    // (Type --> Values)
    private val trainedData = mutableMapOf<String, MutableList<DoubleArray>>()

    var isBuilt = false
        private set

    /*
    var sampleRate = 1.0
        set(value) {

            check(value in 0..1) {
                "Sample rate needs to be between 0 and 1"
            }

            field = value
        }

    // The weights for all the columnLabels
    var weights = List(columnLabels.size) { 1.0 }
        set(value) {

            check(value.size == columnLabels.size) {
                "The amount of weights should equal the amount of [columnLabels]"
            }

            field = value
        }*/


    init {
        /*check(maxNodes >= 2) {
            "A tree needs at least 2 nodes"
        }
        check(minLeafs >= 1) {
            "A tree needs at least 1 leaf"
        }*/
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

        //if (isBuilt) return

        trainedData.map { it.value. }

        // Don't check if it's built here
        // Use isTrained instead
        //isBuilt = true
    }


    private fun bestSplitFor(inputs: List<DoubleArray>): Pair<Double, Question?> {

        var bestGain = 0.0

        var bestQuestion: Question? = null

        val uncertainty = Impurity.GINI(inputs)


        inputs.forEachIndexed { column, values ->

            values.forEach { value ->

                val question = Question(column, value)

                val (trueRows, falseRows) = values.toSet().partition { column >= it }

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


        return bestGain to bestQuestion
    }

    /**
     * Use [partition] to call this
     */
    private fun infoGain(trueRows: List<Double>, falseRows: List<Double>, uncertainty: Double): Double {
        val score = trueRows.size / (trueRows.size + falseRows.size)
        return uncertainty - score * Impurity.GINI(listOf(trueRows.toDoubleArray())) - (1 - score) * Impurity.GINI(listOf(falseRows.toDoubleArray()))
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

    /**
     * Gets the unique values for a column
     *
     * @param rows
     * @param column
     * @return
     */
    private fun uniqueValues(rows: List<DoubleArray>, column: Int): Set<Double> {
        return rows.map { it[column] }.toSet()
    }

    /**
     * TODO
     *
     * @param data
     * @return
     */
    private fun classCounts(data: Map<String, List<String>>): Map<String, Int> {
        return data.map { it.key to it.value.size }.toMap()
    }

    /**
     *
     *
     * @param rows
     * @param question
     * @return
     */
    private fun partition(rows: Map<String, List<Double>>, question: Question): Pair<List<List<Double>>, List<List<Double>>> {
        return rows.values.partition { question.match(it) }
    }



    /**
     * TODO
     *
     * @property column
     * @property value
     */
    data class Question(val column: Int, val value: Double) {

        fun match(example: List<Double>): Boolean {

            /*value.toDoubleOrNull()?.let {
                return example[column] >= value
            }*/

            return example[column] == value
        }

    }


    data class Leaf(override val value: DoubleArray) : NodeBase<DoubleArray> {

        override fun equals(other: Any?): Boolean {

            if (this === other) return true

            if (other !is Leaf || !value.contentEquals(other.value)) return false

            return true
        }

        override fun hashCode(): Int {
            return value.contentHashCode()
        }
    }


    data class Node(override val value: Question, val trueBranch: NodeBase<Any>, val falseBranch: NodeBase<Any>) : NodeBase<Question>


}

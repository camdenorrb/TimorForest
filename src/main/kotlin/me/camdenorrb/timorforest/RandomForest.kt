package me.camdenorrb.timorforest

import me.camdenorrb.timorforest.tree.DecisionTree
import kotlin.math.roundToInt

sealed class RandomForest(val numTrees: Int, val header: List<String>) {

    class Classifier(val sampleRatio: Double, numTrees: Int, header: List<String>) : RandomForest(numTrees, header) {

        private val trainingData = mutableListOf<List<Any>>()

        private var trees: List<DecisionTree> = emptyList()


        fun train(values: List<List<Any>>) {

            require(values.all { it.size == header.size }) {
                "Invalid values size, must equal header size. ${values.size} != ${header.size}"
            }

            trainingData.addAll(values)

            trees = buildTrees()
        }

        fun predict(values: List<Any>): MutableMap.MutableEntry<Any, Int> {

            val count = mutableMapOf<Any, Int>()

            trees.forEach { tree ->
                tree.predict(values).value.keys.forEach {
                    count[it] = count.getOrDefault(it, 0) + 1
                }
            }

            return count.entries.maxBy { it.value }!!
        }

        private fun buildTrees(): List<DecisionTree> {
            return (0 until numTrees).map {
                DecisionTree(header, trainingData.shuffled().take((trainingData.size * sampleRatio).roundToInt()))
            }
        }

    }

    class Regression(numTrees: Int, header: List<String>) : RandomForest(numTrees, header) {

    }

}
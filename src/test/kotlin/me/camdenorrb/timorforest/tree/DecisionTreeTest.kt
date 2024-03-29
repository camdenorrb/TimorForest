package me.camdenorrb.timorforest.tree

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class DecisionTreeTest {

    private lateinit var tree: DecisionTree


    @BeforeTest
    fun make() {

        val data = listOf(
            listOf("Black", 2, "Cat"),
            listOf("Brown", 2, "Duck"),
            listOf("White", 4, "Dog")
            // listOf("White", 5, "Cat")
        )

        tree = DecisionTree(listOf("Color", "Age", "Label"), data)

        println(tree)
    }

    @Test
    fun test() {
        println(tree.predict(listOf("White", 4)))
    }

    @AfterTest
    fun clean() {
    }

}

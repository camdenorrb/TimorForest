package me.camdenorrb.timorforest.tree

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class DecisionTreeTest {

    val tree = DecisionTree()


    @BeforeTest
    fun make() {

        val data = listOf<List<Comparable<*>>>(
            listOf("Black", 2, "Cat"),
            listOf("Brown", 3, "Duck"),
            listOf("White", 4, "Dog")
        )

        tree.train(data)
    }

    @Test
    fun test() {
        println(tree.predict(listOf("Brown", 2)))
    }

    @AfterTest
    fun clean() {
    }

}

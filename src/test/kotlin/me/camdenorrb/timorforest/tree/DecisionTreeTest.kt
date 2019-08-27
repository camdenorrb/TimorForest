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
           // listOf("White", 5, "Cat")

        )

        tree.train(data)

        println(tree)
    }

    @Test
    fun test() {
        println(tree.predict(listOf("White", 5)))
    }

    @AfterTest
    fun clean() {
    }

}

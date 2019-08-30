package me.camdenorrb.timorforest

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RandomForestTest {

    private val forest = RandomForest.Classifier(0.5, 1000, listOf("Color", "Age", "Label"))


    @BeforeTest
    fun make() {

        val data = listOf(
            listOf("Black", 2, "Cat"),
            listOf("Black", 2, "Cat"),
            listOf("Brown", 2, "Duck"),
            listOf("Brown", 2, "Duck"),
            listOf("White", 4, "Dog"),
            listOf("White", 4, "Dog")
            // listOf("White", 5, "Cat")
        )

        forest.train(data)
    }

    @Test
    fun test() {
        println(forest.predict(listOf("White", 4)))
    }

    @AfterTest
    fun clean() {
    }

}

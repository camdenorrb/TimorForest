package me.camdenorrb.timorforest

sealed class RandomForest(val numTrees: Int) {

    class Classifier(numTrees: Int) : RandomForest(numTrees) {

    }

    class Regression(numTrees: Int) : RandomForest(numTrees) {

    }

}
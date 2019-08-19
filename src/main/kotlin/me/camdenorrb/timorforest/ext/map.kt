package me.camdenorrb.timorforest.ext


inline fun <K, V> Map<K, V>.partition(block: Map.Entry<K, V>.() -> Boolean): Pair<Map<K, V>, Map<K, V>> {

    val map1 = mutableMapOf<K, V>()
    val map2 = mutableMapOf<K, V>()

    this.forEach {
        if (block(it)) map1[it.key] = it.value
        else map2[it.key] = it.value
    }

    return map1 to map2
}
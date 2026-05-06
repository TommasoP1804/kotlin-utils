import dev.tommasop1804.kutils.*

/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

fun main() {
    val x: String? = "nuixer"
    val y = x.ifNull { true }
    println(y)
}
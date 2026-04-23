import dev.tommasop1804.kutils.classes.coding.*
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toJson

/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

fun main() {
    val json = Json("""
        {
          "name": "Tommaso",
          "surname": "Pastorelli",
          "ages": [18, 20, 22],
          "object": {
            "temp": true
          }
        }
    """.trimIndent())
    println(json["object.temp"]?.toJson())
}
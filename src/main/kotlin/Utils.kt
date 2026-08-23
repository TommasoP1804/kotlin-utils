/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("UtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused")

import dev.tommasop1804.kutils.annotations.*

@PublishedApi
internal class Break(val result: Any? = null) : Throwable()
@PublishedApi
internal class Continue : Throwable()
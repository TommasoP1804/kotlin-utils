/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("UtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused")

import dev.tommasop1804.kutils.annotations.*

@Deprecated("Use breakLoop() instead", ReplaceWith("breakLoop()", "dev.tommasop1804.kutils.breakLoop"))
class Break(val result: Any? = null) : Throwable()
@Deprecated("Use continueLoop() instead", ReplaceWith("continueLoop()", "dev.tommasop1804.kutils.breakLoop"))
class Continue : Throwable()
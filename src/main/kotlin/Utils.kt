@file:JvmName("UtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused")

import dev.tommasop1804.kutils.annotations.Since

internal class Break(val result: Any? = null) : Throwable()
internal class Continue : Throwable()
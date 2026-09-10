/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:Suppress("DSL_MARKER_APPLIED_TO_WRONG_TARGET", "unused")

package dev.tommasop1804.kutils.dsl.sql

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.constants.*
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table

@PublishedApi
internal fun Column<*>.toQualifiedColumnName(alias: String?): String =
    "${alias?.let { "$it." } ?: String.EMPTY}${name}"

@SqlDslMarker
fun SqlBuilder.select(vararg columns: Pair<String?, Column<*>>, distinct: Boolean = false) =
    select(
        *columns.map { [alias, prop] -> prop.toQualifiedColumnName(alias) }.toTypedArray(),
        distinct = distinct
    )
@SqlDslMarker
fun SqlBuilder.select(vararg columns: Column<*>, distinct: Boolean = false) =
    select(
        *columns.map { it.name }.toTypedArray(),
        distinct = distinct
    )
@SqlDslMarker
fun SqlBuilder.select(tableAlias: String?, vararg columns: Column<*>, distinct: Boolean = false) {
    val alias = tableAlias ?: columns.firstOrNull()?.table?.tableName
    select(*columns.map { it.toQualifiedColumnName(alias) }.toTypedArray(), distinct = distinct)
}

@SqlDslMarker
fun SqlBuilder.from(table: Table, alias: String? = null) {
    from("${table.tableName}${if (alias != null) " $alias" else String.EMPTY}")
}
@SqlDslMarker
fun SqlBuilder.from(vararg tables: Pair<Table, String>) {
    from(*tables.map { "${it.first.tableName} ${it.second}" }.toTypedArray())
}

@SqlDslMarker
fun SqlBuilder.join(table: Table, alias: String? = null, type: JoinType, on: String? = null) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), type, on)
}
@SqlDslMarker
fun JoinScope.inner(table: Table, alias: String? = null, on: String) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.Inner, on)
}
@SqlDslMarker
fun JoinScope.left(table: Table, alias: String? = null, on: String) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.LeftOuter, on)
}
@SqlDslMarker
fun JoinScope.right(table: Table, alias: String? = null, on: String) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.RightOuter, on)
}
@SqlDslMarker
fun JoinScope.full(table: Table, alias: String? = null, on: String) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.FullOuter, on)
}
@SqlDslMarker
fun JoinScope.cross(table: Table, alias: String? = null) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.Cross)
}
@SqlDslMarker
fun JoinScope.natural(table: Table, alias: String? = null) {
    join(table.tableName + (alias?.let { " $it" } ?: String.EMPTY), JoinType.Natural)
}

@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.eq(value: Any) =
    "$name = $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.eq(value: Any) =
    "$name = $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.eq(value: Any) =
    "$name = $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.eq(value: Any) =
    "$this = $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.eq(value: Any) =
    "$this = $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.eq(value: Any) =
    "$this = $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.eq(property: Column<*>) =
    "$name = ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.eq(property: Column<*>) =
    "$name = ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.eq(property: Column<*>) =
    "$name = ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.eq(property: Column<*>) =
    "$this = ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.eq(property: Column<*>) =
    "$this = ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.eq(property: Column<*>) =
    "$this = ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.eq(property: Pair<String, Column<*>>) =
    "$name = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.eq(property: Pair<String, Column<*>>) =
    "$name = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.eq(property: Pair<String, Column<*>>) =
    "$name = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.eq(property: Pair<String, Column<*>>) =
    "$this = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.eq(property: Pair<String, Column<*>>) =
    "$this = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.eq(property: Pair<String, Column<*>>) =
    "$this = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.eq(value: Any) =
    "$first.${second.name} = $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.eq(value: Any) =
    "$first.${second.name} = $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.eq(value: Any) =
    "$first.${second.name} = $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.eq(property: Column<*>) =
    "$first.${second.name} = ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.eq(property: Column<*>) =
    "$first.${second.name} = ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.eq(property: Column<*>) =
    "$first.${second.name} = ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.eq(property: Pair<String, Column<*>>) =
    "$first.${second.name} = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.eq(property: Pair<String, Column<*>>) =
    "$first.${second.name} = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.eq(property: Pair<String, Column<*>>) =
    "$first.${second.name} = ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.neq(value: Any) =
    "$name != $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.neq(value: Any) =
    "$name != $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.neq(value: Any) =
    "$name != $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.neq(value: Any) =
    "$this != $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.neq(value: Any) =
    "$this != $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.neq(value: Any) =
    "$this != $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.neq(property: Column<*>) =
    "$name != ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.neq(property: Column<*>) =
    "$name != ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.neq(property: Column<*>) =
    "$name != ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.neq(property: Column<*>) =
    "$this != ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.neq(property: Column<*>) =
    "$this != ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.neq(property: Column<*>) =
    "$this != ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.neq(property: Pair<String, Column<*>>) =
    "$name != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.neq(property: Pair<String, Column<*>>) =
    "$name != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.neq(property: Pair<String, Column<*>>) =
    "$name != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.neq(property: Pair<String, Column<*>>) =
    "$this != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.neq(property: Pair<String, Column<*>>) =
    "$this != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.neq(property: Pair<String, Column<*>>) =
    "$this != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.neq(value: Any) =
    "$first.${second.name} != $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.neq(value: Any) =
    "$first.${second.name} != $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.neq(value: Any) =
    "$first.${second.name} != $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.neq(property: Column<*>) =
    "$first.${second.name} != ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.neq(property: Column<*>) =
    "$first.${second.name} != ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.neq(property: Column<*>) =
    "$first.${second.name} != ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.neq(property: Pair<String, Column<*>>) =
    "$first.${second.name} != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.neq(property: Pair<String, Column<*>>) =
    "$first.${second.name} != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.neq(property: Pair<String, Column<*>>) =
    "$first.${second.name} != ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gt(value: Any) =
    "$name > $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gt(value: Any) =
    "$name > $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gt(value: Any) =
    "$name > $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gt(value: Any) =
    "$this > $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gt(value: Any) =
    "$this > $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gt(value: Any) =
    "$this > $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gt(property: Column<*>) =
    "$name > ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gt(property: Column<*>) =
    "$name > ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gt(property: Column<*>) =
    "$name > ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gt(property: Column<*>) =
    "$this > ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gt(property: Column<*>) =
    "$this > ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gt(property: Column<*>) =
    "$this > ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gt(property: Pair<String, Column<*>>) =
    "$name > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gt(property: Pair<String, Column<*>>) =
    "$name > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gt(property: Pair<String, Column<*>>) =
    "$name > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gt(property: Pair<String, Column<*>>) =
    "$this > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gt(property: Pair<String, Column<*>>) =
    "$this > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gt(property: Pair<String, Column<*>>) =
    "$this > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gt(value: Any) =
    "$first.${second.name} > $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gt(value: Any) =
    "$first.${second.name} > $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gt(value: Any) =
    "$first.${second.name} > $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gt(property: Column<*>) =
    "$first.${second.name} > ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gt(property: Column<*>) =
    "$first.${second.name} > ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gt(property: Column<*>) =
    "$first.${second.name} > ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gt(property: Pair<String, Column<*>>) =
    "$first.${second.name} > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gt(property: Pair<String, Column<*>>) =
    "$first.${second.name} > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gt(property: Pair<String, Column<*>>) =
    "$first.${second.name} > ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lt(value: Any) =
    "$name < $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lt(value: Any) =
    "$name < $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lt(value: Any) =
    "$name < $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lt(value: Any) =
    "$this < $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lt(value: Any) =
    "$this < $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lt(value: Any) =
    "$this < $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lt(property: Column<*>) =
    "$name < ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lt(property: Column<*>) =
    "$name < ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lt(property: Column<*>) =
    "$name < ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lt(property: Column<*>) =
    "$this < ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lt(property: Column<*>) =
    "$this < ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lt(property: Column<*>) =
    "$this < ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lt(property: Pair<String, Column<*>>) =
    "$name < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lt(property: Pair<String, Column<*>>) =
    "$name < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lt(property: Pair<String, Column<*>>) =
    "$name < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lt(property: Pair<String, Column<*>>) =
    "$this < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lt(property: Pair<String, Column<*>>) =
    "$this < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lt(property: Pair<String, Column<*>>) =
    "$this < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lt(value: Any) =
    "$first.${second.name} < $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lt(value: Any) =
    "$first.${second.name} < $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lt(value: Any) =
    "$first.${second.name} < $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lt(property: Column<*>) =
    "$first.${second.name} < ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lt(property: Column<*>) =
    "$first.${second.name} < ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lt(property: Column<*>) =
    "$first.${second.name} < ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lt(property: Pair<String, Column<*>>) =
    "$first.${second.name} < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lt(property: Pair<String, Column<*>>) =
    "$first.${second.name} < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lt(property: Pair<String, Column<*>>) =
    "$first.${second.name} < ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gte(value: Any) =
    "$name >= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gte(value: Any) =
    "$name >= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gte(value: Any) =
    "$name >= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gte(value: Any) =
    "$this >= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gte(value: Any) =
    "$this >= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gte(value: Any) =
    "$this >= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gte(property: Column<*>) =
    "$name >= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gte(property: Column<*>) =
    "$name >= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gte(property: Column<*>) =
    "$name >= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gte(property: Column<*>) =
    "$this >= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gte(property: Column<*>) =
    "$this >= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gte(property: Column<*>) =
    "$this >= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.gte(property: Pair<String, Column<*>>) =
    "$name >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.gte(property: Pair<String, Column<*>>) =
    "$name >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.gte(property: Pair<String, Column<*>>) =
    "$name >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.gte(property: Pair<String, Column<*>>) =
    "$this >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.gte(property: Pair<String, Column<*>>) =
    "$this >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.gte(property: Pair<String, Column<*>>) =
    "$this >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gte(value: Any) =
    "$first.${second.name} >= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gte(value: Any) =
    "$first.${second.name} >= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gte(value: Any) =
    "$first.${second.name} >= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gte(property: Column<*>) =
    "$first.${second.name} >= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gte(property: Column<*>) =
    "$first.${second.name} >= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gte(property: Column<*>) =
    "$first.${second.name} >= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.gte(property: Pair<String, Column<*>>) =
    "$first.${second.name} >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.gte(property: Pair<String, Column<*>>) =
    "$first.${second.name} >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.gte(property: Pair<String, Column<*>>) =
    "$first.${second.name} >= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lte(value: Any) =
    "$name <= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lte(value: Any) =
    "$name <= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lte(value: Any) =
    "$name <= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lte(value: Any) =
    "$this <= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lte(value: Any) =
    "$this <= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lte(value: Any) =
    "$this <= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lte(property: Column<*>) =
    "$name <= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lte(property: Column<*>) =
    "$name <= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lte(property: Column<*>) =
    "$name <= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lte(property: Column<*>) =
    "$this <= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lte(property: Column<*>) =
    "$this <= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lte(property: Column<*>) =
    "$this <= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.lte(property: Pair<String, Column<*>>) =
    "$name <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.lte(property: Pair<String, Column<*>>) =
    "$name <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.lte(property: Pair<String, Column<*>>) =
    "$name <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.lte(property: Pair<String, Column<*>>) =
    "$this <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun String.lte(property: Pair<String, Column<*>>) =
    "$this <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun String.lte(property: Pair<String, Column<*>>) =
    "$this <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lte(value: Any) =
    "$first.${second.name} <= $value"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lte(value: Any) =
    "$first.${second.name} <= $value"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lte(value: Any) =
    "$first.${second.name} <= $value"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lte(property: Column<*>) =
    "$first.${second.name} <= ${property.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lte(property: Column<*>) =
    "$first.${second.name} <= ${property.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lte(property: Column<*>) =
    "$first.${second.name} <= ${property.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.lte(property: Pair<String, Column<*>>) =
    "$first.${second.name} <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.lte(property: Pair<String, Column<*>>) =
    "$first.${second.name} <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.lte(property: Pair<String, Column<*>>) =
    "$first.${second.name} <= ${property.first}.${property.second.name}"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.like(value: Any) =
    "$name LIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.like(value: Any) =
    "$name LIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.like(value: Any) =
    "$name LIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.like(value: Any) =
    "$this LIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.like(value: Any) =
    "$this LIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.like(value: Any) =
    "$this LIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.like(property: Column<*>) =
    "$name LIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.like(property: Column<*>) =
    "$name LIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.like(property: Column<*>) =
    "$name LIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.like(property: Column<*>) =
    "$this LIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.like(property: Column<*>) =
    "$this LIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.like(property: Column<*>) =
    "$this LIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.like(property: Pair<String, Column<*>>) =
    "$name LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.like(property: Pair<String, Column<*>>) =
    "$name LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.like(property: Pair<String, Column<*>>) =
    "$name LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.like(property: Pair<String, Column<*>>) =
    "$this LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.like(property: Pair<String, Column<*>>) =
    "$this LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.like(property: Pair<String, Column<*>>) =
    "$this LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.like(value: Any) =
    "$first.${second.name} LIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.like(value: Any) =
    "$first.${second.name} LIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.like(value: Any) =
    "$first.${second.name} LIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.like(property: Column<*>) =
    "$first.${second.name} LIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.like(property: Column<*>) =
    "$first.${second.name} LIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.like(property: Column<*>) =
    "$first.${second.name} LIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.like(property: Pair<String, Column<*>>) =
    "$first.${second.name} LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.like(property: Pair<String, Column<*>>) =
    "$first.${second.name} LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.like(property: Pair<String, Column<*>>) =
    "$first.${second.name} LIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.ilike(value: Any) =
    "$name ILIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.ilike(value: Any) =
    "$name ILIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.ilike(value: Any) =
    "$name ILIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.ilike(value: Any) =
    "$this ILIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.ilike(value: Any) =
    "$this ILIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.ilike(value: Any) =
    "$this ILIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.ilike(property: Column<*>) =
    "$name ILIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.ilike(property: Column<*>) =
    "$name ILIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.ilike(property: Column<*>) =
    "$name ILIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.ilike(property: Column<*>) =
    "$this ILIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.ilike(property: Column<*>) =
    "$this ILIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.ilike(property: Column<*>) =
    "$this ILIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Column<*>.ilike(property: Pair<String, Column<*>>) =
    "$name ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Column<*>.ilike(property: Pair<String, Column<*>>) =
    "$name ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Column<*>.ilike(property: Pair<String, Column<*>>) =
    "$name ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun String.ilike(property: Pair<String, Column<*>>) =
    "$this ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun String.ilike(property: Pair<String, Column<*>>) =
    "$this ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun String.ilike(property: Pair<String, Column<*>>) =
    "$this ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.ilike(value: Any) =
    "$first.${second.name} ILIKE '$value'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.ilike(value: Any) =
    "$first.${second.name} ILIKE '$value'"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.ilike(value: Any) =
    "$first.${second.name} ILIKE '$value'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.ilike(property: Column<*>) =
    "$first.${second.name} ILIKE '${property.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.ilike(property: Column<*>) =
    "$first.${second.name} ILIKE '${property.name}'"
@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.ilike(property: Column<*>) =
    "$first.${second.name} ILIKE '${property.name}'"
@SqlDslMarker
context(_: SqlBuilder)
infix fun Pair<String, Column<*>>.ilike(property: Pair<String, Column<*>>) =
    "$first.${second.name} ILIKE '${property.first}.${property.second.name}'"
@SqlDslMarker
context(_: WhereScope)
infix fun Pair<String, Column<*>>.ilike(property: Pair<String, Column<*>>) =
    "$first.${second.name} ILIKE '${property.first}.${property.second.name}'"

@SqlDslMarker
context(_: JoinScope)
infix fun Pair<String, Column<*>>.ilike(property: Pair<String, Column<*>>) =
    "$first.${second.name} ILIKE '${property.first}.${property.second.name}'"

@SqlDslMarker
fun WhereScope.isNullValue(prop: Column<*>) = isNullValue(prop.name)
@SqlDslMarker
fun WhereScope.isNotNullValue(prop: Column<*>) = isNotNullValue(prop.name)

@SqlDslMarker
fun SqlBuilder.groupBy(vararg columns: Pair<String?, Column<*>>) =
    groupBy(*columns.map { [alias, prop] -> prop.toQualifiedColumnName(alias) }.toTypedArray())
@SqlDslMarker
fun SqlBuilder.groupBy(vararg columns: Column<*>) =
    groupBy(*columns.map { it.name }.toTypedArray())
@SqlDslMarker
fun SqlBuilder.groupBy(tableAlias: String?, vararg columns: Column<*>) {
    val alias = tableAlias ?: columns.firstOrNull()?.table?.tableName
    groupBy(*columns.map { it.toQualifiedColumnName(alias) }.toTypedArray())
}

@SqlDslMarker
fun SqlBuilder.insertInto(table: Table) {
    insertInto(table.tableName)
}
@SqlDslMarker
fun SqlBuilder.columns(vararg columns: Column<*>) =
    columns(*columns.map { it.name }.toTypedArray())

@SqlDslMarker
fun SqlBuilder.update(table: Table) {
    update(table.tableName)
}

@SqlDslMarker
fun SqlBuilder.set(vararg expressions: Pair<Column<*>, String>) =
    set(*expressions.map { [prop, `value`] -> "${prop.name} = $value" }.toTypedArray())

@SqlDslMarker
fun SqlBuilder.deleteFrom(table: Table) {
    deleteFrom(table.tableName)
}

@SqlDslMarker
fun SqlBuilder.orderBy(vararg columns: Triple<String?, Column<*>, SortDirection>) =
    orderBy(*columns.map { [alias, prop, dir] -> prop.toQualifiedColumnName(alias) to dir }.toTypedArray())
@SqlDslMarker
fun SqlBuilder.orderBy(vararg columns: Pair<Column<*>, SortDirection>) =
    orderBy(*columns.map { [prop, dir] -> prop.name to dir }.toTypedArray())
@SqlDslMarker
fun SqlBuilder.orderBy(vararg columns: Column<*>, direction: SortDirection = SortDirection.Ascending) =
    orderBy(*columns.map { it.name to direction }.toTypedArray())
@SqlDslMarker
fun SqlBuilder.orderBy(tableAlias: String?, vararg columns: Column<*>, direction: SortDirection = SortDirection.Ascending) {
    val alias = tableAlias ?: columns.firstOrNull()?.table?.tableName
    orderBy(*columns.map { it.toQualifiedColumnName(alias) to direction }.toTypedArray())
}
@SqlDslMarker
fun OrderByScope.column(prop: Column<*>, direction: SortDirection = SortDirection.Ascending) =
    column(prop.name, direction)
@SqlDslMarker
fun OrderByScope.column(alias: String?, prop: Column<*>, direction: SortDirection = SortDirection.Ascending) =
    column(prop.toQualifiedColumnName(alias), direction)

@SqlDslMarker
fun OrderByScope.asc(prop: Column<*>) = asc(prop.name)

@SqlDslMarker
fun OrderByScope.desc(prop: Column<*>) = desc(prop.name)

@SqlDslMarker
fun OrderByScope.nullsFirst(prop: Column<*>, direction: SortDirection = SortDirection.Ascending) =
    nullsFirst(prop.name, direction)

@SqlDslMarker
fun OrderByScope.nullsLast(prop: Column<*>, direction: SortDirection = SortDirection.Ascending) =
    nullsLast(prop.name, direction)

@SqlDslMarker
fun SqlBuilder.truncate(table: Table, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
    truncate(table.tableName, ifExists, dropType)
}

@SqlDslMarker
fun SqlBuilder.createTable(table: Table, body: String) {
    createTable(table.tableName, body)
}

@SqlDslMarker
fun SqlBuilder.alterTable(table: Table, ifExists: Boolean = false, alteration: String) {
    alterTable(table.tableName, ifExists, alteration)
}

@SqlDslMarker
fun SqlBuilder.dropTable(table: Table, ifExists: Boolean = false, dropType: DropType = DropType.Restrict) {
    dropTable(table.tableName, ifExists, dropType)
}

@SqlDslMarker
fun SqlBuilder.createIndex(indexName: String, table: Table, columns: String, unique: Boolean = false) {
    createIndex(indexName, table.tableName, columns, unique)
}

@SqlDslMarker
fun SqlBuilder.showTable(table: Table) {
    showTable(table.tableName)
}

@SqlDslMarker
fun SqlBuilder.showColumnsFromTable(table: Table) {
    showColumnsFromTable(table)
}

@SqlDslMarker
fun SqlBuilder.showIndexFromTable(table: Table) {
    showIndexFromTable(table)
}

@SqlDslMarker
fun TriggerScope.onTable(table: Table) {
    onTable(table.tableName)
}
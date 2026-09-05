/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ExposedUuidExtensionsKt")
@file:Since("5.3.0")
@file:Suppress("unused")

package dev.tommasop1804.kutils.classes.identifiers

import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.annotations.*
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.UUIDColumnType
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity

/**
 * A type alias for UUIDColumnType.
 * This allows the UUIDColumnType to be referred to with a shorter and more convenient name, UuidColumnType.
 * @since 5.3.0
 */
typealias UuidColumnType = UUIDColumnType
/**
 * A type alias for the UUIDEntity class.
 * This provides a shorter or alternative name to reference UUIDEntity in the codebase.
 * @since 5.3.0
 */
typealias UuidEntity = UUIDEntity

/**
 * Represents a specialized table for managing entities identified by UUIDs in a relational database.
 *
 * This class extends the `IdTable` class and provides a structure for tables
 * that use `UUID`-based primary key columns. The UUIDs are generated based on the specified
 * `UuidVersion`. It includes features to define the primary key column and set a
 * default client-side UUID generation strategy.
 *
 * @constructor Initializes a `UuidTable` with a specified table name, ID column name, and UUID version.
 * @param name The name of the table. Defaults to an empty string.
 * @param columnName The name of the ID column in the table. Defaults to "id".
 * @param version The version of the UUID to be generated for this table. Determines the methodology used
 *                for generating the UUID.
 * @since 5.3.0
 */
open class UuidTable(name: String = String.EMPTY, columnName: String = "id", version: dev.tommasop1804.kutils.classes.identifiers.UuidVersion) : IdTable<Uuid>(name) {
    final override val id: Column<EntityID<Uuid>> = javaUUID(columnName).clientDefault { Uuid(version) }.entityId()
    final override val primaryKey = PrimaryKey(id)
}
@file:Suppress("java_integer_as_kotlin_int")

package dev.tommasop1804.kutils.classes.constants

import dev.tommasop1804.kutils.classes.coding.JSON
import dev.tommasop1804.kutils.classes.coding.Language
import dev.tommasop1804.kutils.classes.coding.YAML
import dev.tommasop1804.kutils.classes.collections.Table
import dev.tommasop1804.kutils.classes.colors.Color
import dev.tommasop1804.kutils.classes.geography.BoundingBox
import dev.tommasop1804.kutils.classes.geography.Country
import dev.tommasop1804.kutils.classes.geography.GeoCoordinate
import dev.tommasop1804.kutils.classes.geometry.Line
import dev.tommasop1804.kutils.classes.geometry.Point
import dev.tommasop1804.kutils.classes.geometry.Shape2D
import dev.tommasop1804.kutils.classes.geometry.Shape3D
import dev.tommasop1804.kutils.classes.measure.Measure
import dev.tommasop1804.kutils.classes.measure.Measurement
import dev.tommasop1804.kutils.classes.measure.ScalarUnit
import dev.tommasop1804.kutils.classes.money.Currency
import dev.tommasop1804.kutils.classes.money.Money
import dev.tommasop1804.kutils.classes.numbers.Hex
import dev.tommasop1804.kutils.classes.numbers.Hex.Companion.toHex
import dev.tommasop1804.kutils.classes.numbers.RomanNumber
import dev.tommasop1804.kutils.classes.registry.Contact
import dev.tommasop1804.kutils.classes.registry.Contact.Email.Companion.isValidEmail
import dev.tommasop1804.kutils.classes.time.*
import dev.tommasop1804.kutils.classes.time.TimeZone
import dev.tommasop1804.kutils.classes.tree.Tree
import dev.tommasop1804.kutils.classes.tree.TreeNode
import dev.tommasop1804.kutils.classes.tuple.Quadruple
import dev.tommasop1804.kutils.classes.tuple.Quintuple
import dev.tommasop1804.kutils.expect
import dev.tommasop1804.kutils.isNotNull
import dev.tommasop1804.kutils.unaryPlus
import java.time.ZoneId
import java.time.chrono.ChronoPeriod
import java.time.temporal.Temporal
import java.time.temporal.TemporalField
import java.time.temporal.TemporalUnit
import java.util.*
import kotlin.reflect.KClass

/**
 * Represents common Kotlin types with their corresponding KClass references.
 * This enum can be used in maps or collections where field names are associated with their types.
 *
 * @property kClass The KClass reference for the type.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused", "OPT_IN_USAGE")
enum class ObjType(
    val kClass: KClass<*>,
    val macroType: MacroType,
    val isCompiledAsPrimitive: Boolean = false,
    val isJavaType: Boolean = false,
    val isKotlinBaseType: Boolean = false,
    val isInterface: Boolean = false,
) {
    /**
     * Represents the String type in Kotlin.
     * Used for text and character sequences.
     * @since 1.0.0
     */
    STRING(String::class, isKotlinBaseType = true, macroType = MacroType.STRING),

    /**
     * Represents a Kotlin type with a hexadecimal classification.
     *
     * This enumeration entry identifies a type as related to or represented by a hexadecimal structure.
     *
     * @since 1.0.0
     */
    HEX(Hex::class, macroType = MacroType.OBJECT),

    /**
     * Represents the COLOR type in the context of Kotlin type processing.
     * It is associated with the Color class and may be used to identify
     * or handle certain characteristics or functionalities involving color
     * representations within the Kotlin type system.
     *
     * @since 1.0.0
     */
    COLOR(Color::class, macroType = MacroType.OBJECT),

    /**
     * Represents the type for handling phone number-related functionality.
     * Encapsulates logic and structure specific to phone number representations
     * within the associated context or system.
     *
     * @since 1.0.0
     */
    PHONE_NUMBER(Contact.PhoneNumber::class, macroType = MacroType.STRING),

    /**
     * Represents the `Email` type in the `KotlinType` enum.
     *
     * This type specifically corresponds to the `Email` value class, which is designed
     * to encapsulate and validate email addresses. The value class ensures the email
     * adheres to a specified format and facilitates operations like serialization,
     * conversion, and character sequence manipulations.
     *
     * @since 1.0.0
     */
    EMAIL(Contact.Email::class, macroType = MacroType.STRING),
    
    /**
     * Represents the Int type in Kotlin.
     * Used for 32-bit integer values.
     * @since 1.0.0
     */
    INT(Int::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the Long type in Kotlin.
     * Used for 64-bit integer values.
     * @since 1.0.0
     */
    LONG(Long::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the Double type in Kotlin.
     * Used for 64-bit floating point numbers.
     * @since 1.0.0
     */
    DOUBLE(Double::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the Float type in Kotlin.
     * Used for 32-bit floating point numbers.
     * @since 1.0.0
     */
    FLOAT(Float::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the Boolean type in Kotlin.
     * Used for true/false values.
     * @since 1.0.0
     */
    BOOLEAN(Boolean::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.BOOLEAN),
    
    /**
     * Represents the Char type in Kotlin.
     * Used for single character values.
     * @since 1.0.0
     */
    CHAR(Char::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.CHARACTER),
    
    /**
     * Represents the Byte type in Kotlin.
     * Used for 8-bit integer values.
     * @since 1.0.0
     */
    BYTE(Byte::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the Short type in Kotlin.
     * Used for 16-bit integer values.
     * @since 1.0.0
     */
    SHORT(Short::class, isCompiledAsPrimitive = true, isKotlinBaseType = true, macroType = MacroType.NUMBER),

    /**
     * Represents the number type within the `KotlinType` system.
     *
     * This enumeration is used to signify classes or data types that handle numerical values.
     * Denoting its association with the `NUMBER` constant in the corresponding `MacroType`,
     * this type enables categorization and processing of numeric data in a structured fashion.
     *
     * The `NUMBER` type supports identification of Java number types,
     * as indicated by the `isKotlinBaseType` parameter set to true.
     *
     * @since 1.0.0
     */
    NUMBER(Number::class, isKotlinBaseType = true, macroType = MacroType.NUMBER),

    /**
     * Represents a Roman numeral type within the application logic.
     *
     * The `ROMAN_NUMBER` object is part of the enumeration or structure defining
     * various types supported in the system. It processes and manages Roman numeral
     * representations. It is associated with the `RomanNumber` class, which likely
     * provides specific functionality, validation, or transformations for Roman numeral values.
     *
     * @since 1.0.0
     */
    ROMAN_NUMBER(RomanNumber::class, macroType = MacroType.NUMBER),
    
    /**
     * Represents the List type in Kotlin.
     * Used for ordered collections of elements.
     * @since 1.0.0
     */
    LIST(List::class, isKotlinBaseType = true, isInterface = true, macroType = MacroType.COLLECTION),
    
    /**
     * Represents the Map type in Kotlin.
     * Used for key-value pair collections.
     * @since 1.0.0
     */
    MAP(Map::class, isKotlinBaseType = true, isInterface = true, macroType = MacroType.MAP),
    
    /**
     * Represents the Set type in Kotlin.
     * Used for unique element collections.
     * @since 1.0.0
     */
    SET(Set::class, isKotlinBaseType = true, isInterface = true, macroType = MacroType.COLLECTION),
    
    /**
     * Represents the Array type in Kotlin.
     * Used for fixed-size collections of elements.
     * @since 1.0.0
     */
    ARRAY(Array::class, isKotlinBaseType = true, macroType = MacroType.ARRAY),
    
    /**
     * Represents the Pair type in Kotlin.
     * Used for storing two values together.
     * @since 1.0.0
     */
    PAIR(Pair::class, isKotlinBaseType = true, macroType = MacroType.TUPLE),
    
    /**
     * Represents the Triple type in Kotlin.
     * Used for storing three values together.
     * @since 1.0.0
     */
    TRIPLE(Triple::class, isKotlinBaseType = true, macroType = MacroType.TUPLE),

    /**
     * Represents the Quadruple type.
     * Used for storing four values together.
     * @since 1.0.0
     */
    QUADRUPLE(Quadruple::class, macroType = MacroType.TUPLE),

    /**
     * Represents the Quintuple type.
     * Used for storing five values together.
     * @since 1.0.0
     */
    QUINTUPLE(Quintuple::class, macroType = MacroType.TUPLE),
    
    /**
     * Represents the Any type in Kotlin.
     * The root of the Kotlin class hierarchy.
     * @since 1.0.0
     */
    ANY(Any::class, isKotlinBaseType = true, macroType = MacroType.OBJECT),
    
    /**
     * Represents the Unit type in Kotlin.
     * Used to indicate a function that doesn't return a value.
     * @since 1.0.0
     */
    UNIT(Unit::class, isKotlinBaseType = true, macroType = MacroType.KOTLIN_SPECIAL_TYPE),
    
    /**
     * Represents the Nothing type in Kotlin.
     * Used to indicate a function that never returns.
     * @since 1.0.0
     */
    NOTHING(Nothing::class, isKotlinBaseType = true, macroType = MacroType.KOTLIN_SPECIAL_TYPE),
    
    /**
     * Represents the java.time.LocalDate type.
     * Used for date values without time components.
     * @since 1.0.0
     */
    LOCAL_DATE(java.time.LocalDate::class, isJavaType = true, macroType = MacroType.TEMPORAL),
    
    /**
     * Represents the java.time.LocalTime type.
     * Used for time values without date components.
     * @since 1.0.0
     */
    LOCAL_TIME(java.time.LocalTime::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents the java.time.LocalDateTime type.
     * Used for date and time values without timezone information.
     * @since 1.0.0
     */
    LOCAL_DATE_TIME(java.time.LocalDateTime::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents the `OffsetDateTime` type from the Java Time API.
     *
     * This constant is used to symbolize the `java.time.OffsetDateTime` class in the context
     * of the containing class. The `OffsetDateTime` class is commonly used to work with date-time
     * values that include an offset from UTC/Greenwich.
     * @since 1.0.0
     */
    OFFSET_DATE_TIME(java.time.OffsetDateTime::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents the java.time.OffsetTime type.
     * Used for time values with timezone information.
     * @since 1.0.0
     */
    OFFSET_TIME(java.time.OffsetTime::class, isJavaType = true, macroType = MacroType.TEMPORAL),
    
    /**
     * Represents the java.time.ZonedDateTime type.
     * Used for date and time values with timezone information.
     * @since 1.0.0
     */
    ZONED_DATE_TIME(java.time.ZonedDateTime::class, isJavaType = true, macroType = MacroType.TEMPORAL),
    
    /**
     * Represents the java.time.Instant type.
     * Used for a point in time in the UTC timezone.
     * @since 1.0.0
     */
    INSTANT(java.time.Instant::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents a local month-day-time type used for handling date and time information without a specific time zone.
     * This class serves as an identifier or enum type referencing a specific time structure with month, day, and time information.
     *
     * @since 1.0.0
     */
    LOCAL_MONTH_DAY_TIME(LocalMonthDayTime::class, macroType = MacroType.TEMPORAL),

    /**
     * Represents a specific type, `OffsetMonthDayTime`, within the `KotlinType` class.
     * This type corresponds to a temporal format comprising a month, day, time,
     * and an offset from UTC or a time zone.
     *
     * @since 1.0.0
     */
    OFFSET_MONTH_DAY_TIME(OffsetMonthDayTime::class, macroType = MacroType.TEMPORAL),

    /**
     * Represents the `ZONED_MONTH_DAY_TIME` type mapping, associated with the `ZonedMonthDayTime` class.
     * This type is used to handle date and time information combined with a timezone, specifically
     * relating to a month and day of the year.
     *
     * @since 1.0.0
     */
    ZONED_MONTH_DAY_TIME(ZonedMonthDayTime::class, macroType = MacroType.TEMPORAL),

    /**
     * Represents the `MonthDay` type from the Java Time API.
     *
     * @since 1.0.0
     */
    MONTH_DAY(java.time.MonthDay::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents the `Year` type defined using the `java.time.Year` class.
     *
     * It is commonly used to manage and store information about years,
     * providing utilities for year-based date handling and operations.
     *
     * @since 1.0.0
     */
    YEAR(java.time.Year::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents a specific year and month in the ISO-8601 calendar system.
     *
     * This class is associated with the `YearMonth` type from the `java.time` package
     * and can be used in contexts where year and month values are required.
     * It is particularly useful for handling date-related computations
     * without requiring a specific day.
     *
     * @since 1.0.0
     */
    YEAR_MONTH(java.time.YearMonth::class, isJavaType = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents the Kotlin type for temporal values, typically used for date or time-related constructs.
     *
     * This type is based on the Java temporal interface (`java.time.temporal.Temporal`)
     * and is identified as a Java type and interface. It is categorized under the temporal macro type.
     *
     * @since 1.0.0
     */
    TEMPORAL(Temporal::class, isJavaType = true, isInterface = true, macroType = MacroType.TEMPORAL),

    /**
     * Represents a Kotlin type for `ChronoPeriod`, a temporal amount in Java's time API.
     *
     * `CHRONO_PERIOD` is intended to classify the `ChronoPeriod` interface from the Java standard library,
     * which models a date-based amount of time. It is categorized as a temporal amount type
     * and marked as a Java-based interface in the macro system.
     *
     * This enum constant is part of the `KotlinType` class and aids in identifying and processing
     * types related to date or time intervals in cross-language Kotlin and Java projects.
     *
     * @since 1.0.0
     */
    CHRONO_PERIOD(ChronoPeriod::class, isJavaType = true, isInterface = true, macroType = MacroType.TEMPORAL_AMOUNT),

    /**
     * Represents a temporal interval type for KotlinType.
     * This type is associated with the class specified by the `kClass` parameter,
     * which is `TemporalInterval::class` in this case.
     *
     * It is commonly used to handle temporal interval constructs in
     * specific use cases and contexts requiring time-based operations or intervals.
     *
     * @since 1.0.0
     */
    TEMPORAL_INTERVAL(TemporalInterval::class, isInterface = true, macroType = MacroType.TEMPORAL_AMOUNT),

    /**
     * Represents a repeated temporal interval type.
     * This class is used to define and handle temporal intervals
     * that occur repeatedly over time.
     *
     * @since 1.0.0
     */
    REPEATED_TEMPORAL_INTERVAL(RepeatedTemporalInterval::class, isInterface = true, macroType = MacroType.TEMPORAL_AMOUNT),

    /**
     * Represents the java.math.BigDecimal type.
     * Used for arbitrary-precision decimal numbers.
     * @since 1.0.0
     */
    BIG_DECIMAL(java.math.BigDecimal::class, isJavaType = true, macroType = MacroType.NUMBER),

    /**
     * Represents the java.math.BigInteger type.
     * Used for arbitrary-precision integer numbers.
     * @since 1.0.0
     */
    BIG_INTEGER(java.math.BigInteger::class, isJavaType = true, macroType = MacroType.NUMBER),
    
    /**
     * Represents the java.util.UUID type.
     * Used for universally unique identifiers.
     * @since 1.0.0
     */
    UUID(java.util.UUID::class, isJavaType = true, macroType = MacroType.OBJECT),

    /**
     * Represents the identification of a zone or a specific type, encapsulated with
     * contextual metadata. Utilized to associate a Kotlin type with its zone or
     * identification category within a certain application scope.
     *
     * @since 1.0.0
     */
    ZONE_IDENT(ZoneIdent::class, isInterface = true, macroType = MacroType.OBJECT),

    /**
     * Represents a Kotlin type reference for money-related processing.
     * This is a type-safe indicator that supports integration and operations
     * in contexts where monetary values need to be defined or manipulated.
     *
     * @since 1.0.0
     */
    MONEY(Money::class, macroType = MacroType.OBJECT),

    /**
     * Represents a mapping for a currency within the system.
     * This predefined type is associated with the `Currency` class,
     * which models currency-related functionalities.
     *
     * Designed to be used for systems that need to handle financial
     * or currency-based operations tied to the supported `Currency` class.
     *
     * @since 1.0.0
     */
    CURRENCY(Currency::class, macroType = MacroType.ENUM),

    /**
     * Represents the unit type for scalar values in the Kotlin type system.
     *
     * This is a specific type symbol used to indicate scalar units, encapsulated by the property
     * associated with a Kotlin type.
     *
     * @since 1.0.0
     */
    SCALAR_UNIT(ScalarUnit::class, isInterface = true, macroType = MacroType.OBJECT),

    /**
     * Represents a unit of measurement or quantification in the system.
     * This is utilized to handle and process measurement-related data efficiently.
     *
     * @since 1.0.0
     */
    MEASURE(Measure::class, macroType = MacroType.ENUM),

    /**
     * Represents a specific measurement type that corresponds to the provided
     * Kotlin class reference. This enum is used to define and categorize the type
     * of measurement in a structured and type-safe manner.
     *
     * @since 1.0.0
     */
    MEASUREMENT(Measurement::class, macroType = MacroType.OBJECT),

    /**
     * Defines an enumeration entry for representing a two-dimensional shape in the `KotlinType` class.
     *
     * This entry associates the `Shape2D` type, which provides methods to calculate geometric properties
     * of 2D shapes, such as area, perimeter, and point containment. The enumeration context can be used
     * for classification or decision-making processes involving 2D shapes.
     *
     * @since 1.0.0
     */
    SHAPE_2D(Shape2D::class, isInterface = true, macroType = MacroType.OBJECT),

    /**
     * Represents a symbol for the `Shape3D` interface within the `KotlinType` class.
     * This symbol serves as a reference to the `Shape3D` interface, which defines properties and operations
     * for three-dimensional geometric shapes. It is used in type systems or context where the `Shape3D` type
     * needs to be identified programmatically.
     *
     * @since 1.0.0
     */
    SHAPE_3D(Shape3D::class, isInterface = true, macroType = MacroType.OBJECT),

    /**
     * Represents the Kotlin type for a specific point in the system that is linked to a class representation.
     * This type indicates the association between the `Point` class and the defined logic within the system.
     *
     * @since 1.0.0
     */
    POINT(Point::class, macroType = MacroType.OBJECT),

    /**
     * Represents the LINE symbol which corresponds to the Kotlin class type for `Line`.
     *
     * This symbol is part of a type-safe representation system in KotlinType, used to map the
     * specific type `Line` within the application, ensuring clear modeling and type handling.
     *
     * @since 1.0.0
     */
    LINE(Line::class, macroType = MacroType.OBJECT),

    /**
     * Represents a type definition for a bounding box in the Kotlin runtime.
     * This class is used to identify and associate bounding boxes within a type system.
     *
     * @since 1.0.0
     */
    BOUNDING_BOX(BoundingBox::class, macroType = MacroType.OBJECT),

    /**
     * Represents the COUNTRY type in the specified `KotlinType` context.
     * This type is associated with the `Country` class and functions as a descriptor or identifier
     * for operations or behaviors related to the `Country` model.
     *
     * @since 1.0.0
     */
    COUNTRY(Country::class, macroType = MacroType.ENUM),

    /**
     * Represents a geographic coordinate type, typically used for denoting a location on Earth using latitude and longitude.
     *
     * This type is associated with `GeoCoordinate::class` and is intended to work with systems that require location-based information.
     *
     * @since 1.0.0
     */
    GEO_COORDINATE(GeoCoordinate::class, macroType = MacroType.OBJECT),

    /**
     * Represents the Kotlin type information of a class.
     *
     * This enumeration is used to define and handle characteristics of Kotlin types,
     * such as whether the type is primitive, a base Kotlin type, or to access
     * class-specific metadata like simple names or qualified names.
     *
     * Each instance of `KOTLIN_TYPE` correlates to a Kotlin type and can be used
     * in scenarios where type information is necessary to determine runtime behavior
     * or to handle type-based logic dynamically.
     *
     * @since 1.0.0
     */
    KOTLIN_TYPE(ObjType::class, macroType = MacroType.ENUM),

    /**
     * Represents the PRIORITY type within the `KotlinType` class.
     *
     * This type corresponds to the `Priority` enumeration, which defines various
     * priority levels, each associated with a specific default color to indicate urgency.
     *
     * Usage of this type allows for consistent handling of priority levels
     * within the broader context of the `KotlinType` class and its associated logic.
     *
     * @since 1.0.0
     */
    PRIORITY(Priority::class, macroType = MacroType.ENUM),

    /**
     * Represents a programming language used as a type classification within the KotlinType structure.
     *
     * The PROGRAMMING_LANGUAGE object refers to the concept of a programming language type,
     * which is wrapped by the `kClass` provided.
     *
     * @since 1.0.0
     */
    PROGRAMMING_LANGUAGE(Language::class, macroType = MacroType.ENUM),

    /**
     * Represents the quantity level within the `KotlinType` class.
     *
     * This identifier is used to reference the `QuantityLevel` enumeration, which
     * provides specific quantity constraints or expectations such as minimum, maximum,
     * or exact levels. It is a key part in contexts where quantitative rules or
     * parameters play a crucial role.
     *
     * @since 1.0.0
     */
    QUANTITY_LEVEL(QuantityLevel::class, macroType = MacroType.ENUM),

    /**
     * Represents the SEX constant, which is tied to the Sex class.
     *
     * This constant is used to reference the `Sex` enumeration, which provides
     * a symbolic representation for biological sex classification (e.g., male and female).
     *
     * @since 1.0.0
     */
    SEX(Sex::class, macroType = MacroType.ENUM),

    /**
     * Enum class representing the type of sorting order.
     *
     * @since 1.0.0
     */
    SORTING_TYPE(SortDirection::class, macroType = MacroType.ENUM),

    /**
     * Enum class representing different text transformation cases.
     *
     * This enumeration can be used to define and manage text transformation rules,
     * such as converting text to upper case, lower case, title case, etc.
     *
     * @since 1.0.0
     */
    TEXT_CASE(TextCase::class, macroType = MacroType.ENUM),

    /**
     * Represents a TABLE enumeration linked to the Table class.
     * This enumeration can be used to identify or relate to the Table class
     * programmatically.
     *
     * @since 1.0.0
     */
    TABLE(Table::class, macroType = MacroType.COLLECTION),

    /**
     * Represents a Kotlin type categorized as a Tree structure.
     *
     * This classification is based on its hierarchical and nested structure,
     * commonly used to represent organized data relationships such as XML data,
     * hierarchical file systems, or abstract syntax trees.
     *
     * @since 1.0.0
     */
    TREE(Tree::class, macroType = MacroType.OBJECT),

    /**
     * Represents a tree-structured node type in a Kotlin type system.
     *
     * The TREE_NODE type allows working with tree-like hierarchical structures.
     * Often used in data models, syntax trees, or representation of hierarchical data.
     *
     * @since 1.0.0
     */
    TREE_NODE(TreeNode::class, macroType = MacroType.OBJECT),

    /**
     * Represents an error type in the system.
     *
     * This constant is typically used to define or categorize certain error states
     * associated with the provided class type.
     *
     * @since 1.0.0
     */
    ERROR(Error::class, isKotlinBaseType = true, macroType = MacroType.THROWABLE),

    /**
     * Represents a constant associated with Kotlin's `Exception` class type.
     *
     * This symbol is part of a type enumeration within the containing class
     * and is used to identify or distinguish the `Exception` class in specific contexts
     * or classifications.
     *
     * @since 1.0.0
     */
    EXCEPTION(Exception::class, isKotlinBaseType = true, macroType = MacroType.THROWABLE),

    /**
     * Represents a functional type within the KotlinType class.
     *
     * This symbolic constant is used to denote a specific type classification
     * for handling functions. It provides metadata and utility for identifying
     * and managing functional types in the type system.
     *
     * @since 1.0.0
     */
    FUNCTION(Function::class, isInterface = true, isKotlinBaseType = true, macroType = MacroType.FUNCTION),

    /**
     * Represents a Kotlin type corresponding to a temporal field.
     *
     * This instance is a specific specialization of `KotlinType` with its primary type being
     * `TemporalField`. It signifies an interface that operates within the context of time-related
     * fields, commonly utilized for defining or processing temporal data constructs in time-based
     * systems or applications.
     *
     * The `MacroType` associated with this type is `TEMPORAL_PART`, which categorizes it as a
     * segment or a part of time-related concepts, enabling detailed or granular handling of
     * temporal components.
     *
     * @since 1.0.0
     */
    TEMPORAL_FIELD(TemporalField::class, isJavaType = true, isInterface = true, macroType = MacroType.TEMPORAL_PART),

    /**
     * Represents a Kotlin type definition for the temporal unit construct.
     *
     * This type is specialized for handling and categorizing temporal units in the system.
     * It is marked as derived from the `TemporalUnit` class, indicating its association
     * with Java's time-related abstractions. The type is further specified as an interface
     * and categorized under the `TEMPORAL_PART` macro type, which refers to subdivisions
     * of time-related components.
     *
     * @since 1.0.0
     */
    TEMPORAL_UNIT(TemporalUnit::class, isJavaType = true, isInterface = true, macroType = MacroType.TEMPORAL_PART),

    /**
     * Represents a lightweight, inline-typed wrapper for JSON-formatted string values.
     *
     * The `JSON` class is a value class optimized for handling JSON string representations.
     * It enforces strong typing for JSON content while retaining compatibility with
     * common serialization and deserialization frameworks.
     *
     * This class implements the `CharSequence` interface, allowing manipulation of
     * the underlying JSON string while maintaining type safety.
     *
     * The `JSON` wrapper is annotated with `@JsonSerialize` and `@JsonDeserialize` to support
     * seamless integration with JSON processing libraries for serialization and deserialization.
     *
     * @since 1.0.0
     */
    JSON(dev.tommasop1804.kutils.classes.coding.JSON::class, macroType = MacroType.STRING),

    /**
     * Represents a YAML configuration or structure associated with a specific `KClass`
     * and categorized under a defined `MacroType`.
     * This class acts as a container or descriptor, enabling users to interpret,
     * categorize, or process YAML-related information programmatically.
     *
     * @param kClass The `KClass` reference that associates the YAML instance with a specific type.
     * @param macroType Defines the `MacroType` that categorizes the YAML instance for broader use cases.
     * @since 1.0.0
     */
    YAML(dev.tommasop1804.kutils.classes.coding.YAML::class, macroType = MacroType.STRING);

    /**
     * Returns the simple name of the class represented by this enum value.
     *
     * @return The simple name of the class.
     */
    val simpleName: String
        get() = kClass.simpleName ?: "Unknown"
    
    /**
     * Returns the fully qualified name of the class represented by this enum value.
     *
     * @return The fully qualified name of the class.
     */
    val qualifiedName: String
        get() = kClass.qualifiedName ?: "Unknown"
    
    companion object {
        /**
         * A filtered list of entries containing only those that represent interface types.
         *
         * This variable filters the `entries` collection, retaining only elements
         * for which the `isInterface` property of the `KotlinType` evaluates to `true`.
         *
         * @since 1.0.0
         */
        val interfaceEntries = entries.sortedBy(ObjType::name).filter(ObjType::isInterface)

        /**
         * A filtered collection of entries containing only those that are classified as Kotlin base types.
         *
         * The filtering operation is performed by checking the `isKotlinBaseType` property
         * of each entry in the original collection.
         *
         * @since 1.0.0
         */
        val kotlinBaseTypeEntries = entries.sortedBy(ObjType::name).filter(ObjType::isKotlinBaseType)

        /**
         * A filtered collection of entries containing only the ones identified as Java types.
         *
         * This property processes a collection of `KotlinType` objects, applying the predicate
         * defined by the `isJavaType` property, and returns the subset of entries that fulfill this condition.
         *
         * Commonly used to isolate and work on types specifically originating from Java, such as
         * when working in mixed-language projects or performing type system analysis.
         *
         * @since 1.0.0
         */
        val javaTypeEntries = entries.sortedBy(ObjType::name).filter(ObjType::isJavaType)

        /**
         * Represents a filtered list of entries that exclude those classified as Java types or Kotlin base types.
         *
         * This variable is commonly used to isolate entries that pertain to custom types,
         * allowing operations or processing exclusively on non-standard types.
         *
         * @since 1.0.0
         */
        val customTypeEntries = entries.sortedBy(ObjType::name).filter { !it.isJavaType && !it.isKotlinBaseType }

        /**
         * A filtered list of entries containing only those compiled as primitives.
         *
         * This variable holds a subset of entries from the context, specifically those
         * for which the `isCompiledAsPrimitive` condition evaluates to true. It is useful
         * in scenarios where operations or processes are limited to entries compiled as
         * primitive types.
         *
         * @since 1.0.0
         */
        val primitiveEntries = entries.sortedBy(ObjType::name).filter(ObjType::isCompiledAsPrimitive)

        /**
         * Finds the enum value corresponding to the given KClass.
         *
         * @param kClass The KClass to find the enum value for.
         * @return The corresponding enum value, or null if not found.
         * @since 1.0.0
         */
        infix fun ofKClass(kClass: KClass<*>): ObjType? = entries.find { it.kClass == kClass }
        
        /**
         * Finds the enum value corresponding to the given Class.
         *
         * @param `class` The Class to find the enum value for.
         * @return The corresponding enum value, or null if not found.
         * @since 1.0.0
         */
        infix fun ofClass(`class`: Class<*>): ObjType? = entries.find { it.kClass.java == `class` }
        
        /**
         * Filters the entries based on the provided macro type.
         *
         * This method returns a list of entries where the `macroType` matches the given parameter.
         *
         * @param macroType The `MacroType` to filter entries by.
         * @return A list of entries with the specified `macroType`.
         * @since 1.0.0
         */
        infix fun byMacroType(macroType: MacroType) = entries.filter { it.macroType == macroType }
    }

    /**
     * Validates if the specified object matches the expected type based on the context
     * of the current `KotlinType` instance. The validation supports a variety of types,
     * including `Email`, `PhoneNumber`, `Hex`, `Color`, `UUID`, `Currency`, `ZoneIdent`,
     * and `Sex`. Custom logic is applied for each type to ensure proper validation,
     * including fallback logic or parsing when necessary.
     *
     * @param obj The object to be validated against the type defined by the `KotlinType` instance.
     * @param force A boolean flag indicating whether type-specific validation logic should be forced.
     *              If set to `true`, the method applies stricter validation rules (e.g., format parsing or
     *              instance checks). If set to `false`, it only validates by type instance.
     *              Defaults to `false`.
     * @since 1.0.0
     */
    operator fun invoke(obj: Any?, force: Boolean = false) = if (force) when {
        this == EMAIL -> obj is Contact.Email || (obj is String && obj.isValidEmail())
        this == PHONE_NUMBER -> {
            if (obj is Contact.PhoneNumber) true
            else try { Contact.PhoneNumber(obj.toString()); true } catch (e: Exception) { false }
        }
        this == HEX -> obj is Hex || (obj is String && obj.toHex().getOrNull().isNotNull())
        this == COLOR -> {
            if (obj is Color || obj is java.awt.Color) true
            else try { Color.ofHEX(obj.toString()); true } catch (e: Exception) { false }
        }
        this == UUID -> {
            if (obj is UUID) true
            else try { java.util.UUID.fromString(obj.toString()); true } catch (e: Exception) { false }
        }
        this == CURRENCY -> {
            if (obj is Currency || obj is java.util.Currency) true
            try { Currency.valueOf(+obj.toString()); true } catch (e: Exception) { false }
        }
        this == ZONE_IDENT -> {
            if (obj is ZoneIdent || obj is ZoneId) true
            try {
                TimeZoneDesignator.valueOf(+obj.toString())
                true
            } catch (e: Exception) {
                try {
                    TimeZone.valueOf(+obj.toString())
                    true
                } catch (e: Exception) {
                    TimeZone.of(+obj.toString()).isNotEmpty()
                }
            }
        }
        this == SEX -> {
            if (obj is Sex) true
            try {
                Sex.valueOf(+obj.toString())
                true
            } catch (e: Exception) {
                expect(obj.toString().length, 1)
                Sex.of(obj.toString()[0]).isNotNull()
            }
        }
        macroType == MacroType.ENUM ->
            try { obj.toString() in kClass.java.enumConstants; true } catch (e: Exception) { false }
        else -> kClass.isInstance(obj)
    } else when (this) {
        EMAIL -> obj is Contact.Email || (obj is String && obj.isValidEmail())
        PHONE_NUMBER -> {
            if (obj is Contact.PhoneNumber) true
            else try { Contact.PhoneNumber(obj.toString()); true } catch (e: Exception) { false }
        }
        JSON -> {
            if (obj is JSON) true
            else try {
                dev.tommasop1804.kutils.classes.coding.JSON(obj.toString()); true } catch (e: Exception) { false }
        }
        YAML -> {
            if (obj is YAML) true
            else try {
                dev.tommasop1804.kutils.classes.coding.YAML(obj.toString()); true } catch (e: Exception) { false }
        }
        else -> kClass.isInstance(obj)
    }

    /**
     * Represents different types of macros that can be processed or categorized.
     *
     * This enum class defines various macro types that are typically utilized
     * for differentiating and handling various data structures, primitive types,
     * and special constructs in application logic.
     *
     * @since 1.0.0
     */
    enum class MacroType {
        /**
         * Represents a character-based macro type within the `MacroType` enum.
         *
         * This type is used to denote macro elements or components
         * that rely on or correspond to a single character.
         *
         * @since 1.0.0
         */
        CHARACTER,
        /**
         * Represents the STRING macro type within the `MacroType` enum.
         *
         * This constant is used to identify and define macro behavior associated
         * with string-based data or text representations.
         *
         * @since 1.0.0
         */
        STRING,
        /**
         * Represents the `NUMBER` type in the `MacroType` enum.
         *
         * This enum constant is used to identify numerical values within the `MacroType`.
         * It signifies that the associated data or behavior pertains to numbers.
         *
         * @since 1.0.0
         */
        NUMBER,
        /**
         * Represents a macro type for temporal data.
         *
         * This macro type is used to categorize items related to temporal entities
         * such as dates, times, or other time-related constructs.
         *
         * It serves as a symbolic identifier within the `MacroType` enum for time-based data processing.
         *
         * @since 1.0.0
         */
        TEMPORAL,
        /**
         * Represents a type of temporal amount.
         *
         * This enumeration is part of the `MacroType` and is used to classify
         * temporal amounts such as durations or periods.
         *
         * @since 1.0.0
         */
        TEMPORAL_AMOUNT,
        /**
         * Represents a temporal part within the `MacroType` context.
         *
         * This enumeration constant identifies instances or subdivisions of time-related components
         * associated with a larger macro entity or concept. It is used to symbolize and categorize
         * specific temporal segments or attributes relevant to the overarching context.
         *
         * @since 1.0.0
         */
        TEMPORAL_PART,
        /**
         * Represents the BOOLEAN macro type in the `MacroType` enumeration.
         *
         * This type is used to indicate a boolean value, typically representing
         * true or false states, and is commonly employed in logical operations
         * or conditions.
         *
         * It serves as a categorization for handling boolean data in contexts
         * where types need to be explicitly defined.
         *
         * @since 1.0.0
         */
        BOOLEAN,
        /**
         * Represents the enumeration type category within the `MacroType` enum.
         *
         * This type is used to categorize and identify values or variables
         * that are instances of Kotlin or Java `enum` types. It signifies
         * a specific classification for enumerable constants in programming.
         *
         * @since 1.0.0
         */
        ENUM,
        /**
         * Represents a type that denotes a collection of elements.
         *
         * This type is used to classify objects that can store multiple related items,
         * such as lists, sets, or other data structure collections.
         *
         * It is often utilized in scenarios where grouping or handling multiple objects
         * within a single entity is required, providing flexibility in data organization.
         *
         * @since 1.0.0
         */
        COLLECTION,
        /**
         * Represents the `MAP` type within the `MacroType` enumeration.
         *
         * This type is used to identify and classify a value or structure as a Map.
         * It is typically used in contexts where map-like key-value pair constructs
         * are handled or required.
         *
         * @since 1.0.0
         */
        MAP,
        /**
         * Represents the type for arrays in the `MacroType` enumeration.
         *
         * This type is utilized when defining or categorizing objects
         * that are identified as arrays, providing a description specific
         * to array-based data structures or constructs.
         *
         * @since 1.0.0
         */
        ARRAY,
        /**
         * Represents a tuple type within the macro type system.
         *
         * This enum constant is utilized to define a collection of fixed-size, ordered elements,
         * typically with varying types. It is commonly used in scenarios where data must be grouped
         * in a structured manner while preserving element order and potentially supporting
         * heterogeneous data types.
         *
         * @since 1.0.0
         */
        TUPLE,
        /**
         * Represents an object type within the `MacroType` enumeration.
         *
         * This type is typically used to signify complex structures
         * or non-primitive value types in the context where macro types are utilized.
         *
         * @since 1.0.0
         */
        OBJECT,
        /**
         * Represents a specialized Kotlin type within the `MacroType` enumeration.
         *
         * This type is used to define specific object categories or types that are unique
         * to Kotlin programming. It can be employed in contexts where differentiation
         * of Kotlin-specific structures or constructs is required.
         *
         * @since 1.0.0
         */
        KOTLIN_SPECIAL_TYPE,
        /**
         * Represents a throwable type within the context of macro processing.
         *
         * This type is used to handle or categorize throwable instances
         * in the system, such as exceptions and errors.
         *
         * Typically utilized where throwable-related operations are required,
         * ensuring proper handling and type safety within the macro system.
         *
         * @since 1.0.0
         */
        THROWABLE,
        /**
         * Represents a function type, typically used for defining behavior or logic that can be invoked.
         *
         * This constant is part of the `MacroType` enum and is used to categorize entities
         * that are of function type, often for processing or procedural logic purposes.
         *
         * @since 1.0.0
         */
        FUNCTION
    }
}
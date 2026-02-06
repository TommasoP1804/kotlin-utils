@file:JvmName("ReflectionUtilsKt")
@file:Suppress("unused")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import dev.tommasop1804.kutils.exceptions.PropertyNotAccessibleException
import dev.tommasop1804.kutils.exceptions.PropertyNotFoundException
import kotlin.reflect.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * Converts the properties of an object into a map representation, optionally excluding specific properties.
 *
 * @param excludeProperties A set of property names to exclude from the resulting map. Defaults to an empty set.
 * @return A map where the keys are property names and the values are the corresponding property values of the object.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMap(vararg excludeProperties: String = emptyArray()): DataMap =
    T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in excludeProperties }
        .associate { prop -> prop.name to prop.get(this) }

/**
 * Converts the public properties of an object into a map representation, excluding specific properties if needed.
 *
 * @param T The type of the object whose properties will be transformed into a map.
 * @param excludeProperties The set of properties to exclude from the resulting map.
 * @return A map containing the names and values of the public properties of the object, excluding the specified properties.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMap(vararg excludeProperties: KProperty<*>): DataMap {
    val exlcudePropsName = excludeProperties.map { it.name }
    return T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in exlcudePropsName }
        .associate { prop -> prop.name to prop.get(this) }
}

/**
 * Converts the non-null properties of an object into a map representation.
 *
 * @param excludeProperties A set of property names to exclude from the resulting map.
 * @return A map where the keys are property names and the values are the corresponding non-null property values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapNotNull(vararg excludeProperties: String = emptyArray()): DataMapNN =
    T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in excludeProperties }
        .mapNotNull { prop ->
            prop.get(this)?.let { value -> prop.name to value }
        }
        .toMap()

/**
 * Creates a map representation of the properties of the invoking object, excluding null values and specified properties.
 *
 * Only properties with public visibility are included in the resulting map.
 *
 * @param excludeProperties The properties to be excluded from the resulting map.
 * @return A DataMapNN containing the names and values of the included properties, excluding null values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapNotNull(vararg excludeProperties: KProperty<*>): DataMapNN {
    val exlcudePropsName = excludeProperties.map { it.name }
    return T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in exlcudePropsName }
        .mapNotNull { prop ->
            prop.get(this)?.let { value -> prop.name to value }
        }
        .toMap()
}

/**
 * Converts the properties of an object into a map representation, including only specific properties.
 *
 * @param includeProperties A set of property names to include in the resulting map.
 * @return A map where the keys are property names and the values are the corresponding property values of the object.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapIncluding(vararg includeProperties: String = emptyArray()): DataMap =
    T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.name in includeProperties }
        .associate { prop -> prop.name to prop.get(this) }

/**
 * Converts an object of type [T] into a DataMap representation by reflecting on its public properties
 * and including only the specified properties.
 *
 * @param includeProperties The properties to include in the resulting DataMap.
 *                          Only public properties specified in this parameter will be included.
 * @return A DataMap containing key-value pairs where keys are property names and values are their corresponding values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapIncluding(vararg includeProperties: KProperty<*>): DataMap {
    val includePropsName = includeProperties.map { it.name }
    return T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.name in includePropsName }
        .associate { prop -> prop.name to prop.get(this) }
}

/**
 * Converts the properties of an object into a map with custom key transformation.
 *
 * @param keyTransform Function to transform property names into map keys.
 * @param excludeProperties A set of property names to exclude from the resulting map.
 * @return A map where the keys are transformed property names and the values are the corresponding property values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapWithKeyTransform(keyTransform: Transformer<String, String>, vararg excludeProperties: String): DataMap =
    T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in excludeProperties }
        .associate { prop -> keyTransform(prop.name) to prop.get(this) }

/**
 * Converts the public properties of the object into a DataMap, transforming the property names
 * using the provided key transformation function, and excluding specified properties.
 *
 * @param keyTransform a function that transforms property names to desired keys in the resulting map
 * @param excludeProperties properties to exclude from the resulting map
 * @return a DataMap containing the transformed keys and their corresponding property values
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapWithKeyTransform(keyTransform: Transformer<String, String>, vararg excludeProperties: KProperty<*>): DataMap {
    val excludePropsName = excludeProperties.map { it.name }
    return T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .filterNot { it.name in excludePropsName }
        .associate { prop -> keyTransform(prop.name) to prop.get(this) }
}

/**
 * Converts the properties of an object into a map with snake_case keys.
 *
 * @param excludeProperties A set of property names to exclude from the resulting map.
 * @return A map where the keys are in snake_case format and the values are the corresponding property values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapSnakeCase(vararg excludeProperties: String = emptyArray()): DataMap =
    toReflectionMapWithKeyTransform(
        { key ->  -key.replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]}_${it.groupValues[2]}" } },
        *excludeProperties
    )

/**
 * Transforms the properties of the current object to a map, converting property names from camelCase to snake_case.
 *
 * @param excludeProperties A vararg array of properties to exclude from the resulting map.
 * @return A DataMap containing the mappings of snake_case property names to their corresponding values.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.toReflectionMapSnakeCase(vararg excludeProperties: KProperty<*>): DataMap =
    toReflectionMapWithKeyTransform(
        { key ->  -key.replace(Regex("([a-z])([A-Z])")) { "${it.groupValues[1]}_${it.groupValues[2]}" } },
        *excludeProperties
    )

/**
 * Gets the value of a property by name using reflection.
 *
 * @param propertyName The name of the property to retrieve.
 * @return The value of the property
 * @throws PropertyNotFoundException if the property does not exist.
 * @throws PropertyNotAccessibleException if the property is not public.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
infix inline fun <reified T : Any, R> T.getPropertyValue(propertyName: String) =
    T::class.memberProperties
        .find { it.name == propertyName }
        .requireNotNullOrThrow { PropertyNotFoundException(propertyName, T::class) }
        .requireOrThrow({ PropertyNotAccessibleException(this) }) { visibility == KVisibility.PUBLIC }
        .get(this) as R

/**
 * Checks if an object has a specific property.
 *
 * @param propertyName The name of the property to check.
 * @return True if the property exists and is public, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> T.hasProperty(propertyName: String): Boolean =
    T::class.memberProperties
        .any { it.name == propertyName && it.visibility == KVisibility.PUBLIC }

/**
 * Checks if an object has a specific property.
 *
 * @param propertyName The name of the property to check.
 * @return True if the property exists and is public, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> KClass<T>.hasProperty(propertyName: String): Boolean =
    memberProperties.any { it.name == propertyName && it.visibility == KVisibility.PUBLIC }

/**
 * Gets all public property names of an object.
 *
 * @return A set containing all public property names.
 * @since 1.0.0
 */
inline fun <reified T : Any> T.getPropertyNames(): StringSet =
    T::class.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .mappedTo { it.name }
        .toSet()

/**
 * Gets all public property names of an object.
 *
 * @return A set containing all public property names.
 * @since 1.0.0
 */
inline fun <reified T : Any> KClass<T>.getPropertyNames(): StringSet =
    memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .mappedTo { it.name }
        .toSet()

/**
 * Retrieves a property of the specified object by its name.
 *
 * @param name The name of the property to retrieve.
 * @return The property with the specified name
 * @throws PropertyNotFoundException if the property does not exist.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T: Any> T.getPropertyByName(name: String) =
    T::class.memberProperties
        .findOrThrow({ PropertyNotFoundException(name, T::class) }) { it.name == name }

/**
 * Retrieves a public property of the specified object by its name.
 *
 * @param name The name of the property to retrieve.
 * @return The property with the specified name
 * @throws PropertyNotFoundException if the property does not exist.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T: Any> KClass<T>.getPropertyByName(name: String) =
    memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .findOrThrow({ PropertyNotFoundException(name, T::class) }) { it.name == name } as KProperty<T>?

/**
 * Retrieves all public properties of the invoking object that match the specified condition.
 *
 * @param predicate A lambda function that defines the condition a property must satisfy
 *                  to be included in the resulting list.
 * @return A list of properties of the invoking object that are public and satisfy the given predicate.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T : Any> T.getPropertiesWhere(predicate: Predicate<KProperty<T>>) =
    T::class.memberProperties
        .mappedTo { it as KProperty<T> }
        .filter(predicate)

/**
 * Retrieves all public properties of the invoking object that match the specified condition.
 *
 * @param predicate A lambda function that defines the condition a property must satisfy
 *                  to be included in the resulting list.
 * @return A list of properties of the invoking object that are public and satisfy the given predicate.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T : Any> KClass<T>.getPropertiesWhere(predicate: Predicate<KProperty<T>>) =
    memberProperties
        .mappedTo { it as KProperty<T> }
        .filter(predicate)

/**
 * Retrieves all publicly visible properties of a class instance that match the specified type.
 *
 * @param type The [KType] of the properties to be retrieved.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T : Any> T.getPropertiesOfType(type: KType) =
        T::class.memberProperties
            .filter { it.returnType == type }
            .mappedTo { it as KProperty<T> }

/**
 * Retrieves all publicly visible properties of a class instance that match the specified type.
 *
 * @param type The [KType] of the properties to be retrieved.
 * @since 1.0.0
 */
@Suppress("unchecked_cast")
inline infix fun <reified T : Any> KClass<T>.getPropertiesOfType(type: KType) =
    memberProperties
        .filter { it.returnType == type }
        .mappedTo { it as KProperty<T> }

/**
 * Retrieves a specific parameter of the function by its name.
 *
 * @param name the name of the parameter to search for.
 * @return the parameter with the specified name, or null if no such parameter exists.
 * @since 1.0.0
 */
infix fun <T> KFunction<T>.getParameterByName(name: String): KParameter? =
    parameters.find { it.name == name }

/**
 * Checks if the given object's class has a specified annotation.
 *
 * @param annotation the annotation class to check for on the object's class.
 * @return true if the annotation is present on the object's class, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> T.hasAnnotation(annotation: KClass<out Annotation>): Boolean =
    T::class.annotations.any { it.annotationClass == annotation }

/**
 * Checks if the given object has a specified annotation.
 *
 * @param annotation the annotation class to check for on the object.
 * @return true if the annotation is present on the object, false otherwise.
 * @since 1.0.0
 */
infix fun <T : Enum<*>> T.entryHasAnnotation(annotation: KClass<out Annotation>): Boolean =
    javaClass.getField(name).isAnnotationPresent(annotation.java)

/**
 * Checks if the given object's class has a specified annotation.
 *
 * @param annotation the annotation class to check for on the object's class.
 * @return true if the annotation is present on the object's class, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> T.classHasAnnotation(annotation: KClass<out Annotation>): Boolean =
    T::class.java.isAnnotationPresent(annotation.java)

/**
 * Extension property for obtaining the Kotlin class (`KClass`) of the declaring class of a property.
 * This property retrieves the class in which the KProperty is defined.
 * It internally accesses the `javaField` of the property to determine the declaring class
 * and casts it to the appropriate type parameter `T`.
 *
 * @receiver The `KProperty` for which the declaring class is being retrieved.
 * @return The Kotlin class (`KClass`) of the declaring class of the property, or null if inaccessible.
 * @since 1.0.0
 */
@Suppress("UPPER_BOUND_VIOLATED_IN_TYPE_OPERATOR_OR_PARAMETER_BOUNDS_WARNING")
val <T> KProperty<T>.ownerClass
    @Suppress("unchecked_cast")
    get() = (javaField?.declaringClass?.kotlin ?: javaGetter?.declaringClass?.kotlin) as? KClass<T>
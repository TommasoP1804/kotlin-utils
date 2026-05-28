/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("ReflectionUtilsKt")
@file:Suppress("unused", "DEPRECATION", "unchecked_cast")
@file:Since("1.0.0")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import dev.tommasop1804.kutils.classes.maps.*
import dev.tommasop1804.kutils.exceptions.*
import kotlin.reflect.*
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.javaGetter

/**
 * An extension property that generates a `PropertiesMap` for an object of type `T`.
 *
 * This property collects all publicly visible member properties of the object, retrieves their
 * corresponding values, and maps them to their respective property references (`KProperty1`).
 *
 * The resulting `PropertiesMap` contains key-value pairs where each key is a property reference
 * and its associated value is the value of that property for the current object. Properties with
 * non-public visibility are excluded from the map.
 *
 * @receiver An object of type `T` for which the member properties are retrieved and mapped.
 * @return A `PropertiesMap` containing the public member properties of the receiver as keys
 * and their corresponding values.
 * @since 3.10.0
 */
val <T : Any> T.memberPropertiesMap get() = this::class.memberProperties
    .filter { it.visibility == KVisibility.PUBLIC }
    .associateWith { prop -> @Suppress("UNCHECKED_CAST") (prop as KProperty1<Any, *>).get(this) }
    .let { PropertiesMap(it) }
/**
 * An extension property that generates a `PropertiesMap` for an object of type `T`.
 *
 * This property collects all publicly visible member properties of the object, retrieves their
 * corresponding values, and maps them to their respective property references (`KProperty1`).
 *
 * The resulting `PropertiesMap` contains key-value pairs where each key is a property reference
 * and its associated value is the value of that property for the current object. Properties with
 * non-public visibility are excluded from the map.
 *
 * All the nulls properties are filtered out from the resulting map.
 *
 * @receiver An object of type `T` for which the member properties are retrieved and mapped.
 * @return A `PropertiesMap` containing the public member properties of the receiver as keys
 * and their corresponding values.
 * @since 3.10.0
 */
val <T : Any> T.memberPropertiesMapNotNull get() = memberPropertiesMap.withoutNulls

/**
 * Extension property that retrieves the names of all public member properties of an instance's class.
 *
 * This property utilizes reflection to access the member properties of the instance's class and filters
 * them to include only those that are publicly visible. The resulting names are returned as a set of strings.
 *
 * @receiver Any instance of a class with properties to inspect.
 * @return A set of strings representing the names of the public member properties of the instance's class.
 * @since 3.10.0
 */
val <T : Any> T.memberPropertiesNames: Set<String> get() = this::class.memberProperties
    .filter { it.visibility == KVisibility.PUBLIC }
    .map { it.name }
    .toSet()

/**
 * Extension property for `KClass<*>` that retrieves the names of all member properties
 * with public visibility as a set of strings.
 *
 * The returned set contains only the names of the properties that are publicly accessible.
 * @since 3.10.0
 */
@get:JvmName("memberPropertiesNamesKClass")
val KClass<*>.memberPropertiesNames: Set<String> get() = this.memberProperties
    .filter { it.visibility == KVisibility.PUBLIC }
    .map { it.name }
    .toSet()

/**
 * Retrieves all member properties of the class as a set.
 *
 * This operator function allows the invocation of a `KClass` to directly obtain
 * its member properties. The returned set contains all the properties defined
 * in the class, including inherited ones.
 *
 * @receiver The Kotlin class (`KClass`) whose member properties are to be retrieved.
 * @return A set of all member properties of the class.
 * @since 3.10.0
 */
operator fun KClass<*>.invoke() = memberProperties.toSet()

/**
 * Gets the value of a property by name using reflection.
 *
 * @param propertyName The name of the property to retrieve.
 * @return The value of the property
 * @throws PropertyNotFoundException if the property does not exist.
 * @throws PropertyNotAccessibleException if the property is not public.
 * @since 1.0.0
 */
inline infix fun <reified T : Any, R> T.getPropertyValue(propertyName: String) =
    T::class.memberProperties
        .find { it.name == propertyName }
        .requireNotNullOrThrow { PropertyNotFoundException(propertyName, T::class) }
        .requireOrThrow({ PropertyNotAccessibleException(it) }) { it.visibility == KVisibility.PUBLIC }
        .get(this) as R

/**
 * Checks if an object has a specific property.
 *
 * @param propertyName The name of the property to check.
 * @return True if the property exists, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> T.hasProperty(propertyName: String): Boolean =
    T::class.memberProperties
        .any { it.name == propertyName }

/**
 * Checks if an object has a specific property.
 *
 * @param propertyName The name of the property to check.
 * @return True if the property exists, false otherwise.
 * @since 1.0.0
 */
inline infix fun <reified T : Any> KClass<T>.hasProperty(propertyName: String): Boolean =
    memberProperties.any { it.name == propertyName }

/**
 * Retrieves a property of a class by its name.
 *
 * @param name The name of the property to be retrieved.
 * @return The property found with the specified name or null if no such property exists.
 * @since 3.10.0
 */
inline infix fun <reified T: Any> T.getProperty(name: String) =
    T::class.memberProperties
        .find { it.name == name }
/**
 * Retrieves a property by its name from the given Kotlin class.
 *
 * @param name The name of the property to retrieve.
 * @return The property of the specified type if found, or null otherwise.
 * @since 3.10.0
 */
infix fun <T: Any> KClass<T>.getProperty(name: String) =
    memberProperties
        .find { it.name == name } as KProperty<T>?

/**
 * Retrieves a property by its name from the given Kotlin class.
 *
 * @param name The name of the property to retrieve.
 * @return The property of the specified type if found, or null otherwise.
 * @since 3.10.0
 */
operator fun <T : Any> KClass<T>.get(name: String) = getProperty(name)

/**
 * Retrieves the value of a specified property from the current object. If the property is not found,
 * it throws an exception provided by the `lazyException` function.
 *
 * @param name The name of the property to retrieve.
 * @param lazyException A function that supplies the exception to be thrown if the property is not found.
 * Defaults to a `PropertyNotFoundException` with the property name and the class type.
 * @return The value of the requested property.
 * @since 3.10.0
 */
inline fun <reified T: Any> T.getPropertyOrThrow(name: String, noinline lazyException: ThrowableSupplier = { PropertyNotFoundException(name, T::class) }) =
    T::class.memberProperties
        .findOrThrow(lazyException) { it.name == name }
/**
 * Retrieves a property of the given class by its name or throws an exception if it is not found.
 *
 * @param name The name of the property to retrieve.
 * @param lazyException A lambda that provides the exception to be thrown if the property is not found.
 *                       Defaults to throwing a `PropertyNotFoundException` with the provided name and class type.
 * @return The property of type `KProperty<T>`, if found.
 * @since 3.10.0
 */
fun <T: Any> KClass<T>.getPropertyOrThrow(name: String, lazyException: ThrowableSupplier = { PropertyNotFoundException(name, this) }) =
    memberProperties
        .findOrThrow(lazyException) { it.name == name } as KProperty<T>?

/**
 * Retrieves a property of the current object that matches the specified predicate.
 *
 * @param predicate A predicate function that is used to determine the desired property
 *                  from the list of member properties of the object's class.
 * @return The first property that matches the predicate, or null if no such property exists.
 * @since 3.10.0
 */
inline infix fun <reified T : Any> T.getProperty(predicate: Predicate<KProperty<T>>) =
    T::class.memberProperties
        .map { it as KProperty<T> }
        .find(predicate)

/**
 * Retrieves a property from the set of member properties of the class that matches the given predicate.
 *
 * @param predicate The condition used to identify the desired property.
 * @return The first property that satisfies the predicate, or null if no such property is found.
 * @since 3.10.0
 */
infix fun <T : Any> KClass<T>.getProperty(predicate: Predicate<KProperty<T>>) =
    memberProperties
        .map { it as KProperty<T> }
        .find(predicate)

/**
 * Retrieves a property from the set of member properties of the class that matches the given predicate.
 *
 * @param predicate The condition used to identify the desired property.
 * @return The first property that satisfies the predicate, or null if no such property is found.
 * @since 3.10.0
 */
operator fun <T : Any> KClass<T>.get(predicate: Predicate<KProperty<T>>) = getProperty(predicate)

/**
 * Retrieves the properties of the specified class type that satisfy the given predicate.
 *
 * @param predicate A lambda function that filters the properties of the class based on the specified condition.
 *                  The function takes a KProperty and returns a Boolean indicating whether the property satisfies the condition.
 * @return A list of properties that match the given predicate.
 * @since 3.10.0
 */
inline infix fun <reified T : Any> T.getProperties(predicate: Predicate<KProperty<T>>) =
    T::class.memberProperties
        .map { it as KProperty<T> }
        .filter(predicate)
/**
 * Retrieves the properties of the given class that satisfy the specified predicate.
 *
 * @param predicate A function used to filter the properties. The function takes a property of type `KProperty<T>`
 *                  and returns a boolean indicating whether the property should be included.
 * @since 3.10.0
 */
infix fun <T : Any> KClass<T>.getProperties(predicate: Predicate<KProperty<T>>) =
    memberProperties
        .map { it as KProperty<T> }
        .filter(predicate)

/**
 * Retrieves the properties of the given class that satisfy the specified predicate.
 *
 * @param predicate A function used to filter the properties. The function takes a property of type `KProperty<T>`
 *                  and returns a boolean indicating whether the property should be included.
 * @since 3.10.0
 */
operator fun <T : Any> KClass<T>.invoke(predicate: Predicate<KProperty<T>>) = getProperties(predicate)

/**
 * Retrieves the properties of the specified type from the declared member properties of the calling object.
 *
 * @param type The type to filter the member properties against.
 * @return A list of properties that match the specified type.
 * @since 3.10.0
 */
inline infix fun <reified T : Any> T.getProperties(type: KType) =
        T::class.memberProperties
            .filter { it.returnType == type }
            .map { it as KProperty<T> }

/**
 * Retrieves the properties of a class that match the specified type.
 *
 * @param type The Kotlin type to filter the class properties by.
 * @return A list of properties of the class that match the specified type.
 * @since 3.10.0
 */
infix fun <T : Any> KClass<T>.getProperties(type: KType) =
    memberProperties
        .filter { it.returnType == type }
        .map { it as KProperty<T> }

/**
 * Retrieves the properties of a class that match the specified type.
 *
 * @param type The Kotlin type to filter the class properties by.
 * @return A list of properties of the class that match the specified type.
 * @since 3.10.0
 */
operator fun <T : Any> KClass<T>.invoke(type: KType) = getProperties(type)

/**
 * Retrieves a parameter with the specified name from the function's parameters.
 *
 * @param name The name of the parameter to retrieve.
 * @return The parameter with the specified name, or null if no such parameter exists.
 * @since 3.10.0
 */
infix fun <T> KFunction<T>.getParameter(name: String): KParameter? = parameters.find { it.name == name }
/**
 * Retrieves a parameter with the specified name from the function's parameters.
 *
 * @param name The name of the parameter to retrieve.
 * @return The parameter with the specified name, or null if no such parameter exists.
 * @since 3.10.0
 */
operator fun <T> KFunction<T>.get(name: String): KParameter? = getParameter(name)

/**
 * Finds and returns the first parameter of the function that matches the given predicate.
 *
 * @param predicate A condition used to find the desired parameter. The predicate is applied to each parameter of the function.
 * @return The first parameter that matches the predicate, or null if no such parameter exists.
 * @since 3.10.0
 */
infix fun <T> KFunction<T>.getParameter(predicate: Predicate<KParameter>): KParameter? = parameters.find(predicate)
/**
 * Finds and returns the first parameter of the function that matches the given predicate.
 *
 * @param predicate A condition used to find the desired parameter. The predicate is applied to each parameter of the function.
 * @return The first parameter that matches the predicate, or null if no such parameter exists.
 * @since 3.10.0
 */
operator fun <T> KFunction<T>.get(predicate: Predicate<KParameter>): KParameter? = getParameter(predicate)

/**
 * Filters the parameters of the function based on the given predicate.
 *
 * @param predicate a condition used to filter the parameters of the function
 * @return a list of parameters that satisfy the given predicate
 * @since 3.10.0
 */
infix fun <T> KFunction<T>.getParameters(predicate: Predicate<KParameter>) = parameters.filter(predicate)
/**
 * Filters the parameters of the function based on the given predicate.
 *
 * @param predicate a condition used to filter the parameters of the function
 * @return a list of parameters that satisfy the given predicate
 * @since 3.10.0
 */
infix operator fun <T> KFunction<T>.invoke(predicate: Predicate<KParameter>) = getParameters(predicate)

/**
 * Searches for an annotation of the specified type on the property itself, its backing Java field,
 * or the corresponding primary constructor parameter if available.
 *
 * @return The annotation of type [A] if found, or `null` if the annotation is not present.
 * @since 3.1.2
 */
inline fun <reified A : Annotation> KProperty<*>.findAnnotationAnywhere(): A? {
    findAnnotation<A>()?.let { return it }
    (this as? KProperty1<*, *>)?.javaField?.getAnnotation(A::class.java)?.let { return it }
    (this as? KProperty1<*, *>)
        ?.let { prop ->
            prop.javaField?.declaringClass?.kotlin?.primaryConstructor
                ?.parameters
                ?.firstOrNull { it.name == prop.name }
                ?.findAnnotation<A>()
        }?.let { return it }
    return null
}

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
    get() = (javaField?.declaringClass?.kotlin ?: javaGetter?.declaringClass?.kotlin) as? KClass<T>
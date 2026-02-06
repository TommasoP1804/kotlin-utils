@file:Suppress("contains_as_in_operator")

package dev.tommasop1804.kutils.exceptions

import dev.tommasop1804.kutils.EMPTY
import dev.tommasop1804.kutils.StringList
import dev.tommasop1804.kutils.isNotNull
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * Exception indicating that the provided arguments are in conflict.
 *
 * Thrown when a function or callable entity is provided with arguments that conflict in some way,
 * making it impossible to execute the operation.
 *
 * This class extends [IllegalArgumentException], inheriting functionality for handling
 * exceptions caused by invalid arguments.
 *
 * Primary constructors allow specifying details about the conflict, including the conflicting
 * callable, parameter names, or parameter values, and optionally the cause of the exception.
 *
 * @constructor Creates an instance of the exception with various constructor overloads
 * for specifying context about the conflict.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
open class ParametersInConflictException : IllegalArgumentException {
    /**
     * Initializes a new instance of the ArgumentsInConflictException class with no detail message.
     *
     * This constructor creates an exception instance without providing specific error details
     * or additional context, indicating a generic conflict in arguments.
     *
     * @since 1.0.0
     */
    constructor() : super()

    /**
     * Constructs an `ArgumentsInConflictException` with the specified detail message.
     *
     * This constructor is used to create an exception instance with a detail message
     * that provides additional context regarding the conflict in arguments. The message
     * can be helpful for debugging or logging the specifics of the error.
     *
     * @param message The detail message associated with the exception, or null if no message is provided.
     * @since 1.0.0
     */
    constructor(message: String?) : super(message)

    /**
     * Constructs an `ArgumentsInConflictException` with the specified detail message and cause.
     *
     * This constructor is used to create an exception instance by specifying a detailed
     * error message and the underlying cause of the conflict. It is useful when there are
     * conflicting arguments or conditions, enabling exception chaining and providing
     * additional context about the conflict scenario.
     *
     * @param message A detailed message describing the conflict error, or null if no specific message is provided.
     * @param cause The cause of this exception, or null if no cause is specified.
     * @since 1.0.0
     */
    constructor(message: String?, cause: Throwable?) : super(message, cause)

    /**
     * Constructs an `ArgumentsInConflictException` with the specified cause.
     *
     * This constructor is used to create an exception instance that encapsulates
     * the underlying cause of a conflict in arguments. It facilitates exception chaining
     * by associating the current exception with the originating exception, helping to
     * identify the root cause of the issue.
     *
     * @param cause The underlying cause of this exception, or null if no cause is provided.
     * @since 1.0.0
     */
    constructor(cause: Throwable?) : super(cause)

    /**
     * Constructs an `ArgumentsInConflictException` with information about the conflicting parameters
     * of the given callable and the related conflicting values.
     *
     * This constructor generates an exception message based on the provided callable's name, the
     * names of parameters in conflict, and optionally, the values that are causing the conflict.
     * If parameter names are specified, only those parameters from the callable are included in
     * the conflict details. If conflict values are provided, they are included in the exception
     * message to give additional context.
     *
     * @param callable The callable whose parameters are in conflict.
     * @param parametersName A set of parameter names that are in conflict. If null, no specific parameters are considered.
     * @param valuesInConflict A set of values causing the conflict. If null, no conflicting values are specified.
     * @since 1.0.0
     */
    @Suppress("contains_as_in_operator")
    constructor(callable: KFunction<*>, parametersName: StringList? = null, valuesInConflict: Set<Any?>? = null) : super(
        $$"<parameters of `$${callable.name}`>$${if (parametersName.isNotNull()) $$"$$${
            callable.parameters.filter { parametersName.distinct().contains(it.name) }
                .joinToString { "`${it.name}` of type `${it.type}`" }
        }" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )

    /**
     * Constructs an `ArgumentsInConflictException` with detailed information about the conflicting arguments.
     *
     * This constructor builds an exception instance by extracting and formatting details
     * regarding parameters of a given callable function and their associated conflicts.
     * It dynamically describes which parameters are in conflict, includes their type information,
     * and optionally specifies values in conflict. When no conflict values are provided,
     * the message includes only parameter details.
     *
     * @param callable The callable function whose parameters are in conflict.
     * @param parameters The subset of parameters causing conflict in the callable function.
     * @param valuesInConflict The optional set of values that are in conflict for the specified parameters, or null if not applicable.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>, parameters: Set<KParameter>, valuesInConflict: Set<Any?>? = null) : super(
        $$"<parameters of `$${callable.name}`>$${if (parameters.isNotNull()) $$"$$${
            callable.parameters.filter { parameters.map(KParameter::name).contains(it.name) }
                .joinToString { "`${it.name}` of type `${it.type}`" }
        }" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )
    
    /**
     * Constructs an `ArgumentsInConflictException` with details about the conflicting arguments.
     *
     * This exception is intended to signal conflicts between arguments passed to a callable entity.
     * It generates a descriptive error message based on the provided argument names and conflicting values.
     *
     * @param callableName The name of the callable that encountered argument conflicts.
     * @param parametersName The set of parameter names associated with the conflict, or `null` if no specific parameter names are provided.
     * @param valuesInConflict The set of conflicting values, or `null` if conflict details are not specified.
     * @since 1.0.0
     */
    constructor(callableName: String, parametersName: StringList? = null, valuesInConflict: Set<Any?>? = null) : super(
        $$"<parameters of `$$callableName`>$${if (parametersName.isNotNull()) $$"$$${parametersName.distinct().joinToString { "`$it`" }}" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )
    
    /**
     * Constructs a new instance of `ArgumentsInConflictException` with the specified parameters.
     *
     * This exception indicates a conflict between the provided arguments for a callable.
     * It generates a detailed error message highlighting the conflict between the parameters
     * and optionally the conflicting values.
     *
     * @param callableName The name of the callable function or member where the conflict occurred.
     * @param parameters The set of parameters that are in conflict. Each parameter entity includes its name and type.
     * @param valuesInConflict The values that caused the conflict, or null if no specific conflicting values are provided.
     * @since 1.0.0
     */
    constructor(callableName: String, parameters: Set<KParameter>, valuesInConflict: Set<Any?>? = null) : super(
        $$"<parameters of `$$callableName`>$${if (parameters.isNotNull()) $$"$$${parameters.joinToString { "`${it.name}` of type `${it.type}`" }}" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )

    /**
     * Constructs an `ArgumentsInConflictException` with the given parameters.
     *
     * This constructor is used to create an instance of `ArgumentsInConflictException`
     * that describes a conflict between parameters of a callable function. The conflict
     * details can include the names of the conflicting parameters and their associated values.
     *
     * @param callable The callable function whose parameters are in conflict.
     * @param parametersName A set of parameter names that are in conflict, or null if not applicable.
     * @param valuesInConflict A set of values causing the conflict, or null if not specified.
     * @param cause The underlying cause of the conflict, or null if not specified.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>, parametersName: StringList? = null, valuesInConflict: Set<Any?>? = null, cause: Throwable?) : super(
        $$"<parameters of `$${callable.name}`>$${if (parametersName.isNotNull()) $$"$$${
            callable.parameters.filter { parametersName.distinct().contains(it.name) }
                .joinToString { "`${it.name}` of type `${it.type}`" }
        }" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}",
        cause)

    /**
     * Constructs an `ArgumentsInConflictException` for a callable function when a set of parameters
     * are in conflict, optionally with conflicting values and an underlying cause.
     *
     * This exception is raised to signal that there is an ambiguity in the provided arguments
     * when executing the specified callable function. The conflict may arise due to multiple
     * parameters being improperly defined or a mismatch in values provided for certain arguments.
     *
     * @param callable The callable function whose parameters are in conflict.
     * @param parameters The set of conflicted parameters causing ambiguity in the callable.
     * @param valuesInConflict Optional. The set of conflicting values for the parameters, if applicable, or null.
     * @param cause Optional. The underlying cause of the parameter conflict, or null if not specified.
     * @since 1.0.0
     */
    constructor(callable: KFunction<*>, parameters: Set<KParameter>, valuesInConflict: Set<Any?>? = null, cause: Throwable?) : super(
        $$"<parameters of `$${callable.name}`>$${if (parameters.isNotNull()) $$"$$${
            callable.parameters.filter { parameters.map(KParameter::name).contains(it.name) }
                .joinToString { "`${it.name}` of type `${it.type}`" }
        }" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}",
        cause)

    /**
     * Constructs an `ArgumentsInConflictException` with a detailed message describing
     * the conflict among parameter arguments or their values, and an optional cause.
     *
     * This constructor is designed to provide context for exceptions caused by conflicting
     * arguments in certain method or function calls. The description includes the name
     * of the callable element, any conflicting parameter names, and values that are in conflict,
     * if provided. An optional `cause` can also be specified for exception chaining and debugging purposes.
     *
     * @param callableName The name of the callable element (e.g., method, function) whose parameters are in conflict.
     * @param parametersName The set of parameter names that are involved in the conflict, or `null` if not specified.
     * @param valuesInConflict The set of argument values causing the conflict, or `null` if not specified.
     * @param cause The cause of this exception, or `null` if no specific cause is provided.
     * @since 1.0.0
     */
    constructor(callableName: String, parametersName: StringList? = null, valuesInConflict: Set<Any?>? = null, cause: Throwable?) : super(
        $$"<parameters of `$$callableName`>$${if (parametersName.isNotNull()) $$"$$${parametersName.distinct().joinToString { "`$it`" }}" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )

    /**
     * Constructs an `ArgumentsInConflictException` with contextual information
     * about conflicting arguments in a callable's parameters.
     *
     * This exception is thrown when there is a conflict among the parameters of a callable,
     * potentially due to ambiguous or contradictory values. Relevant parameters, their types,
     * and the conflicting values are included in the exception message for improved clarity.
     *
     * @param callableName The name of the callable where the parameter conflict occurred.
     * @param parameters A set of parameters involved in the conflict, including their names and types.
     * @param valuesInConflict Optional conflicting values associated with the parameters, or null if unavailable.
     * @param cause The cause of this exception, potentially null.
     * @since 1.0.0
     */
    constructor(callableName: String, parameters: Set<KParameter>, valuesInConflict: Set<Any?>? = null, cause: Throwable?) : super(
        $$"<parameters of `$$callableName`>$${if (parameters.isNotNull()) $$"$$${parameters.joinToString { "`${it.name}` of type `${it.type}`" }}" else String.EMPTY} are in conflict$${if (valuesInConflict.isNotNull()) " with values ${valuesInConflict.joinToString { "`$it`" }}" else String.EMPTY}"
    )
}
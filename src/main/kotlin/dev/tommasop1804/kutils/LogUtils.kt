/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

@file:JvmName("LogUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused", "functionName")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import kotlin.reflect.KClass

/**
 * An interface providing a logger instance for the implementing class.
 * Classes implementing this interface can use the predefined `logger` property
 * to log information, warnings, or errors. The logger is automatically associated
 * with the class or its enclosing class in the case of nested structures.
 * @author Tommaso Pastorelli
 * @since 4.8.0
 */
interface Loggable {
    /**
     * Provides a logger instance for the implementing class.
     * The logger is initialized using the enclosing class if available,
     * or the actual class if no enclosing class is present. This ensures
     * that the log output corresponds to the specific class using the logger.
     * @since 4.8.0
     */
    val logger: Logger get() {
        val javaClass = this::class.java
        val clazz = javaClass.enclosingClass ?: javaClass
        return LoggerFactory.getLogger(clazz)
    }

    /**
     * A logger instance specific to the runtime class of the current object.
     * This logger is initialized with the exact runtime class of the object,
     * regardless of whether it is nested or not. It ensures that log messages
     * are tagged with the most precise class context.
     * @since 4.8.0
     */
    val loggerOfThis: Logger get() = LoggerFactory.getLogger(this::class.java)
}

/**
 * Represents different levels of logging severity.
 * Each log level is associated with an integer value and a string name.
 *
 * @param levelInt The integer representation of the log level.
 * @param levelName The string name of the log level.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@MustUseReturnValues
enum class LogLevel(val levelInt: Int, val levelName: String) {
    /**
     * Represents the error logging level.
     *
     * This logging level is used to indicate serious issues that need immediate attention.
     * It corresponds to the integer value 40 and the string representation "ERROR".
     *
     * @since 4.0.0
     */
    Error(40, "ERROR"),
    /**
     * Represents the WARN log level, commonly used to indicate potentially harmful situations
     * that may require attention but are not immediately critical.
     * 
     * @since 4.0.0
     */
    Warn(30, "WARN"),
    /**
     * Represents the INFO log level with a severity integer value of 20 and a name "INFO".
     *
     * This log level is used to indicate informational messages that highlight the progress of the application at a coarse-grained level.
     *
     * @since 4.0.0
     */
    Info(20, "INFO"),
    /**
     * Represents the DEBUG log level, typically used for detailed diagnostic messages 
     * that are useful during software development and debugging.
     * 
     * @since 4.0.0
     */
    Debug(10, "DEBUG"),
    /**
     * Represents the TRACE logging level with the lowest severity.
     *
     * TRACE is used to log fine-grained informational events that are primarily
     * useful for debugging and provide detailed insight into the application's behavior.
     *
     * @since 4.0.0
     */
    Trace(0, "TRACE");

    /**
     * Returns the string representation of the `LogLevel` instance.
     *
     * This method retrieves the `levelName` property to represent the
     * current logging level as a string.
     *
     * @return The string value of the `levelName` property.
     * @since 4.0.0
     */
    override fun toString() = levelName

    /**
     * Converts the current `LogLevel` instance to its corresponding SLF4J `Level`.
     *
     * Uses the `levelName` property of the `LogLevel` enum to match and retrieve 
     * the associated SLF4J logging level.
     *
     * @return The corresponding `Level` from SLF4J for the current `LogLevel` instance.
     * @since 1.0.0
     */
    fun toSlf4jLevel() = Level.valueOf(levelName)
    /**
     * Converts the current `LogLevel` instance to its corresponding `java.util.logging.Level`.
     *
     * Utilizes the `levelName` property of the `LogLevel` instance to match and retrieve
     * the associated Java logging level. The method parses the `levelName` and maps it to
     * an equivalent `Level` from the `java.util.logging` package.
     *
     * @return The corresponding `Level` from `java.util.logging` for the current `LogLevel` instance.
     * @since 1.0.0
     */
    fun toJavaLogLevel(): java.util.logging.Level = java.util.logging.Level.parse(levelName)
}

/**
 * Creates a new Logger instance with the specified name.
 *
 * @param name The name of the logger to be created.
 * @return A Logger instance configured with the given name.
 * @since 1.0.0
 */
fun Logger(name: String): Logger = LoggerFactory.getLogger(name)
/**
 * Creates a Logger instance for the specified class.
 *
 * @param `class` The KClass instance representing the class for which the Logger is created.
 * @return A Logger instance associated with the specified class.
 * @since 1.0.0
 */
fun Logger(`class`: KClass<*>): Logger = LoggerFactory.getLogger(`class`.java)
/**
 * Provides a logger instance for the specified class type.
 *
 * This method uses the reified type parameter `T` to obtain the class
 * and associates a `Logger` instance with it. The logger can then
 * be used for logging messages, errors, and other information
 * relevant to the associated class.
 *
 * @return The logger instance associated with the specified class type.
 * @since 1.0.0
 */
inline fun <reified T> T.Logger(): Logger = LoggerFactory.getLogger(T::class.java)

/**
 * Logs a message at the specified logging level using the logger associated with the class of the receiver type.
 *
 * @param level The logging level to use for the message. Common levels include TRACE, DEBUG, INFO, WARN, and ERROR.
 * @param message The message to be logged.
 * @since 1.0.0
 */
inline fun <reified T> T.log(level: LogLevel, message: Any) = log(
    LoggerFactory.getLogger(T::class.java),
    level,
    message
)
/**
 * Logs a message with an associated throwable at the specified log level using a logger
 * derived from the class of the given instance.
 *
 * @param level The logging level to use for the message.
 * @param message An optional message to be logged. If null, a default value will be used.
 * @param throwable The throwable to be logged alongside the message.
 * @since 1.0.0
 */
inline fun <reified T> T.log(level: LogLevel, message: Any? = null, throwable: Throwable) = log(
    LoggerFactory.getLogger(T::class.java),
    level,
    message,
    throwable
)
/**
 * Logs a message and an optional throwable using the given logger at the specified log level.
 *
 * @param logger the Logger instance used to log the message
 * @param level the logging level specifying the severity of the log
 * @param message the message content to log, or null for an empty message
 * @param throwable the Throwable instance associated with the log entry
 * @since 1.0.0
 */
fun log(logger: Logger, level: LogLevel, message: Any?, throwable: Throwable) = when (level) {
    LogLevel.Trace -> logger.trace(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.Debug -> logger.debug(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.Info -> logger.info(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.Warn -> logger.warn(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.Error -> logger.error(message?.toString() ?: String.EMPTY, throwable)
}
/**
 * Logs a message at the specified log level using the provided logger.
 *
 * @param logger The logger instance used to log the message.
 * @param level The severity level of the log (TRACE, DEBUG, INFO, WARN, or ERROR).
 * @param message The message to be logged.
 * @since 1.0.0
 */
fun log(logger: Logger, level: LogLevel, message: Any) = when (level) {
    LogLevel.Trace -> logger.trace(message.toString())
    LogLevel.Debug -> logger.debug(message.toString())
    LogLevel.Info -> logger.info(message.toString())
    LogLevel.Warn -> logger.warn(message.toString())
    LogLevel.Error -> logger.error(message.toString())
}
/**
 * Logs a message with the specified logging level within the context of a `Logger` instance.
 *
 * This method serves as a contextual extension that allows seamless logging by infixing 
 * a `LogLevel` object with a message. The method requires a current `Logger` to be 
 * available in the context through the `logger` receiver.
 *
 * @param message The content or object to log at the specified logging level.
 * @since 1.0.0
 */
@JvmName("logContext")
context(logger: Logger)
infix fun LogLevel.log(message: Any) = log(logger, this, message)
/**
 * Logs the string representation of the current object using the specified logger and log level.
 *
 * @param logger The logger instance used to write the log message.
 * @param level The level of the log, which determines the severity or importance of the message.
 * @since 1.0.0
 */
@JvmName("logAnyReceiver")
fun <T> T.log(logger: Logger, level: LogLevel) = apply { log(logger, level, toString()) }
/**
 * Logs the current instance at the specified log level using the provided logger from the context.
 *
 * This method leverages the `Logger` instance available in the context to log the string representation
 * of the current instance (`toString()`) at the specified `LogLevel`.
 *
 * @param level The severity level of the log (TRACE, DEBUG, INFO, WARN, or ERROR).
 * @since 4.8.0
 */
@JvmName("logAnyReceiverContext")
context(logger: Logger)
infix fun <T> T.log(level: LogLevel) = apply { log(logger, level, toString()) }

/**
 * Logs a message at the specified logging level if the given condition is true.
 *
 * This function allows conditional logging based on a boolean predicate, helping to control
 * when a log message should be emitted for the receiver type.
 *
 * @param condition A boolean condition that determines whether the log message should be emitted.
 * @param level The logging level to use for the message. Supported levels include TRACE, DEBUG, INFO, WARN, and ERROR.
 * @param message The message to be logged if the condition is true.
 * @since 4.1.0
 */
inline fun <reified T> T.logIf(condition: Boolean, level: LogLevel, message: Any) {
    if (condition) log(level, message)
}
/**
 * Logs a message and throwable at the specified logging level if the given condition is true.
 *
 * @param condition A boolean value that determines whether the logging action should be executed.
 *                  If true, the log will be recorded; otherwise, the method does nothing.
 * @param level The logging level to use for the log entry. Specifies the severity of the log.
 * @param message An optional message to log. If null, a default message may be used.
 * @param throwable The throwable object to be logged along with the message.
 * @since 4.1.0
 */
inline fun <reified T> T.logIf(condition: Boolean, level: LogLevel, message: Any? = null, throwable: Throwable) {
    if (condition) log(level, message, throwable)
}
/**
 * Logs a message and optional throwable at the specified log level if the given condition is true.
 *
 * @param condition A boolean value that determines whether the log operation will occur. If true, the message is logged at the specified level.
 * @param logger The logger instance used to perform the logging.
 * @param level The level of severity for the log message, represented by the `LogLevel` enum.
 * @param message The message to log. If null, an empty string will be used.
 * @param throwable The optional throwable to include in the log, providing context about an exception or error.
 * @since 4.1.0
 */
fun logIf(condition: Boolean, logger: Logger, level: LogLevel, message: Any?, throwable: Throwable) {
    if (condition) {
        when (level) {
            LogLevel.Trace -> logger.trace(message?.toString() ?: String.EMPTY, throwable)
            LogLevel.Debug -> logger.debug(message?.toString() ?: String.EMPTY, throwable)
            LogLevel.Info -> logger.info(message?.toString() ?: String.EMPTY, throwable)
            LogLevel.Warn -> logger.warn(message?.toString() ?: String.EMPTY, throwable)
            LogLevel.Error -> logger.error(message?.toString() ?: String.EMPTY, throwable)
        }
    }
}
/**
 * Logs a message at the specified logging level if the given condition is true.
 *
 * @param condition A boolean value determining whether the message should be logged.
 * @param logger An instance of Logger used to log the message.
 * @param level The severity level at which the message will be logged.
 * @param message The content to be logged, which will be converted to a string.
 * @since 4.1.0
 */
fun logIf(condition: Boolean, logger: Logger, level: LogLevel, message: Any) {
    if (condition) {
        when (level) {
            LogLevel.Trace -> logger.trace(message.toString())
            LogLevel.Debug -> logger.debug(message.toString())
            LogLevel.Info -> logger.info(message.toString())
            LogLevel.Warn -> logger.warn(message.toString())
            LogLevel.Error -> logger.error(message.toString())
        }
    }
}
/**
 * Logs a message at a specified log level if the given condition is true, within the provided logging context.
 *
 * @param condition The condition that determines whether the message should be logged.
 * @param message The message to log if the condition evaluates to true.
 * @since 4.1.0
 */
@JvmName("logIfContext")
context(logger: Logger)
fun LogLevel.logIf(condition: Boolean, message: Any) {
    if (condition) log(logger, this, message)
}
/**
 * Logs the current object using the provided logger and log level if the specified condition is met.
 *
 * @param condition A Boolean value that determines whether the object should be logged.
 * @param logger The logger instance used to log the object.
 * @param level The severity level of the log (TRACE, DEBUG, INFO, WARN, or ERROR).
 * @return The current object after logging, if the condition is true.
 * @since 4.1.0
 */
@JvmName("logIfAnyReceiver")
fun <T> T.logIf(condition: Boolean, logger: Logger, level: LogLevel) = apply { if (condition) log(logger, level, toString()) }
/**
 * Logs the current object if the specified condition is met, and returns the object itself.
 *
 * This function enables logging based on a condition, for the severity level specified.
 * It uses the provided logger within the context to log the current object.
 *
 * @param condition Determines whether the logging should occur. If true, the object is logged.
 * @param level The severity level at which the object should be logged.
 * @return The calling object itself, allowing for method chaining.
 * @since 4.8.0
 */
@JvmName("logIfAnyReceiverContext")
context(logger: Logger)
fun <T> T.logIf(condition: Boolean, level: LogLevel) = apply { if (condition) log(logger, level, toString()) }
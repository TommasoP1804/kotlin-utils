@file:JvmName("LogUtilsKt")
@file:Since("1.0.0")
@file:Suppress("unused", "functionName")

package dev.tommasop1804.kutils

import dev.tommasop1804.kutils.annotations.Since
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import kotlin.reflect.KClass

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
enum class LogLevel(val levelInt: Int, val levelName: String) {
    /**
     * Represents the error logging level.
     *
     * This logging level is used to indicate serious issues that need immediate attention.
     * It corresponds to the integer value 40 and the string representation "ERROR".
     *
     * @since 1.0.0
     */
    ERROR(40, "ERROR"), 
    /**
     * Represents the WARN log level, commonly used to indicate potentially harmful situations
     * that may require attention but are not immediately critical.
     * 
     * @property levelInt The integer value associated with the WARN log level.
     * @property levelName The string representation of the WARN log level.
     * @since 1.0.0
     */
    WARN(30, "WARN"), 
    /**
     * Represents the INFO log level with a severity integer value of 20 and a name "INFO".
     *
     * This log level is used to indicate informational messages that highlight the progress of the application at a coarse-grained level.
     *
     * @property levelInt The integer value associated with the INFO log level, used for comparison and filtering.
     * @property levelName The string representation of the INFO log level, utilized in logging frameworks or external integrations.
     * @since 1.0.0
     */
    INFO(20, "INFO"), 
    /**
     * Represents the DEBUG log level, typically used for detailed diagnostic messages 
     * that are useful during software development and debugging.
     * 
     * @property levelInt The integer value associated with this log level (10).
     * @property levelName The name of this log level ("DEBUG").
     * @since 1.0.0
     */
    DEBUG(10, "DEBUG"), 
    /**
     * Represents the TRACE logging level with the lowest severity.
     *
     * TRACE is used to log fine-grained informational events that are primarily
     * useful for debugging and provide detailed insight into the application's behavior.
     *
     * @property levelInt The numeric representation of the TRACE level.
     * @property levelName The string representation of the TRACE level.
     * @since 1.0.0
     */
    TRACE(0, "TRACE");
    
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
    LogLevel.TRACE -> logger.trace(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.DEBUG -> logger.debug(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.INFO -> logger.info(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.WARN -> logger.warn(message?.toString() ?: String.EMPTY, throwable)
    LogLevel.ERROR -> logger.error(message?.toString() ?: String.EMPTY, throwable)
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
    LogLevel.TRACE -> logger.trace(message.toString())
    LogLevel.DEBUG -> logger.debug(message.toString())
    LogLevel.INFO -> logger.info(message.toString())
    LogLevel.WARN -> logger.warn(message.toString())
    LogLevel.ERROR -> logger.error(message.toString())
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
@JvmName("logWithContext")
context(logger: Logger)
infix fun LogLevel.log(message: Any) = log(this, message)

/**
 * Logs the string representation of the current object using the specified logger and log level.
 *
 * @param logger The logger instance used to write the log message.
 * @param level The level of the log, which determines the severity or importance of the message.
 * @since 1.0.0
 */
@JvmName("logAnyReceiver")
fun <T> T.log(logger: Logger, level: LogLevel) = apply { log(logger, level, toString()) }
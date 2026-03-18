package dev.tommasop1804.kutils.classes.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode

/**
 * Represents a transfer of money utilizing a specific payment method. Encapsulates both
 * monetary value and the payment method used for the transaction.
 *
 * This class is designed to support JSON serialization and deserialization via Jackson
 * utilizing both its internal serializers and deserializers (`Serializer`, `Deserializer`) and
 * legacy ones (`OldSerializer`, `OldDeserializer`). It also supports destructuring for easy
 * access to its components (money and method).
 *
 * @property money The monetary value involved in the transfer.
 * @property method The payment method used for the transfer.
 * @since 3.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
@JsonSerialize(using = MoneyTransfer.Companion.Serializer::class)
@JsonDeserialize(using = MoneyTransfer.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = MoneyTransfer.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = MoneyTransfer.Companion.OldDeserializer::class)
open class MoneyTransfer(val money: Money, val method: PaymentMethod) {
    /**
     * Retrieves the currency associated with the `money` field of the `MoneyTransfer` class.
     *
     * This property provides access to the currency information of the monetary value managed
     * by the `MoneyTransfer` instance. It delegates to the `currency` property of the `money` field.
     *
     * @since 3.1.0
     */
    val currency get() = money.currency
    /**
     * Provides a constant reference to the currency field within the `MoneyTransfer` object.
     *
     * This property allows for read-only access to the currency information associated with
     * the `money` property of the containing instance. The currency value remains consistent
     * and is inferred from the state of the `money` field.
     *
     * Useful in contexts where immutable access to the currency representation is required.
     *
     * @return The constant representation of the currency value from `money`.
     * @since 3.1.0
     */
    val constCurrency get() = money.constCurrency

    companion object {
        /**
         * Creates a `MoneyTransfer` instance using the specified `PaymentMethod`.
         *
         * This function enables the creation of a `MoneyTransfer` combining a monetary value
         * (`Money`) and a payment method (`PaymentMethod`).
         *
         * @param method The payment method to be used for the money transfer.
         * @since 3.1.0
         */
        infix fun Money.transferWith(method: PaymentMethod) = MoneyTransfer(this, method)

        class Serializer : ValueSerializer<MoneyTransfer>() {
            override fun serialize(
                value: MoneyTransfer,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writeStringProperty("money", value.money.toString())
                gen.writePOJOProperty("method", value.method)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<MoneyTransfer>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): MoneyTransfer {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                val money = Money.parse(node.get("money").asString())()
                return MoneyTransfer(money, PaymentMethod.tryDeserialize(node.get("method")))
            }
        }

        class OldSerializer : JsonSerializer<MoneyTransfer>() {
            override fun serialize(value: MoneyTransfer, gen: JsonGenerator, serializers: SerializerProvider?) {
                gen.writeStartObject()
                gen.writeStringField("money", value.money.toString())
                gen.writeObjectField("method", value.method)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<MoneyTransfer>() {
            override fun deserialize(
                p: JsonParser,
                ctxt: com.fasterxml.jackson.databind.DeserializationContext?
            ): MoneyTransfer {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                val money = Money.parse(node.get("money").asText())()
                return MoneyTransfer(money, PaymentMethod.tryDeserialize(node.get("method")))
            }
        }
    }

    /**
     * Provides the first destructured component of the MoneyTransfer object.
     * This typically corresponds to the monetary value managed by the class.
     *
     * Used in scenarios where instances of MoneyTransfer are deconstructed
     * into their individual parts.
     *
     * @return The monetary value represented by the money field.
     * @since 3.1.0
     */
    operator fun component1() = money

    /**
     * Component function that provides the value of the `method` property when the instance of the class
     * is destructured. Commonly used in destructuring declarations for retrieving the second component of
     * the instance.
     *
     * @return The value of the `method` property.
     * @since 3.1.0
     */
    operator fun component2() = method

    /**
     * Compares this `MoneyTransfer` instance to another object to determine equality.
     *
     * Checks whether the given object is also of type `MoneyTransfer` and whether its
     * `money` and `method` properties are equal to those of this instance.
     *
     * @param other The object to compare with this instance for equality.
     * @return `true` if the given object is a `MoneyTransfer` instance with the same
     * `money` and `method` values as this instance, otherwise `false`.
     * @since 3.1.0
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MoneyTransfer

        if (money != other.money) return false
        if (method != other.method) return false

        return true
    }

    /**
     * Computes the hash code for the MoneyTransfer object. The hash code is calculated based on
     * the hash codes of the `money` and `method` properties. This ensures that the hash code
     * reflects the state of these significant fields of the instance.
     *
     * @return An integer representing the hash code of the MoneyTransfer object.
     * @since 3.1.0
     */
    override fun hashCode(): Int {
        var result = money.hashCode()
        result = 31 * result + method.hashCode()
        return result
    }

    /**
     * Generates a string representation of the `MoneyTransfer` object.
     *
     * The returned string includes the values of the `money` and `method` properties,
     * formatted in a way that highlights their relationship within the object.
     *
     * The `toString` implementation is primarily intended for debugging
     * and logging purposes, providing a human-readable description of
     * the `MoneyTransfer` instance.
     *
     * @return A string representation of the `MoneyTransfer` object.
     * @since 3.1.0
     */
    override fun toString() = "MoneyTransfer(money=$money, method=$method)"
}
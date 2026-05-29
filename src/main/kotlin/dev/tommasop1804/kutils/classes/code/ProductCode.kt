/*
 * Copyright © 2026 Tommaso Pastorelli (TommasoP1804) | Kotlin-Utils
 */

package dev.tommasop1804.kutils.classes.code

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.code.Ean13.Companion.isValidEan13
import dev.tommasop1804.kutils.classes.code.Ean13P2.Companion.isValidEan13P2
import dev.tommasop1804.kutils.classes.code.Ean13P5.Companion.isValidEan13P5
import dev.tommasop1804.kutils.classes.code.Ean14.Companion.isValidEan14
import dev.tommasop1804.kutils.classes.code.Ean8.Companion.isValidEan8
import dev.tommasop1804.kutils.classes.code.Ean8P2.Companion.isValidEan8P2
import dev.tommasop1804.kutils.classes.code.Ean8P5.Companion.isValidEan8P5
import dev.tommasop1804.kutils.classes.code.ProductCode.Ean.Companion.isValidEan
import dev.tommasop1804.kutils.classes.code.ProductCode.Ean.Companion.toEan
import dev.tommasop1804.kutils.classes.code.ProductCode.Upc.Companion.toUpc
import dev.tommasop1804.kutils.classes.code.UpcA.Companion.isValidUpcA
import dev.tommasop1804.kutils.classes.code.UpcE.Companion.isValidUpcE
import dev.tommasop1804.kutils.classes.geography.*
import dev.tommasop1804.kutils.classes.geography.Country.*
import dev.tommasop1804.kutils.exceptions.*
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize

/**
 * Represents a product code entity with support for different formats like EAN and UPC.
 * This interface defines the structure for handling and validating product codes
 * across various formats.
 *
 * @since 1.0.0
 */
@Suppress("unused", "functionName")
@JsonSerialize(using = ProductCode.Companion.Serializer::class)
@JsonDeserialize(using = ProductCode.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ProductCode.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ProductCode.Companion.OldDeserializer::class)
interface ProductCode {
    /**
     * Represents the unique code associated with a product.
     *
     * This code is used to uniquely identify a product within a specific system
     * or context. It is typically a string value that conforms to a specific
     * format or standard relevant to the domain it is used in.
     *
     * @since 1.0.0
     */
    val value: String

    /**
     * Represents a set of countries determined based on specific conditions.
     *
     * This variable evaluates the context or property and assigns the corresponding
     * set of countries based on predefined identification logic, primarily relying
     * on numeric ranges or equality checks.
     *
     * The ranges and conditions follow a numeric structure that identifies specific countries
     * or groups of countries, such as country codes or other identifiers. It includes special
     * cases for UPC system identification.
     *
     * The result is dynamically determined and returned as a set of country objects.
     *
     * @return A set of `Country` objects matching the evaluated conditions.
     * @since 1.0.0
     */
    val country: Set<Country>
        get() {
            if (this is Upc) return setOf(UnitedStates, Canada)
            return when {
                3(value).toInt() in 300..379 -> setOf(France)
                3(value).toInt() == 380 -> setOf(Bulgaria)
                3(value).toInt() == 383 -> setOf(Slovenia)
                3(value).toInt() == 385 -> setOf(Croatia)
                3(value).toInt() == 387 -> setOf(BosniaAndHerzegovina)
                3(value).toInt() == 389 -> setOf(Montenegro)
                3(value).toInt() == 390 -> setOf(Kosovo)
                3(value).toInt() in 400..440 -> setOf(Germany)
                3(value).toInt() in 450..459 -> setOf(Japan)
                3(value).toInt() in 460..469 -> setOf(RussianFederation)
                3(value).toInt() == 470 -> setOf(Kyrgyzstan)
                3(value).toInt() == 471 -> setOf(Taiwan)
                3(value).toInt() == 474 -> setOf(Estonia)
                3(value).toInt() == 475 -> setOf(Latvia)
                3(value).toInt() == 476 -> setOf(Azerbaijan)
                3(value).toInt() == 477 -> setOf(Lithuania)
                3(value).toInt() == 478 -> setOf(Uzbekistan)
                3(value).toInt() == 479 -> setOf(SriLanka)
                3(value).toInt() == 480 -> setOf(Philippines)
                3(value).toInt() == 481 -> setOf(Belarus)
                3(value).toInt() == 482 -> setOf(Ukraine)
                3(value).toInt() == 484 -> setOf(Moldova)
                3(value).toInt() == 485 -> setOf(Armenia)
                3(value).toInt() == 486 -> setOf(Georgia)
                3(value).toInt() == 487 -> setOf(Kazakhstan)
                3(value).toInt() == 488 -> setOf(Kyrgyzstan)
                3(value).toInt() == 489 -> setOf(HongKong)
                3(value).toInt() in 490..499 -> setOf(Japan)
                3(value).toInt() in 500..509 -> setOf(UnitedKingdom)
                3(value).toInt() in 520..521 -> setOf(Greece)
                3(value).toInt() == 528 -> setOf(Lebanon)
                3(value).toInt() == 529 -> setOf(Cyprus)
                3(value).toInt() == 530 -> setOf(Albania)
                3(value).toInt() == 531 -> setOf(NorthMacedonia)
                3(value).toInt() == 535 -> setOf(Malta)
                3(value).toInt() == 539 -> setOf(Ireland)
                3(value).toInt() in 540..549 -> setOf(Belgium, Luxembourg)
                3(value).toInt() == 560 -> setOf(Portugal)
                3(value).toInt() == 569 -> setOf(Iceland)
                3(value).toInt() in 570..579 -> setOf(Denmark)
                3(value).toInt() == 590 -> setOf(Poland)
                3(value).toInt() == 594 -> setOf(Romania)
                3(value).toInt() == 599 -> setOf(Hungary)
                3(value).toInt() in 600..601 -> setOf(SouthAfrica)
                3(value).toInt() == 603 -> setOf(Ghana)
                3(value).toInt() == 604 -> setOf(Senegal)
                3(value).toInt() == 608 -> setOf(Bahrain)
                3(value).toInt() == 609 -> setOf(Mauritius)
                3(value).toInt() == 611 -> setOf(Morocco)
                3(value).toInt() == 613 -> setOf(Algeria)
                3(value).toInt() == 615 -> setOf(Nigeria)
                3(value).toInt() == 616 -> setOf(Kenya)
                3(value).toInt() == 618 -> setOf(CoteDIvorie)
                3(value).toInt() == 619 -> setOf(Tunisia)
                3(value).toInt() == 620 -> setOf(Tanzania)
                3(value).toInt() == 621 -> setOf(Syria)
                3(value).toInt() == 622 -> setOf(Egypt)
                3(value).toInt() == 623 -> setOf(Brunei)
                3(value).toInt() == 624 -> setOf(Libya)
                3(value).toInt() == 625 -> setOf(Jordan)
                3(value).toInt() == 626 -> setOf(Iran)
                3(value).toInt() == 627 -> setOf(Kuwait)
                3(value).toInt() == 628 -> setOf(SaudiArabia)
                3(value).toInt() == 629 -> setOf(UnitedArabEmirates)
                3(value).toInt() in 640..649 -> setOf(Finland)
                3(value).toInt() in 690..699 -> setOf(China)
                3(value).toInt() in 700..709 -> setOf(Norway)
                3(value).toInt() == 729 -> setOf(Israel)
                3(value).toInt() in 730..739 -> setOf(Sweden)
                3(value).toInt() == 740 -> setOf(Guatemala)
                3(value).toInt() == 741 -> setOf(ElSalvador)
                3(value).toInt() == 742 -> setOf(Honduras)
                3(value).toInt() == 743 -> setOf(Nicaragua)
                3(value).toInt() == 744 -> setOf(CostaRica)
                3(value).toInt() == 745 -> setOf(Panama)
                3(value).toInt() == 746 -> setOf(DominicanRepublic)
                3(value).toInt() == 750 -> setOf(Mexico)
                3(value).toInt() in 754..755 -> setOf(Canada)
                3(value).toInt() == 759 -> setOf(Venezuela)
                3(value).toInt() in 760..769 -> setOf(Switzerland)
                3(value).toInt() in 770..771 -> setOf(Colombia)
                3(value).toInt() == 773 -> setOf(Uruguay)
                3(value).toInt() == 775 -> setOf(Peru)
                3(value).toInt() == 777 -> setOf(Bolivia)
                3(value).toInt() in 778..778 -> setOf(Argentina)
                3(value).toInt() == 780 -> setOf(Chile)
                3(value).toInt() == 784 -> setOf(Paraguay)
                3(value).toInt() == 786 -> setOf(Ecuador)
                3(value).toInt() in 789..790 -> setOf(Brazil)
                3(value).toInt() in 800..839 -> setOf(Italy)
                3(value).toInt() in 840..849 -> setOf(Spain)
                3(value).toInt() == 850 -> setOf(Cuba)
                3(value).toInt() == 858 -> setOf(Slovakia)
                3(value).toInt() == 859 -> setOf(CzechRepublic)
                3(value).toInt() == 860 -> setOf(Serbia)
                3(value).toInt() == 865 -> setOf(Mongolia)
                3(value).toInt() == 867 -> setOf(NorthKorea)
                3(value).toInt() in 868..869 -> setOf(Turkey)
                3(value).toInt() in 870..879 -> setOf(Netherlands)
                3(value).toInt() == 880 -> setOf(SouthKorea)
                3(value).toInt() == 884 -> setOf(Cambodia)
                3(value).toInt() == 885 -> setOf(Thailand)
                3(value).toInt() == 888 -> setOf(Singapore)
                3(value).toInt() == 890 -> setOf(India)
                3(value).toInt() == 893 -> setOf(Vietnam)
                3(value).toInt() == 894 -> setOf(Bangladesh)
                3(value).toInt() == 896 -> setOf(Pakistan)
                3(value).toInt() == 899 -> setOf(Indonesia)
                3(value).toInt() in 900..919 -> setOf(Austria)
                3(value).toInt() in 930..939 -> setOf(Australia)
                3(value).toInt() in 940..949 -> setOf(NewZealand)
                3(value).toInt() == 955 -> setOf(Malaysia)
                3(value).toInt() == 958 -> setOf(Macao)
                else -> emptySet()
            }
        }

    companion object {
        class Serializer : ValueSerializer<ProductCode>() {
            override fun serialize(
                value: ProductCode,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeString(value.value)
            }
        }

        class Deserializer : ValueDeserializer<ProductCode>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = ProductCode(p.string)
        }

        class OldSerializer : JsonSerializer<ProductCode>() {
            override fun serialize(value: ProductCode, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.value)
        }

        class OldDeserializer : JsonDeserializer<ProductCode>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ProductCode = ProductCode(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<ProductCode?, String?> {
            override fun convertToDatabaseColumn(attribute: ProductCode?): String? = attribute?.value
            override fun convertToEntityAttribute(dbData: String?): ProductCode? = dbData?.let { ProductCode(it) }
        }
    }

    /**
     * Represents the EAN (European Article Number) code interface, which serves as a base
     * for validating and handling different types of EAN codes such as EAN-8, EAN-13, EAN-14, and their extensions.
     * EAN is a barcoding standard used for product identification.
     *
     * The interface provides utility methods for format validation and control digit computation.
     *
     * @since 3.0.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = Ean.Companion.Serializer::class)
    @JsonDeserialize(using = Ean.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Ean.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Ean.Companion.OldDeserializer::class)
    interface Ean : ProductCode {
        companion object {
            /**
             * Determines whether the string represents a valid EAN code by checking against various EAN standards.
             *
             * This method validates the string by sequentially checking its conformity with the following EAN formats:
             * - EAN-13
             * - EAN-8
             * - EAN-13 with a 5-digit add-on (EAN-13 P5)
             * - EAN-13 with a 2-digit add-on (EAN-13 P2)
             * - EAN-8 with a 2-digit add-on (EAN-8 P2)
             * - EAN-8 with a 5-digit add-on (EAN-8 P5)
             * - EAN-14
             *
             * The string passes validation if it conforms to at least one of these standards.
             *
             * @receiver The string to evaluate as a potential EAN barcode.
             * @return `true` if the string is valid according to any of the specified EAN formats, `false` otherwise.
             * @since 3.0.0
             */
            fun CharSequence.isValidEan() = isValidEan13() ||
                    isValidEan8() ||
                    isValidEan13P5() ||
                    isValidEan13P2() ||
                    isValidEan8P2() ||
                    isValidEan8P5() ||
                    isValidEan14()

            /**
             * Converts the string into an `EAN` object by validating its format and determining the appropriate EAN standard.
             *
             * The method validates the input string to ensure it matches supported EAN formats
             * (e.g., EAN-13, EAN-8, EAN-13+5, etc.) and computes the corresponding EAN object.
             * Throws a `NoMatchingFormatException` if the input does not match any recognized EAN format.
             *
             * @receiver The string to be converted to an EAN object.
             * @return A `Result` containing the corresponding `EAN` object if the conversion is successful,
             *         or an exception if the string does not match a valid EAN format.
             * @since 3.0.0
             */
            fun CharSequence.toEan(): Result<Ean> = runCatching {
                validateInputFormat(Ean::class) { matches(Regex("[0-9 ]+")) && length in 8..19 }

                when {
                    isValidEan13() -> Ean13(this)
                    isValidEan8() -> Ean8(this)
                    isValidEan13P5() -> Ean13P5(this)
                    isValidEan13P2() -> Ean13P2(this)
                    isValidEan8P2() -> Ean8P2(this)
                    isValidEan8P5() -> Ean8P5(this)
                    isValidEan14() -> Ean14(this)
                    else -> throw NoMatchingFormatException("No valid EAN format found.")
                }
            }

            class Serializer : ValueSerializer<Ean>() {
                override fun serialize(value: Ean, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeString(value.value)
                }
            }

            class Deserializer : ValueDeserializer<Ean>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = p.string.toEan()()
            }

            class OldSerializer : JsonSerializer<Ean>() {
                override fun serialize(value: Ean, gen: JsonGenerator, serializers: SerializerProvider) =
                    gen.writeString(value.value)
            }

            class OldDeserializer : JsonDeserializer<Ean>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Ean = p.text.toEan()()
            }

            @jakarta.persistence.Converter(autoApply = true)
            class Converter : AttributeConverter<Ean?, String?> {
                override fun convertToDatabaseColumn(attribute: Ean?): String? = attribute?.value
                override fun convertToEntityAttribute(dbData: String?): Ean? = dbData?.let { it.toEan()() }
            }
        }
    }
    /**
     * Represents a Universal Product Code (UPC), a standardized barcode system for product identification.
     * This interface serves as a common type for all UPC-related implementations and functionality.
     * @since 3.0.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = Upc.Companion.Serializer::class)
    @JsonDeserialize(using = Upc.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Upc.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Upc.Companion.OldDeserializer::class)
    interface Upc : ProductCode {
        companion object {
            /**
             * Determines whether the string is a valid UPC code, supporting both UPC-A and UPC-E formats.
             *
             * This function checks if the string is either a valid UPC-A or a valid UPC-E code
             * by invoking the respective validation methods `isValidUPC_A` and `isValidUPC_E`.
             *
             * @receiver The string to be validated as a UPC code.
             * @return `true` if the string is a valid UPC-A or UPC-E code, `false` otherwise.
             * @since 3.0.0
             */
            fun CharSequence.isValidUpc() = isValidUpcA() || isValidUpcE()

            /**
             * Converts the current string into a `UPC` instance, representing either `UPC_A` or `UPC_E` formats,
             * based on input format validation and matching criteria.
             *
             * The function first validates the input format, ensuring it consists only of numeric digits
             * and contains either 7 or 12 characters. If the format is valid, it further determines
             * if the string matches the criteria for either `UPC_A` or `UPC_E` and constructs the appropriate instance.
             * If no format matches, a `NoMatchingFormatException` is thrown.
             *
             * @receiver The string to be converted into a `UPC` instance.
             * @return A `Result` wrapping a `UPC` instance if conversion is successful, or an error if validation or format matching fails.
             * @since 3.0.0
             */
            fun CharSequence.toUpc(): Result<Upc> = runCatching {
                validateInputFormat(Upc::class) { matches(Regex("[0-9]+")) && length in setOf(8, 12) }

                when {
                    isValidUpcA() -> UpcA(this)
                    isValidUpcE() -> UpcE(this)
                    else -> throw NoMatchingFormatException("No valid UPC format found.")
                }
            }

            /**
             * Computes the check digit for the provided numeric code based on a weighted sum algorithm.
             *
             * The check digit is computed by iterating through the digits of the provided code and applying
             * a weight of 3 to digits at even indexes (0-based) and a weight of 1 to digits at odd indexes.
             * The sum of these weighted values is divided by 10, and the check digit is determined such that
             * the entire number (including the check digit) is divisible by 10.
             *
             * @param code the numeric string for which the check digit should be computed. Assumes the input
             *             consists only of numeric characters.
             * @return the computed check digit as a character.
             * @since 3.0.0
             */
            fun computeCheckDigit(code: String): Char {
                val value = code.mapIndexed { index, ch -> if (index.isEven) ch.digitToInt() * 3 else ch.digitToInt() }.sum()
                if (value % 10 == 0) return '0'
                return (10 - (value % 10)).digitToChar()
            }

            class Serializer : ValueSerializer<Upc>() {
                override fun serialize(value: Upc, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeString(value.value)
                }
            }

            class Deserializer : ValueDeserializer<Upc>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = p.string.toUpc()()
            }

            class OldSerializer : JsonSerializer<Upc>() {
                override fun serialize(value: Upc, gen: JsonGenerator, serializers: SerializerProvider) =
                    gen.writeString(value.value)
            }

            class OldDeserializer : JsonDeserializer<Upc>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Upc = p.text.toUpc()()
            }

            @jakarta.persistence.Converter(autoApply = true)
            class Converter : AttributeConverter<Upc?, String?> {
                override fun convertToDatabaseColumn(attribute: Upc?): String? = attribute?.value
                override fun convertToEntityAttribute(dbData: String?): Upc? = dbData?.let { it.toUpc()() }
            }
        }
    }
}

/**
 * Determines the appropriate product code format (EAN or UPC) based on the input string
 * and converts the string to the corresponding format.
 *
 * If the input string represents a valid EAN format, it is converted to an EAN instance;
 * otherwise, it is converted to a UPC instance. The conversion process uses `toEAN` for EAN-format processing
 * and `toUPC` for UPC-format processing. If neither conversion is valid, an exception is thrown.
 *
 * @param code The input string to be evaluated and converted. The string must conform to either
 *             a valid EAN or UPC code format. Examples of valid formats include:
 *             - EAN-13, EAN-8, EAN-13 with P5/P2 add-ons, EAN-8 with P5/P2 add-ons, and EAN-14.
 *             - UPC-A and UPC-E formats.
 * @return The resulting product code object, either as EAN or UPC, based on the format of the input string.
 *         Throws an exception if the input string does not conform to any valid format.
 * @since 1.0.0
 */
fun ProductCode(code: String) = if (code.isValidEan()) code.toEan()() else code.toUpc()()
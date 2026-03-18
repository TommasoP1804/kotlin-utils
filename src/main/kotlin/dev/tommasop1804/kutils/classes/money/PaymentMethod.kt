package dev.tommasop1804.kutils.classes.money

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.registry.*
import dev.tommasop1804.kutils.classes.registry.Contact.*
import dev.tommasop1804.kutils.exceptions.*
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.math.BigDecimal
import java.time.YearMonth
import java.util.Currency

/**
 * Represents a payment method interface to standardize various types of payment methods.
 * Each implementation specifies its unique properties and behaviors.
 * Common types include cards, bank transfers, digital wallets, and cash.
 *
 * This interface provides properties to identify the payment method type and an abstract
 * property for the display name.
 *
 * @since 3.1.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
sealed interface PaymentMethod {
    /**
     * Represents the display name of an entity, such as a user, product, or any identifiable item.
     *
     * This value is typically used for display purposes in user interfaces or outputs where a 
     * human-readable name or label is required.
     *
     * The `displayName` may contain alphabetic characters, numbers, and permitted special characters,
     * depending on the context in which it is being used. Validation rules, if any, will depend on 
     * system specifications and requirements.
     *
     * @since 3.1.0
     */
    val displayName: String

    /**
     * A read-only property that checks whether the current object is an instance of the `Card` class.
     *
     * @return `true` if the object is an instance of `Card`, `false` otherwise.
     * @since 3.1.0
     */
    val isCard get() = this is Card
    /**
     * Indicates whether the current object is an instance of the `BankTransfer` class.
     *
     * This property is used to determine if the underlying object represents 
     * a bank transfer transaction.
     *
     * @return `true` if the object is an instance of `BankTransfer`, otherwise `false`.
     * @since 3.1.0
     */
    val isBankTransfer get() = this is BankTransfer
    /**
     * Checks if the current instance is of type `DigitalWallet`.
     *
     * This property provides a boolean value indicating if the object 
     * is classified as a `DigitalWallet` based on its runtime type.
     *
     * @return `true` if the instance is a `DigitalWallet`, otherwise `false`.
     * @since 3.1.0
     */
    val isDigitalWallet get() = this is DigitalWallet
    /**
     * A property that indicates whether the current object is of type `Cash`.
     *
     * This property is typically used to check if the object represents a cash-related entity.
     *
     * @receiver The instance being checked.
     * @return `true` if the object is of type `Cash`; otherwise, `false`.
     * @since 3.1.0
     */
    val isCash get() = this is Cash

    @Suppress("UNCHECKED_CAST")
    companion object {
        internal fun <T : PaymentMethod> tryDeserialize(jsonNode: JsonNode) = when {
            jsonNode.get("panHash").isNotNull() -> Card(
                panHash = jsonNode.get("panHash").asString(),
                last4Digits = jsonNode.get("last4Digits").asString(),
                expiry = YearMonth(jsonNode.get("expiry").asString())(),
                cvvHash = jsonNode.get("cvvHash").asString(),
                holderName = jsonNode.get("holderName").asString(),
                issuer = jsonNode.get("issuer")?.let { Card.Issuer.ofName(it.asString()) }
            ) as T
            jsonNode.get("iban").isNotNull() -> BankTransfer(
                iban = jsonNode.get("iban").asString().let(::Iban),
                bic = jsonNode.get("bic")?.asString()?.let(::Bic),
                bankName = jsonNode.get("bankName")?.asString(),
                holderName = jsonNode.get("holderName")?.asString(),
            ) as T
            jsonNode.get("provider").isNotNull() -> DigitalWallet(
                provider = jsonNode.get("provider").asString().let { DigitalWallet.Provider.ofName(it) },
                email = jsonNode.get("email").asString()?.let(::Email),
            ) as T
            jsonNode.get("currency").isNotNull() -> Cash(
                maxAmount = jsonNode.get("maxAmount")?.asDouble()?.toBigDecimal(),
                currency = jsonNode.get("currency").asString().let { Currency.getInstance(it) },
            ) as T
            else -> throw IllegalArgumentException()
        }

        internal fun <T : PaymentMethod> tryDeserialize(jsonNode: com.fasterxml.jackson.databind.JsonNode) = when {
            jsonNode.get("panHash").isNotNull() -> Card(
                panHash = jsonNode.get("panHash").asText(),
                last4Digits = jsonNode.get("last4Digits").asText(),
                expiry = YearMonth(jsonNode.get("expiry").asText())(),
                cvvHash = jsonNode.get("cvvHash").asText(),
                holderName = jsonNode.get("holderName").asText(),
                issuer = jsonNode.get("issuer")?.let { Card.Issuer.ofName(it.asText()) }
            ) as T
            jsonNode.get("iban").isNotNull() -> BankTransfer(
                iban = jsonNode.get("iban").asText().let(::Iban),
                bic = jsonNode.get("bic")?.asText()?.let(::Bic),
                bankName = jsonNode.get("bankName")?.asText(),
                holderName = jsonNode.get("holderName")?.asText(),
            ) as T
            jsonNode.get("provider").isNotNull() -> DigitalWallet(
                provider = jsonNode.get("provider").asText().let { DigitalWallet.Provider.ofName(it) },
                email = jsonNode.get("email").asText()?.let(::Email),
            ) as T
            jsonNode.get("currency").isNotNull() -> Cash(
                maxAmount = jsonNode.get("maxAmount")?.asDouble()?.toBigDecimal(),
                currency = jsonNode.get("currency").asText().let { Currency.getInstance(it) },
            ) as T
            else -> throw IllegalArgumentException()
        }
    }

    /**
     * Represents a payment card with attributes for PAN (Primary Account Number),
     * expiration date, security code, and cardholder's name. Provides utilities
     * for accessing card details like issuer recognition, last 4 digits of the PAN, 
     * and expiration status.
     *
     * The class supports serialization and deserialization via custom Jackson serializers 
     * and deserializers defined as inner classes.
     *
     * @property last4Digits The last 4 digits of the card's PAN.
     * @property expiry The expiration date of the card as a `YearMonth`.
     * @property holderName The name of the cardholder.
     * @property issuer The issuer of the card, if available.
     * 
     * @since 3.1.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = Card.Companion.Serializer::class)
    @JsonDeserialize(using = Card.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Card.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Card.Companion.OldDeserializer::class)
    class Card internal constructor(
        val last4Digits: String,
        val expiry: YearMonth,
        private val cvvHash: String,
        val holderName: String,
        val issuer: Issuer?,
        private val panHash: String
    ) : PaymentMethod {
        /**
         * A human-readable representation of the card's display name.
         *
         * Combines the card issuer (if available) and the last four digits of the card's PAN
         * for clear identification. If the issuer is not present, only the masked PAN is displayed.
         *
         * Example format:
         * - If the issuer is present: "IssuerName •••• 1234"
         * - If the issuer is absent: "•••• 1234"
         *
         * The display name is derived dynamically based on the card's `pan` and `last4Digits`.
         *
         * @since 3.1.0
         */
        override val displayName: String get() = (if (issuer.isNotNull()) "${issuer.displayName} " else String.EMPTY) + "•••• $last4Digits"

        /**
         * Indicates whether the card is expired.
         *
         * This property determines the expiration status of the card by comparing 
         * its `expiry` date with the current year and month. If the `expiry` date 
         * is earlier than the current year and month, the card is considered expired.
         *
         * @return `true` if the card is expired, otherwise `false`.
         * @since 3.1.0
         */
        val isExpired: Boolean get() = expiry.isBefore(YearMonth())

        /**
         * Constructs a Card object using the provided Primary Account Number (PAN),
         * expiry date, CVV, holder's name, and issuer information.
         *
         * The PAN is normalized and only the last 4 digits are stored.
         *
         * @param pan The normalized Primary Account Number associated with the card.
         * @param expiry The expiry date of the card as a `YearMonth`.
         * @param cvv The security code (CVV) of the card.
         * @param holderName The name of the cardholder.
         * @since 3.1.0
         */
        constructor(pan: Pan, expiry: YearMonth, cvv: String, holderName: String) : this(
            pan.normalized.takeLast(4),
            expiry,
            cvv hashingToString HashingAlgorithm.BLAKE3_256,
            holderName,
            pan.issuer,
            pan.value hashingToString HashingAlgorithm.BLAKE3_256
        ) {
            cvv.isNumeric || throw MalformedInputException("Security code must be numeric")
        }

        companion object {
            private data class CardRule(
                val pattern: Regex,
                val lengths: IntRange
            )

            class Serializer : ValueSerializer<Card>() {
                override fun serialize(value: Card, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    gen.writeStringProperty("panHash", value.panHash)
                    gen.writeStringProperty("last4Digits", value.last4Digits)
                    gen.writeStringProperty("expiry", value.expiry.toString())
                    gen.writeStringProperty("cvvHash", value.cvvHash)
                    gen.writeStringProperty("holderName", value.holderName)
                    if (value.issuer.isNotNull()) gen.writeStringProperty("issuer", value.issuer.displayName)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<Card>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Card {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    return Card(
                        panHash = node.get("panHash").asString(),
                        last4Digits = node.get("last4Digits").asString(),
                        expiry = YearMonth(node.get("expiry").asString())(),
                        cvvHash = node.get("cvvHash").asString(),
                        holderName = node.get("holderName").asString(),
                        issuer = node.get("issuer")?.let { Issuer.ofName(it.asString()) }
                    )
                }
            }

            class OldSerializer : JsonSerializer<Card>() {
                override fun serialize(value: Card, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("panHash", value.panHash)
                    gen.writeStringField("last4Digits", value.last4Digits)
                    gen.writeStringField("expiry", value.expiry.toString())
                    gen.writeStringField("cvvHash", value.cvvHash)
                    gen.writeStringField("holderName", value.holderName)
                    gen.writeStringField("issuer", value.issuer?.displayName)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Card>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Card {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    return Card(
                        panHash = node.get("panHash").asText(),
                        last4Digits = node.get("last4Digits").asText(),
                        expiry = YearMonth(node.get("expiry").asText())(),
                        cvvHash = node.get("cvvHash").asText(),
                        holderName = node.get("holderName").asText(),
                        issuer = node.get("issuer")?.let { Issuer.ofName(it.asText()) }
                    )
                }
            }
        }

        /**
         * Compares the provided PAN (Primary Account Number) with this instance's normalized PAN
         * after removing all occurrences of the SPACE character. This method checks for equivalence 
         * after normalization.
         *
         * @param pan the PAN to compare with this instance's normalized PAN.
         * @since 3.1.0
         */
        fun samePan(pan: String) = Pan(pan).value.hashingCompare(HashingAlgorithm.BLAKE3_256, panHash)
        /**
         * Compares the given `Pan` object with the current card's `pan` field for equality.
         *
         * @param pan The `Pan` object to be compared with the current card's `pan`.
         * @return `true` if the provided `pan` matches the current card's `pan`; otherwise, `false`.
         * @since 3.1.0
         */
        fun samePan(pan: Pan) = pan.value.hashingCompare(HashingAlgorithm.BLAKE3_256, panHash)
        /**
         * Compares the PAN (Primary Account Number) of this card with another card's PAN.
         *
         * @param other The card whose PAN is to be compared with this card's PAN.
         * @return True if the PAN of both cards matches, false otherwise.
         * @since 3.1.0
         */
        fun samePan(other: Card) = other.panHash == panHash

        
        /*
         * Compares the provided security code with this card's `securityCode` field for equality.
         *
         * @param code The security code to compare with this card's `securityCode`.
         * @return `true` if the provided code matches the current card's `securityCode`; otherwise, `false`.
         * @since 3.1.0
         */
        fun sameCvv(code: String) = code.hashingCompare(HashingAlgorithm.BLAKE3_256, cvvHash)
        /**
         * Compares the security code of this card with the security code of another card.
         *
         * @param other The card whose security code is to be compared with this card's security code.
         * @return `true` if the security codes of both cards match, `false` otherwise.
         * @since 3.1.0
         */
        fun sameCvv(other: Card) = cvvHash == other.cvvHash

        /**
         * Returns a string representation of the card.
         *
         * The string representation typically contains the value of `displayName`.
         *
         * @return A string that represents this card instance.
         * @since 3.1.0
         */
        override fun toString() = displayName
        
        /**
         * Checks whether this instance is equal to another object.
         *
         * Two `Card` instances are considered equal if all of their corresponding fields match:
         * `last4Digits`, `expiry`, `cvvHash`, `holderName`, `issuer`, and `panHash`.
         *
         * @param other The object to compare with this instance.
         * @return `true` if the given object is equal to this instance, otherwise `false`.
         * @since 3.1.0
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Card

            if (last4Digits != other.last4Digits) return false
            if (expiry != other.expiry) return false
            if (cvvHash != other.cvvHash) return false
            if (holderName != other.holderName) return false
            if (issuer != other.issuer) return false
            if (panHash != other.panHash) return false

            return true
        }

        /**
         * Computes and returns the hash code for this `Card` instance.
         *
         * The hash code is computed based on the values of key fields in the class, including
         * `last4Digits`, `expiry`, `cvvHash`, `holderName`, `issuer`, and `panHash`.
         *
         * This method ensures consistency with the `equals` method, such that two objects
         * considered equal will have the same hash code.
         *
         * @return The hash code value for this `Card` instance.
         * @since 3.1.0
         */
        override fun hashCode(): Int {
            var result = last4Digits.hashCode()
            result = 31 * result + expiry.hashCode()
            result = 31 * result + cvvHash.hashCode()
            result = 31 * result + holderName.hashCode()
            result = 31 * result + (issuer?.hashCode() ?: 0)
            result = 31 * result + panHash.hashCode()
            return result
        }

        /**
         * Represents a card issuer, such as Visa, MasterCard, or American Express. An issuer is 
         * identified by its display name and associated with specific validation rules 
         * including a pattern and allowed length ranges for card numbers.
         *
         * Each issuer may define its own distinct patterns and lengths for PAN (Primary Account Number) validation.
         *
         * This enum also includes serialization and deserialization mechanisms for converting 
         * between the `Issuer` instance and its representation as a string in JSON.
         *
         * @param displayName The human-friendly name of the issuer.
         * @since 3.1.0
         */
        @JsonSerialize(using = Issuer.Companion.Serializer::class)
        @JsonDeserialize(using = Issuer.Companion.Deserializer::class)
        @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Issuer.Companion.OldSerializer::class)
        @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Issuer.Companion.OldDeserializer::class)
        enum class Issuer(val displayName: String, private val rule: CardRule) {
            AMERICAN_EXPRESS("American Express", CardRule(Regex("^3[47][0-9]{13}$"), 15..15)),
            VISA("Visa", CardRule(Regex("^4[0-9]{12}(?:[0-9]{3})?(?:[0-9]{3})?$"), 13..19)),
            MASTERCARD("Mastercard", CardRule(Regex("^(5[1-5][0-9]{14}|2(2[2-9][0-9]{12}|[3-6][0-9]{13}|7[01][0-9]{12}|720[0-9]{12}))$"), 16..16)),
            DISCOVER("Discover", CardRule(Regex("^(6011[0-9]{12}|64[4-9][0-9]{13}|65[0-9]{14}|622(12[6-9]|1[3-9][0-9]|[2-8][0-9]{2}|9[01][0-9]|92[0-5])[0-9]{10})$"), 16..19)),
            UNIONPAY("UnionPay", CardRule(Regex("^62[0-9]{14,17}$"), 16..19)),
            CHINA_UNIONPAY("China UnionPay", CardRule(Regex("^31[0-9]{17}$"), 19..19)),
            JCB("JCB", CardRule(Regex("^35(2[89]|[3-8][0-9])[0-9]{12,15}$"), 16..19)),
            DINERS_CLUB("Diners Club", CardRule(Regex("^3(0[0-5]|[68][0-9])[0-9]{11,16}$"), 14..19)),
            DINERS_CLUB_US_CA("Diners Club US/CA", CardRule(Regex("^55[0-9]{14}$"), 16..16)),
            MAESTRO("Maestro", CardRule(Regex("^(5018|5020|5038|5893|6304|6759|676[1-3])[0-9]{8,15}$"), 12..19)),
            MAESTRO_UK("Maestro UK", CardRule(Regex("^(6759|676770|676774)[0-9]{8,15}$"), 12..19)),
            MIR("Mir", CardRule(Regex("^220[0-4][0-9]{12,15}$"), 16..19)),
            TROY("Troy", CardRule(Regex("^(65[0-9]{14}|9792[0-9]{12})$"), 16..16)),
            UATP("UATP", CardRule(Regex("^1[0-9]{14}$"), 15..15)),
            RUPAY("RuPay", CardRule(Regex("^(60|65|81|82|508)[0-9]{13}$"), 16..16)),
            RUPAY_JCB("RuPay-JCB", CardRule(Regex("^35([36])[0-9]{13}$"), 16..16)),
            INTERPAYMENT("Interpayment", CardRule(Regex("^636[0-9]{13,16}$"), 16..19)),
            INSTAPAYMENT("Instapayment", CardRule(Regex("^63[7-9][0-9]{13}$"), 16..16)),
            DANKORT("Dankort", CardRule(Regex("^5019[0-9]{12}$"), 16..16)),
            DANKORT_VISA("Dankort-Visa", CardRule(Regex("^4571[0-9]{12}$"), 16..16)),
            BORICA("Borica", CardRule(Regex("^2205[0-9]{12}$"), 16..16)),
            UZCARD("Uzcard", CardRule(Regex("^(8600|5614)[0-9]{12}$"), 16..16)),
            HUMO("HUMO", CardRule(Regex("^9860[0-9]{12}$"), 16..16)),
            VERVE("Verne", CardRule(Regex("^(5060(99|[0-8][0-9])|6500(0[2-9]|1[0-9]|2[0-7])|5078(6[5-9]|[7-9][0-9]))[0-9]{10}$"), 16..19)),
            GPN("GPN", CardRule(Regex("^(1946|50|56|58|6[0-3])[0-9]{10,17}$"), 16..19)),
            NAPAS("Napas", CardRule(Regex("^9704[0-9]{12,15}$"), 16..19)),
            @Deprecated("No more used") BANKCARD("Bankcard", CardRule(Regex("^(5610|56022[1-5])[0-9]{12}$"), 16..16)),
            @Deprecated("No more used") LASER("Laser", CardRule(Regex("^(6304|6706|6771|6709)[0-9]{12,15}$"), 16..19)),
            @Deprecated("No more used") SOLO("Solo", CardRule(Regex("^(6334|6767)[0-9]{12,15}$"), 16..19)),
            @Deprecated("No more used") SWITCH("Switch", CardRule(Regex("^(4903|4905|4911|4936|564182|633110|6333|6759)[0-9]{10,15}$"), 16..19)),
            @Deprecated("No more used") VISA_ELECTRON("Visa Electron", CardRule(Regex("4(026|17500|844|913|917)[0-9]{10,12}"), 16..16));

            companion object {
                /**
                 * Finds an entry matching the provided PAN (Primary Account Number) based on its normalized 
                 * value and length, using the rules associated with the entries of the issuer.
                 *
                 * @param pan The PAN to be checked against the issuer's rules and entries.
                 * @return The matching entry if found, or `null` if no entry matches the given PAN.
                 * @since 3.1.0
                 */
                infix fun from(pan: Pan) = entries.find {
                    it.rule.pattern.matches(pan.normalized) && pan.normalized.length in it.rule.lengths
                }

                /**
                 * Finds an entry in the collection whose display name matches the provided name, ignoring case considerations.
                 *
                 * @param name The display name to search for. Case differences are ignored during the comparison.
                 * @since 3.1.0
                 */
                infix fun ofName(name: String) = entries.find { it.displayName equalsIgnoreCase name }

                class Serializer : ValueSerializer<Issuer>() {
                    override fun serialize(value: Issuer, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                        gen.writeString(value.displayName)
                    }
                }

                class Deserializer : ValueDeserializer<Issuer>() {
                    override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Issuer.ofName(p.string)!!
                }

                class OldSerializer : JsonSerializer<Issuer>() {
                    override fun serialize(value: Issuer, gen: JsonGenerator, serializers: SerializerProvider) =
                        gen.writeString(value.displayName)
                }

                class OldDeserializer : JsonDeserializer<Issuer>() {
                    override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = Issuer.ofName(p.text)!!
                }
            }
        }
    }

    /**
     * Represents a bank transfer payment method, including details like the IBAN, BIC, bank name, and holder name.
     * Provides serialization and deserialization mechanisms for custom handling in JSON processing.
     *
     * @property iban The International Bank Account Number associated with this bank transfer.
     * @property bic The Bank Identifier Code, which may be optional.
     * @property bankName The name of the bank associated with the IBAN, if available.
     * @property holderName The name of the account holder, if available.
     * @since 3.1.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = BankTransfer.Companion.Serializer::class)
    @JsonDeserialize(using = BankTransfer.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = BankTransfer.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = BankTransfer.Companion.OldDeserializer::class)
    data class BankTransfer(
        val iban: Iban,
        val bic: Bic? = null,
        val bankName: String? = null,
        val holderName: String? = null,
    ) : PaymentMethod {
        /**
         * Represents the display name for the bank transfer.
         *
         * The `displayName` combines the bank transfer type, the bank's name (if available), 
         * and the masked IBAN, formatted for a user-friendly representation.
         *
         * The value is dynamically constructed:
         * - If the `bankName` field is not null, it is included in the display.
         * - The `maskedIban` field is always included.
         *
         * This property is primarily intended for use in contexts where a descriptive representation
         * of a bank transfer is necessary, such as UI elements or logging.
         *
         * @since 3.1.0
         */
        override val displayName: String get() = "Bank Transfer " + (if (bankName.isNotNull()) "$bankName " else String.EMPTY) + maskedIban

        /**
         * A computed property that generates a masked representation of the International Bank 
         * Account Number (IBAN). This masking hides characters in the IBAN for security, displaying 
         * only the first 4 characters, followed by a series of mask characters (`•`) and the 
         * last 4 characters of the IBAN.
         *
         * - If the IBAN length is greater than 8, the mask will be applied as described (4 characters, mask, 4 characters).
         * - If the IBAN length is 8 or less, the full IBAN value is returned without masking.
         *
         * The masking mechanism utilizes a custom operator function to handle substring extraction
         * for the first 4 characters of the IBAN.
         *
         * @return The masked representation of the IBAN or the unaltered IBAN value if the length is 8 or less.
         * @since 3.1.0
         */
        val maskedIban get() = if (iban.length > 8) "${4(iban.value)}${"•".repeat(iban.length - 8)}${iban.takeLast(4)}" else iban.value

        /**
         * Represents the country associated with the IBAN (International Bank Account Number).
         * This value retrieves the country code that corresponds to the IBAN provided.
         *
         * @return A string representing the country code according to the IBAN.
         * @since 3.1.0
         */
        val country get() = iban.country
        
        companion object {
            class Serializer : ValueSerializer<BankTransfer>() {
                override fun serialize(value: BankTransfer, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    gen.writeStringProperty("iban", value.iban.value)
                    if (value.bic.isNotNull()) gen.writeStringProperty("bic", value.bic.value)
                    if (value.bankName.isNotNull()) gen.writeStringProperty("bankName", value.bankName)
                    if (value.holderName.isNotNull()) gen.writeStringProperty("holderName", value.holderName)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<BankTransfer>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): BankTransfer {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    val bic = node.get("bic")?.asString()?.let { Bic(it) }
                    val bankName = node.get("bankName")?.asString()
                    val holderName = node.get("holderName")?.asString()
                    return BankTransfer(
                        iban = Iban(node.get("iban").asString()),
                        bic = bic,
                        bankName = bankName,
                        holderName = holderName
                    )
                }
            }

            class OldSerializer : JsonSerializer<BankTransfer>() {
                override fun serialize(value: BankTransfer, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("iban", value.iban.value)
                    if (value.bic.isNotNull()) gen.writeStringField("bic", value.bic.value)
                    if (value.bankName.isNotNull()) gen.writeStringField("bankName", value.bankName)
                    if (value.holderName.isNotNull()) gen.writeStringField("holderName", value.holderName)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<BankTransfer>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): BankTransfer {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    val bic = node.get("bic")?.asText()?.let { Bic(it) }
                    val bankName = node.get("bankName")?.asText()
                    val holderName = node.get("holderName")?.asText()
                    return BankTransfer(
                        iban = Iban(node.get("iban").asText()),
                        bic = bic,
                        bankName = bankName,
                        holderName = holderName
                    )
                }
            }
        }

        /**
         * Provides a string representation of the object.
         * 
         * This implementation returns the value of the `displayName` property, which is expected
         * to represent the transfer target or account holder's representative name in string format.
         * 
         * @return A string value representing the object.
         * @since 3.1.0
         */
        override fun toString() = displayName
        
        /**
         * Checks whether this BankTransfer instance is equal to another object.
         *
         * Performs equality checks by comparing the `iban`, `bic`, `bankName`, and `holderName`
         * properties with those of the other object.
         *
         * @param other the object to be compared with this instance
         * @return `true` if the objects are considered equal, `false` otherwise
         * @since 3.1.0
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as BankTransfer

            if (iban != other.iban) return false
            if (bic != other.bic) return false
            if (bankName != other.bankName) return false
            if (holderName != other.holderName) return false

            return true
        }

        /**
         * Computes the hash code for the BankTransfer object based on its properties.
         *
         * @return the computed hash code as an integer.
         * @since 3.1.0
         */
        override fun hashCode(): Int {
            var result = iban.hashCode()
            result = 31 * result + (bic?.hashCode() ?: 0)
            result = 31 * result + (bankName?.hashCode() ?: 0)
            result = 31 * result + (holderName?.hashCode() ?: 0)
            return result
        }
    }

    /**
     * Represents a digital wallet payment method with a specific provider and optional email.
     *
     * This class supports serialization and deserialization for various JSON processing frameworks, 
     * including Jackson and tools.jackson, using multiple serializer and deserializer implementations.
     *
     * @property provider The digital wallet provider (e.g., PayPal, Google Pay).
     * @property email The contact email associated with the digital wallet, if available.
     * @since 3.1.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = DigitalWallet.Companion.Serializer::class)
    @JsonDeserialize(using = DigitalWallet.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = DigitalWallet.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = DigitalWallet.Companion.OldDeserializer::class)
    data class DigitalWallet(
        val provider: Provider = Provider.OTHER,
        val email: Contact.Email? = null
    ) : PaymentMethod {
        /**
         * The display name of the digital wallet. Combines the provider's display name and the email
         * (if available) into a single formatted string.
         *
         * If the `email` is not null, it appends the email in parentheses to the provider's display
         * name. Otherwise, it uses only the provider's display name.
         *
         * @since 3.1.0
         */
        override val displayName: String get() = provider.displayName + if (email.isNotNull()) " ($email)" else String.EMPTY

        companion object {
            class Serializer : ValueSerializer<DigitalWallet>() {
                override fun serialize(value: DigitalWallet, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    gen.writeStringProperty("provider", value.provider.displayName)
                    if (value.email.isNotNull()) gen.writeStringProperty("email", value.email.value)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<DigitalWallet>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): DigitalWallet {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    val email = node.get("email")?.asString()?.let { Contact.Email(it) }
                    return DigitalWallet(
                        provider = Provider.ofName(node.get("provider").asString()),
                        email = email
                    )
                }
            }

            class OldSerializer : JsonSerializer<DigitalWallet>() {
                override fun serialize(value: DigitalWallet, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("provider", value.provider.displayName)
                    if (value.email.isNotNull()) gen.writeStringField("email", value.email.value)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<DigitalWallet>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): DigitalWallet {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    val email = node.get("email")?.asText()?.let { Contact.Email(it) }
                    return DigitalWallet(
                        provider = Provider.ofName(node.get("provider").asText()),
                        email = email
                    )
                }
            }
        }

        /**
         * Returns the string representation of the instance.
         * This implementation provides the `displayName` field value
         * to represent the class in its string form.
         *
         * @return The value of the `displayName` field.
         * @since 3.1.0
         */
        override fun toString() = displayName
        
        /**
         * Checks whether this `DigitalWallet` is equal to the specified object.
         *
         * @param other The object to compare with this `DigitalWallet`.
         * @return `true` if the specified object is a `DigitalWallet` with matching `provider` and `email` values, `false` otherwise.
         * @since 3.1.0
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DigitalWallet

            if (provider != other.provider) return false
            if (email != other.email) return false

            return true
        }

        /**
         * Generates a hash code for the DigitalWallet instance.
         *
         * The hash code is computed based on the `provider` and `email` fields of the object.
         *
         * @return an integer representing the hash code of the DigitalWallet instance.
         * @since 3.1.0
         */
        override fun hashCode(): Int {
            var result = provider.hashCode()
            result = 31 * result + (email?.hashCode() ?: 0)
            return result
        }


        /**
         * Represents a provider used in digital wallet services.
         *
         * This enum holds a set of well-defined payment providers along with their display names.
         * It supports JSON serialization and deserialization using both standard Jackson and compatibility layers.
         *
         * Serialization and deserialization mechanisms:
         * - `Serializer` and `Deserializer` are designed for tools.jackson.core-based implementations.
         * - `OldSerializer` and `OldDeserializer` offer compatibility with com.fasterxml.jackson.databind.
         *
         * The `ofName` function enables retrieving a provider instance based on a display name, ensuring a case-insensitive
         * search, and falls back to the `OTHER` value if no match is found.
         *
         * @property displayName The displayed name of the provider.
         * @since 3.1.0
         */
        @JsonSerialize(using = Provider.Companion.Serializer::class)
        @JsonDeserialize(using = Provider.Companion.Deserializer::class)
        @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Provider.Companion.OldSerializer::class)
        @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Provider.Companion.OldDeserializer::class)
        enum class Provider(val displayName: String) {
            PAYPAL("PayPal"),
            GOOGLE_PAY("Google Pay"),
            APPLE_PAY("Apple Pay"),
            SAMSUNG_PAY("Samsung Pay"),
            SATISPAY("Satispay"),
            AMAZON_PAY("Amazon Pay"),
            CURVE_PAY("Curve Pay"),
            STRIPE("Stripe"),
            OTHER("Other");

            companion object {
                /**
                 * Searches for an entry in the collection of `entries` that matches the specified name, 
                 * ignoring case considerations. If no match is found, it falls back to `OTHER`.
                 *
                 * @param name the name to search for, case-insensitively
                 * @return the matching entry if found; otherwise, returns `OTHER`
                 * @since 3.1.0
                 */
                infix fun ofName(name: String) = entries.find { it.displayName equalsIgnoreCase name } ?: OTHER

                class Serializer : ValueSerializer<Provider>() {
                    override fun serialize(value: Provider, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                        gen.writeString(value.displayName)
                    }
                }

                class Deserializer : ValueDeserializer<Provider>() {
                    override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Provider.ofName(p.string)
                }

                class OldSerializer : JsonSerializer<Provider>() {
                    override fun serialize(value: Provider, gen: JsonGenerator, serializers: SerializerProvider) =
                        gen.writeString(value.displayName)
                }

                class OldDeserializer : JsonDeserializer<Provider>() {
                    override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext) = Provider.ofName(p.text)
                }
            }
        }
    }

    /**
     * Represents a payment method utilizing cash. 
     * This class encapsulates details about the maximum allowable amount and the currency associated with cash payments.
     *
     * This data class is designed to be used with JSON serialization and deserialization via Jackson.
     * It includes legacy serialization support alongside the new serialization mechanisms.
     * 
     * @property maxAmount The maximum amount permitted for the cash transaction. 
     *                     If null, there is no upper limit on the cash transaction amount.
     * @property currency The currency in which the cash transaction is denominated.
     *                    Must be a valid instance of `Currency`.
     * 
     * @constructor Creates a new instance of `Cash` with an optional maximum amount and a valid currency.
     * 
     * @since 3.1.0
     * @author Tommaso Pastorelli
     */
    @JsonSerialize(using = Cash.Companion.Serializer::class)
    @JsonDeserialize(using = Cash.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Cash.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Cash.Companion.OldDeserializer::class)
    data class Cash(
        val maxAmount: BigDecimal? = null,
        val currency: Currency
    ) : PaymentMethod {
        /**
         * Represents the display name of the cash object, combining the currency and optional
         * maximum amount for better clarity.
         *
         * The `displayName` includes the currency of the cash instance. Additionally, if a 
         * maximum amount (`maxAmount`) is defined, it appends the maximum limit to the display name 
         * in parentheses, offering a concise summary of both values.
         *
         * Example output of `displayName` could look like:
         * - `Cash of USD`
         * - `Cash of EUR (max 1000)`
         *
         * The behavior relies on checking if the `maxAmount` is not null, leveraging the `isNotNull` 
         * utility function. When `maxAmount` is null, the maximum limit is excluded from the `displayName`.
         *
         * @since 3.1.0
         */
        override val displayName: String get() = "Cash of $currency" + (if (maxAmount.isNotNull()) " (max $maxAmount)" else String.EMPTY)

        /**
         * Secondary constructor for the `Cash` class.
         *
         * @param maxAmount The maximum monetary amount, which can be null.
         * @param currency The currency object used to initialize this instance.
         * 
         * @throws IllegalArgumentException if the currency code is invalid.
         * 
         * @since 3.1.0
         */
        constructor(maxAmount: BigDecimal? = null, currency: dev.tommasop1804.kutils.classes.money.Currency) :
                this(maxAmount, currency.toJavaCurrency() ?: throw IllegalArgumentException("Invalid currency code"))

        /**
         * Secondary constructor for the `Cash` class that initializes a new instance using a `Money` object.
         * It delegates to the primary constructor by extracting the amount and currency details from the `Money` instance.
         *
         * @param maxAmount An instance of `Money` specifying the maximum amount and its associated currency.
         * @since 3.1.0
         */
        constructor(maxAmount: Money) : this(maxAmount.amount, maxAmount.currency)

        companion object {
            /**
             * Creates a new instance of `Cash` using the currency from the provided `Money` instance.
             *
             * This method ignores any maximum amount restrictions when creating the `Cash` instance.
             *
             * @param amount the `Money` instance used to derive the currency for the new `Cash` object.
             * @since 3.1.0
             */
            infix fun fromAmountNoMax(amount: Money) = Cash(currency = amount.currency)

            class Serializer : ValueSerializer<Cash>() {
                override fun serialize(value: Cash, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    if (value.maxAmount.isNotNull()) gen.writeNumberProperty("maxAmount", value.maxAmount)
                    gen.writeStringProperty("currency", value.currency.currencyCode)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<Cash>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Cash {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    val maxAmount = node.get("maxAmount")?.decimalValue()
                    return Cash(
                        currency = Currency.getInstance(node.get("currency").asString()),
                        maxAmount = maxAmount
                    )
                }
            }

            class OldSerializer : JsonSerializer<Cash>() {
                override fun serialize(value: Cash, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    if (value.maxAmount.isNotNull()) gen.writeNumberField("maxAmount", value.maxAmount)
                    gen.writeStringField("currency", value.currency.currencyCode)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Cash>() {
                override fun deserialize(
                    p: JsonParser,
                    ctxt: com.fasterxml.jackson.databind.DeserializationContext
                ): Cash {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    val maxAmount = node.get("maxAmount")?.decimalValue()
                    return Cash(
                        currency = Currency.getInstance(node.get("currency").asText()),
                        maxAmount = maxAmount
                    )
                }
            }
        }

        /**
         * Returns a string representation of the object.
         *
         * The returned string is derived from the `displayName` field,
         * providing a human-readable representation for instances of this class.
         *
         * @return the string representation of the object.
         * @since 3.1.0
         */
        override fun toString() = displayName

        /**
         * Checks whether this instance is equal to another object.
         *
         * @param other the object to compare with this instance
         * @return `true` if the objects are considered equal, `false` otherwise
         * @since 3.1.0
         */
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Cash

            if (maxAmount?.let { other.maxAmount?.compareTo(it) != 0 } ?: other.maxAmount.isNull()) return false
            if (currency != other.currency) return false

            return true
        }

        /**
         * Computes the hash code for the Cash object based on its properties.
         *
         * The hash code is calculated using the optional `maxAmount` property and the mandatory 
         * `currency` property. This method ensures consistency with the contract of the `equals` method.
         *
         * @return the computed hash code as an integer.
         * @since 3.1.0
         */
        override fun hashCode(): Int {
            var result = maxAmount?.hashCode() ?: 0
            result = 31 * result + currency.hashCode()
            return result
        }
    }
}
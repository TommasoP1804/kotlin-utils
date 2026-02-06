@file:Suppress("unused", "kutils_temporal_now_as_temporal", "kutils_temporal_parse_as_temporal", "kutils_collection_declaration")

package dev.tommasop1804.kutils.classes.registry

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.geography.Country
import dev.tommasop1804.kutils.classes.registry.Contact.Email.Companion.EMAIL_REGEX
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.net.URI
import java.time.OffsetDateTime
import java.time.temporal.Temporal
import java.time.temporal.TemporalAdjuster
import kotlin.reflect.KProperty

/**
 * Represents a contact entity with various details such as name, jobs, communication methods,
 * relationships, and additional metadata.
 *
 * @property name The full name and related details of the contact.
 * @property jobs A set of jobs associated with the contact.
 * @property phoneNumbers A map of phone numbers categorized by type.
 * @property emails A map of email addresses categorized by type.
 * @property groups A set of group names the contact belongs to.
 * @property addresses A map of addresses categorized by type.
 * @property importantDates A map of specific important dates related to the contact.
 * @property relationships A map of relationships with the contact (e.g., "parent", "friend").
 * @property notes Optional notes or remarks about the contact.
 * @property websites A map of websites linked to the contact, categorized by type.
 * @property profilePicture An optional URL to the contact's profile picture.
 * @property isFavourite Indicates whether this contact is marked as a favorite.
 * @property otherFields A map of additional, dynamically defined fields.
 * @property createdAt The timestamp when the contact was created.
 * @property updatedAt The timestamp when the contact was last updated.
 * @property lastContactedAt An optional timestamp of the last time this contact was interacted with.
 *
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("kutils_null_check")
@JsonSerialize(using = Contact.Companion.Serializer::class)
@JsonDeserialize(using = Contact.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Contact.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Contact.Companion.OldDeserializer::class)
data class Contact(
    var name: Name,
    var jobs: Set<Job> = emptySet(),
    var phoneNumbers: Map<String, PhoneNumber> = emptyMap(),
    var emails: Map<String, Email> = emptyMap(),
    var groups: StringSet = emptySet(),
    var addresses: Map<String, Address> = emptyMap(),
    var importantDates: Map<String, TemporalAdjuster> = emptyMap(),
    var relationships: DataMap = emptyMap(),
    var notes: String? = null,
    var websites: Map<String, URI> = emptyMap(),
    var profilePicture: URI? = null,
    var isFavourite: Boolean = false,
    var otherFields: DataMap = emptyMap(),
    
    // METADATA
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),
    var lastContactedAt: OffsetDateTime? = null
) {
    companion object {
        class Serializer : ValueSerializer<Contact>() {
            override fun serialize(value: Contact, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeStartObject()
                gen.writePOJOProperty("name", value.name)
                gen.writePOJOProperty("jobs", value.jobs)
                gen.writePOJOProperty("phoneNumbers", value.phoneNumbers)
                gen.writePOJOProperty("emails", value.emails)
                gen.writeArrayPropertyStart("groups")
                value.groups.forEach { gen.writeString(it) }
                gen.writeEndArray()
                gen.writePOJOProperty("addresses", value.addresses)
                gen.writePOJOProperty("importantDates", value.importantDates)
                gen.writePOJOProperty("relationships", value.relationships)
                gen.writeStringProperty("notes", value.notes)
                gen.writePOJOProperty("websites", value.websites)
                gen.writeStringProperty("profilePicture", value.profilePicture?.toString())
                gen.writeBooleanProperty("isFavourite", value.isFavourite)
                gen.writePOJOProperty("otherFields", value.otherFields)
                gen.writeStringProperty("createdAt", value.createdAt.toString())
                gen.writeStringProperty("updatedAt", value.updatedAt.toString())
                gen.writeStringProperty("lastContactedAt", value.lastContactedAt?.toString())
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<Contact>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Contact {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return Contact(
                    name = node.get("name").traverse(p.objectReadContext()).readValueAs(Name::class.java),
                    jobs = node["jobs"]?.let { ctxt.readValue<Array<Job>?>(it.traverse(p.objectReadContext()), ctxt.constructType(Array<Job>::class.java))?.toSet() } ?: emptySet(),
                    phoneNumbers = node["phoneNumbers"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    emails = node["emails"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    groups = node["groups"]?.let { ctxt.readValue<Array<String>?>(it.traverse(p.objectReadContext()), ctxt.constructType(Array<String>::class.java))?.toSet() } ?: emptySet(),
                    addresses = node["addresses"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    importantDates = node["importantDates"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    relationships = node["relationships"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    notes = node["notes"]?.asString(),
                    websites = node["websites"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    profilePicture = node["profilePicture"]?.asString()?.let { URI(it)},
                    isFavourite = node["isFavourite"].asBoolean(),
                    otherFields = node["otherFields"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    createdAt = OffsetDateTime.parse(node["createdAt"].asString()),
                    updatedAt = OffsetDateTime.parse(node["updatedAt"].asString()),
                    lastContactedAt = node["lastContactedAt"]?.asString()?.let { OffsetDateTime.parse(it) }
                )
            }
        }

        class OldSerializer : JsonSerializer<Contact>() {
            override fun serialize(value: Contact, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeObjectField("name", value.name)
                gen.writeObjectField("jobs", value.jobs)
                gen.writeObjectField("phoneNumbers", value.phoneNumbers)
                gen.writeObjectField("emails", value.emails)
                gen.writeFieldName("groups")
                gen.writeStartArray()
                value.groups.forEach { gen.writeString(it) }
                gen.writeEndArray()
                gen.writeObjectField("addresses", value.addresses)
                gen.writeObjectField("importantDates", value.importantDates)
                gen.writeObjectField("relationships", value.relationships)
                gen.writeStringField("notes", value.notes)
                gen.writeObjectField("websites", value.websites)
                gen.writeStringField("profilePicture", value.profilePicture?.toString())
                gen.writeBooleanField("isFavourite", value.isFavourite)
                gen.writeObjectField("otherFields", value.otherFields)
                gen.writeStringField("createdAt", value.createdAt.toString())
                gen.writeStringField("updatedAt", value.updatedAt.toString())
                gen.writeStringField("lastContactedAt", value.lastContactedAt?.toString())
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<Contact>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Contact {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return Contact(
                    name = node.get("name").traverse(p.codec).readValueAs(Name::class.java),
                    jobs = node["jobs"]?.let { ctxt.readValue<Array<Job>?>(it.traverse(p.codec), ctxt.constructType(Array<Job>::class.java))?.toSet() } ?: emptySet(),
                    phoneNumbers = node["phoneNumbers"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    emails = node["emails"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    groups = node["groups"]?.let { ctxt.readValue<Array<String>?>(it.traverse(p.codec), ctxt.constructType(Array<String>::class.java))?.toSet() } ?: emptySet(),
                    addresses = node["addresses"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    importantDates = node["importantDates"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    relationships = node["relationships"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    notes = node["notes"]?.asText(),
                    websites = node["websites"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    profilePicture = node["profilePicture"]?.asText()?.let { URI(it)},
                    isFavourite = node["isFavourite"].asBoolean(),
                    otherFields = node["otherFields"]?.let { ctxt.readValue(it.traverse(p.codec), ctxt.constructType(Map::class.java)) } ?: emptyMap(),
                    createdAt = OffsetDateTime.parse(node["createdAt"].asText()),
                    updatedAt = OffsetDateTime.parse(node["updatedAt"].asText()),
                    lastContactedAt = node["lastContactedAt"]?.asText()?.let { OffsetDateTime.parse(it) }
                )
            }
        }
    }

    /**
     * Retrieves the value of a property dynamically through delegation.
     *
     * @param thisRef The instance of the containing object. Can be null.
     * @param property The metadata of the property being accessed.
     * @return The value of the property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R
    
    /**
     * Represents a person's name with various components and optional phonetic details.
     *
     * This class provides a comprehensive structure for managing a person's name, including prefixes, suffixes, and
     * phonetic transcriptions. The `fullName` property combines the available name components into a single string,
     * simplifying usage in contexts where a fully formatted name is required.
     *
     * The class enforces that at least one of `firstName`, `middleName`, or `lastName` must not be null, ensuring
     * that it represents a valid identification for the contact.
     *
     * @param namePrefix Optional prefix for the name (e.g., "Dr.", "Ms.").
     * @param firstName First name of the individual.
     * @param middleName Middle name of the individual.
     * @param lastName Last name of the individual.
     * @param nameSuffix Optional suffix for the name (e.g., "Jr.", "III").
     * @param phoneticFirstName Phonetic transcription of the first name.
     * @param phoneticMiddleName Phonetic transcription of the middle name.
     * @param phoneticLastName Phonetic transcription of the last name.
     * @param nickname An optional nickname or informal name for the individual.
     *
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    @JsonSerialize(using = Name.Companion.Serializer::class)
    @JsonDeserialize(using = Name.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Name.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Name.Companion.OldDeserializer::class)
    data class Name(
        var namePrefix: String? = null,
        var firstName: String? = null,
        var middleName: String? = null,
        var lastName: String? = null,
        var nameSuffix: String? = null,
        var phoneticFirstName: String? = null,
        var phoneticMiddleName: String? = null,
        var phoneticLastName: String? = null,
        var nickname: String? = null,
    ) {
        /**
         * Represents the full name constructed by combining individual name components:
         * name prefix, first name, middle name, last name, and name suffix.
         * The components are concatenated with a space as a separator,
         * and null components are excluded from the resulting string.
         *
         * @return A string representing the full name or null if no components are available.
         * @since 1.0.0
         */
        val fullName: String
            get() = listOfNotNull(
                namePrefix,
                firstName,
                middleName,
                lastName,
                nameSuffix
            ).joinToString(" ")

        init {
            validate(firstName.isNotNull() || middleName.isNotNull() || lastName.isNotNull()) { "The name must have at least one not null field" }
        }

        companion object {
            class Serializer : ValueSerializer<Name>() {
                override fun serialize(value: Name, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    gen.writeStringProperty("namePrefix", value.namePrefix)
                    gen.writeStringProperty("firstName", value.firstName)
                    gen.writeStringProperty("middleName", value.middleName)
                    gen.writeStringProperty("lastName", value.lastName)
                    gen.writeStringProperty("nameSuffix", value.nameSuffix)
                    gen.writeStringProperty("phoneticFirstName", value.phoneticFirstName)
                    gen.writeStringProperty("phoneticMiddleName", value.phoneticMiddleName)
                    gen.writeStringProperty("phoneticLastName", value.phoneticLastName)
                    gen.writeStringProperty("nickname", value.nickname)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<Name>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Name {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    return Name(
                        namePrefix = with(node["namePrefix"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        firstName = with(node["firstName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        middleName = with(node["middleName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        lastName = with(node["lastName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        nameSuffix = with(node["nameSuffix"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        phoneticFirstName = with(node["phoneticFirstName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        phoneticMiddleName = with(node["phoneticMiddleName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        phoneticLastName = with(node["phoneticLastName"]) { if (node.isNull || node.asString() == "null") null else asString() },
                        nickname = with(node["nickname"]) { if (node.isNull || node.asString() == "null") null else asString() }
                    )
                }
            }

            class OldSerializer : JsonSerializer<Name>() {
                override fun serialize(value: Name, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("namePrefix", value.namePrefix)
                    gen.writeStringField("firstName", value.firstName)
                    gen.writeStringField("middleName", value.middleName)
                    gen.writeStringField("lastName", value.lastName)
                    gen.writeStringField("nameSuffix", value.nameSuffix)
                    gen.writeStringField("phoneticFirstName", value.phoneticFirstName)
                    gen.writeStringField("phoneticMiddleName", value.phoneticMiddleName)
                    gen.writeStringField("phoneticLastName", value.phoneticLastName)
                    gen.writeStringField("nickname", value.nickname)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Name>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Name {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    return Name(
                        namePrefix = with(node["namePrefix"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        firstName = with(node["firstName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        middleName = with(node["middleName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        lastName = with(node["lastName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        nameSuffix = with(node["nameSuffix"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        phoneticFirstName = with(node["phoneticFirstName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        phoneticMiddleName = with(node["phoneticMiddleName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        phoneticLastName = with(node["phoneticLastName"]) { if (node.isNull || node.asText() == "null") null else asText() },
                        nickname = with(node["nickname"]) { if (node.isNull || node.asText() == "null") null else asText() }
                    )
                }
            }
        }

        /**
         * Provides the delegated property value by resolving its name from a reflection-based map.
         *
         * @param R The type of the value being retrieved.
         * @param thisRef The reference to the instance this property is bound to. Can be null.
         * @param property The metadata of the property being accessed.
         * @return The value associated with the property name, cast to the appropriate type.
         * @since 1.0.0
         */
        @Suppress("unchecked_cast")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

        /**
         * Returns the string representation of an instance of the class.
         * This method overrides the default `toString` implementation to return the full name.
         *
         * @return A string containing the full name of the instance.
         * @since 1.0.0
         */
        override fun toString() = fullName
    }
    /**
     * Represents a job associated with an entity. A job includes details such as the job title,
     * the department, the company, and the date since the job has been held.
     * At least one of the fields must be specified to create a valid job instance.
     *
     * @property jobTitle The title of the job. Can be null if not specified.
     * @property jobDepartment The department where the job is held. Can be null if not specified.
     * @property jobCompany The company for which the job is held. Can be null if not specified.
     * @property since The date since the job has been held. Can be null if not specified.
     * @throws IllegalArgumentException If none of the fields (jobTitle, jobDepartment, or jobCompany) are provided.
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    @JsonSerialize(using = Job.Companion.Serializer::class)
    @JsonDeserialize(using = Job.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Job.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Job.Companion.OldDeserializer::class)
    data class Job(
        var jobTitle: String? = null,
        var jobDepartment: String? = null,
        var jobCompany: String? = null,
        var since: Temporal? = null
    ) {
        init {
            validate(jobTitle.isNotNull() || jobDepartment.isNotNull() || jobCompany.isNotNull()) {
                "Job must have at least one field specified"
            }
        }

        companion object {
            class Serializer : ValueSerializer<Job>() {
                override fun serialize(value: Job, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                    gen.writeStartObject()
                    gen.writeStringProperty("jobTitle", value.jobTitle)
                    gen.writeStringProperty("jobDepartment", value.jobDepartment)
                    gen.writeStringProperty("jobCompany", value.jobCompany)
                    gen.writePOJOProperty("since", value.since.toString())
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<Job>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Job {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    return Job(
                        jobTitle = with(node["jobTitle"]) {if (this == null || isNull) null else asString() },
                        jobDepartment = with(node["jobDepartment"]) {if (this == null || isNull) null else asString() },
                        jobCompany = with(node["jobCompany"]) {if (this == null || isNull) null else asString() },
                        since = with(node["since"]) {if (this == null || isNull) null else traverse(p.objectReadContext())?.readValueAs(Temporal::class.java)}
                    )
                }
            }

            class OldSerializer : JsonSerializer<Job>() {
                override fun serialize(value: Job, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("jobTitle", value.jobTitle)
                    gen.writeStringField("jobDepartment", value.jobDepartment)
                    gen.writeStringField("jobCompany", value.jobCompany)
                    gen.writeObjectField("since", value.since.toString())
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Job>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Job {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    return Job(
                        jobTitle = with(node["jobTitle"]) {if (this == null || isNull) null else asText() },
                        jobDepartment = with(node["jobDepartment"]) {if (this == null || isNull) null else asText() },
                        jobCompany = with(node["jobCompany"]) {if (this == null || isNull) null else asText() },
                        since = with(node["since"]) {if (this == null || isNull) null else traverse(p.codec)?.readValueAs(Temporal::class.java)}
                    )
                }
            }
        }
        
        /**
         * Provides a delegated property accessor that retrieves the value of the property with the specified name from the map
         * representation of the containing object.
         *
         * @param thisRef The receiver object for which the property is being accessed. Can be null.
         * @param property The metadata for the property being accessed, from which the name is extracted.
         * @return The value associated with the property's name in the map representation of the object, cast to the expected type.
         * @since 1.0.0
         */
        @Suppress("unchecked_cast")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R
    }
    /**
     * Represents an address with details such as street, civic number, city, neighbourhood, state,
     * postal code, and country. Each property is optional, allowing for flexible address configurations.
     *
     * @property street The street name of the address, nullable.
     * @property civicNumber The civic or house number of the address, nullable.
     * @property apartment The apartment number of the address, nullable.
     * @property floor The floor number of the address, nullable.
     * @property stair The stair number of the address, nullable.
     * @property building The building name or identifier of the address, nullable.
     * @property city The city of the address, nullable.
     * @property neighbourhood The neighbourhood or locality of the address, nullable.
     * @property state The state or region of the address, nullable.
     * @property postalCode The postal or ZIP code of the address, nullable.
     * @property country The country of the address, nullable, represented as a `Country` object.
     *
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    @JsonSerialize(using = Address.Companion.Serializer::class)
    @JsonDeserialize(using = Address.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Address.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Address.Companion.OldDeserializer::class)
    data class Address(
        var street: String? = null,
        var civicNumber: String? = null,
        var apartment: String? = null,
        var floor: String? = null,
        var stair: String? = null,
        var building: String? = null,
        var city: String? = null,
        var neighbourhood: String? = null,
        var state: String? = null,
        var district: String? = null,
        var postalCode: String? = null,
        var country: Country? = null
    ) {
        /**
         * Formats the address into a single string, allowing customization of whether the civic number
         * should appear before or after the street name.
         *
         * The method constructs the resulting address string based on non-null address components in
         * a logical and readable order.
         *
         * @param civicNumberBeforeStreet A boolean indicating whether the civic number should
         * come before the street name. Default is true.
         * @since 1.0.0
         */
        fun format(civicNumberBeforeStreet: Boolean = true) = buildString {
            if (civicNumberBeforeStreet && civicNumber.isNotNull()) append(civicNumber).append(" ")
            if (street.isNotNull()) append(street)
            if (!civicNumberBeforeStreet && civicNumber.isNotNull()) append(" ").append(civicNumber)
            if (apartment.isNotNull() || stair.isNotNull() || floor.isNotNull() || building.isNotNull()) append(" (")
            if (apartment.isNotNull()) append("Aparment $apartment")
            if (floor.isNotNull()) append(", Floor $floor")
            if (stair.isNotNull()) append(", Stair $stair")
            if (building.isNotNull()) append(", Building $building")
            if (apartment.isNotNull() || stair.isNotNull() || floor.isNotNull() || building.isNotNull()) append(')')
            append(", ")
            if (neighbourhood.isNotNull()) append(neighbourhood).append(", ")
            if (postalCode.isNotNull()) append(postalCode).append(" ")
            if (city.isNotNull()) append(city).append(" ")
            if (district.isNotNull()) append("(").append(district).append("), ")
            if (state.isNotNull()) append(state).append(", ")
            if (country.isNotNull()) append(country?.countryName)
            if (last() == ' ') deleteCharAt(lastIndex)
            if (last() == ',') deleteCharAt(lastIndex)
        }

        companion object {
            class Serializer : ValueSerializer<Address>() {
                override fun serialize(
                    value: Address,
                    gen: tools.jackson.core.JsonGenerator,
                    ctxt: SerializationContext
                ) {
                    gen.writeStartObject()
                    gen.writeStringProperty("street", value.street)
                    gen.writeStringProperty("civicNumber", value.civicNumber)
                    gen.writeStringProperty("apartment", value.apartment)
                    gen.writeStringProperty("floor", value.floor)
                    gen.writeStringProperty("stair", value.stair)
                    gen.writeStringProperty("building", value.building)
                    gen.writeStringProperty("city", value.city)
                    gen.writeStringProperty("neighbourhood", value.neighbourhood)
                    gen.writeStringProperty("state", value.state)
                    gen.writeStringProperty("district", value.district)
                    gen.writeStringProperty("postalCode", value.postalCode)
                    gen.writeStringProperty("country", value.country?.countryName)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<Address>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Address {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    return Address(
                        street = with(node["street"]) { if (this == null || isNull) null else asString() },
                        civicNumber = with(node["civicNumber"]) { if (this == null || isNull) null else asString() },
                        apartment = with(node["apartment"]) { if (this == null || isNull) null else asString() },
                        floor = with(node["floor"]) { if (this == null || isNull) null else asString() },
                        stair = with(node["stair"]) { if (this == null || isNull) null else asString() },
                        building = with(node["building"]) { if (this == null || isNull) null else asString() },
                        city = with(node["city"]) { if (this == null || isNull) null else asString() },
                        neighbourhood = with(node["neighbourhood"]) { if (this == null || isNull) null else asString() },
                        state = with(node["state"]) { if (this == null || isNull) null else asString() },
                        district = with(node["district"]) { if (this == null || isNull) null else asString() },
                        postalCode = with(node["postalCode"]) { if (this == null || isNull) null else asString() },
                        country = with(node["country"]) { if (this == null || isNull) null else asString().let { Country.ofCountryName(it) } }
                    )
                }
            }

            class OldSerializer : JsonSerializer<Address>() {
                override fun serialize(value: Address, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("street", value.street)
                    gen.writeStringField("civicNumber", value.civicNumber)
                    gen.writeStringField("apartment", value.apartment)
                    gen.writeStringField("floor", value.floor)
                    gen.writeStringField("stair", value.stair)
                    gen.writeStringField("building", value.building)
                    gen.writeStringField("city", value.city)
                    gen.writeStringField("neighbourhood", value.neighbourhood)
                    gen.writeStringField("state", value.state)
                    gen.writeStringField("district", value.district)
                    gen.writeStringField("postalCode", value.postalCode)
                    gen.writeStringField("country", value.country?.countryName)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Address>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Address {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    return Address(
                        street = with(node["street"]) { if (this == null || isNull) null else asText() },
                        civicNumber = with(node["civicNumber"]) { if (this == null || isNull) null else asText() },
                        apartment = with(node["apartment"]) { if (this == null || isNull) null else asText() },
                        floor = with(node["floor"]) { if (this == null || isNull) null else asText() },
                        stair = with(node["stair"]) { if (this == null || isNull) null else asText() },
                        building = with(node["building"]) { if (this == null || isNull) null else asText() },
                        city = with(node["city"]) { if (this == null || isNull) null else asText() },
                        neighbourhood = with(node["neighbourhood"]) { if (this == null || isNull) null else asText() },
                        state = with(node["state"]) { if (this == null || isNull) null else asText() },
                        district = with(node["district"]) { if (this == null || isNull) null else asText() },
                        postalCode = with(node["postalCode"]) { if (this == null || isNull) null else asText() },
                        country = with(node["country"]) { if (this == null || isNull) null else asText().let { Country.ofCountryName(it) } }
                    )
                }
            }
        }
        
        /**
         * Provides delegated property access by retrieving the value of the property
         * from a reflection-based map representation of the containing object.
         * This operator function allows the delegating object to use this property accessor.
         *
         * @param R The type of the value associated with the property.
         * @param thisRef Reference to the object for which the value is being retrieved.
         *                Can be null if the receiver is not relevant.
         * @param property Metadata about the property being accessed, such as its name.
         * @return The value of the property cast to the generic type `R`.
         * @since 1.0.0
         */
        @Suppress("unchecked_cast")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R
    }

    /**
     * Represents a value object for an email address.
     *
     * This class ensures the validity of the email address format upon initialization
     * by verifying it against a predefined email pattern.
     *
     * The `Email` class is implemented as a value class, and it delegates all properties and
     * methods related to `CharSequence` to the underlying `value` string.
     *
     * Serialization and deserialization are supported for JSON processing via custom
     * Jackson serializers and deserializers. Additionally, a JPA converter is provided for
     * database persistence.
     *
     * @property value The internal string representation of the email address.
     * @throws MalformedInputException If the given string is not a valid email address.
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    @JvmInline
    @JsonSerialize(using = Email.Companion.Serializer::class)
    @JsonDeserialize(using = Email.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Email.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Email.Companion.OldDeserializer::class)
    @Suppress("unused")
    value class Email(val value: String) : CharSequence {
        /**
         * Represents the length of the email string.
         * Provides the total number of characters contained in the underlying string value of the email.
         *
         * @return the length of the email string.
         * @since 1.0.0
         */
        override val length: Int
            get() = value.length
        
        /**
         * Retrieves the local part of the email address.
         *
         * The local part is the portion of the email address before the "@" symbol.
         * For example, in the email address "user@example.com", the local part
         * would be "user".
         *
         * @return A string representing the local part of the email address.
         * @since 1.0.0
         */
        val localPart: String
            get() = value before '@'

        /**
         * Retrieves the domain part of the email address.
         *
         * The domain is the portion of the email address following the "@" symbol.
         * For example, in the email address "user@example.com", the domain would be "example.com".
         *
         * @return the domain part of the email address as a string.
         * @since 1.0.0
         */
        val domain: String
            get() = value after '@'

        /**
         * Represents the domain name portion of an email address.
         *
         * This property extracts the domain name from the email's value, excluding the "@" prefix
         * and the top-level domain suffix. For example, in the email "user@example.com",
         * it would return "example".
         *
         * This property assumes that the email address is already validated.
         *
         * @receiver An instance of the containing Email class.
         * @return The domain name of the email as a string.
         * @since 1.0.0
         */
        val domainName: String
            get() = value after '@' beforeLast '.'
        
        /**
         * Provides the top-level domain (TLD) extracted from the email address.
         *
         * The TLD is determined by identifying the portion of the domain
         * after the last period in the email address.
         *
         * @return A string representing the top-level domain of the email address,
         * prefixed with a period (e.g., ".com", ".org").
         * @since 1.0.0
         */
        val topLevelDomain: String
            get() = ".${value afterLast '.'}"

        init {
            EMAIL_REGEX(value) || throw MalformedInputException("The string is not a valid email address")
        }

        companion object {
            /**
             * A regular expression pattern to validate email addresses.
             *
             * This regex pattern checks if a string conforms to a valid email structure,
             * including characters allowed in the local part and domain, the "@" symbol separating them,
             * and a valid domain suffix of at least two characters.
             *
             * The pattern is case-insensitive.
             *
             * @since 1.0.0
             */
            val EMAIL_REGEX = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", RegexOption.IGNORE_CASE)
            /**
             * Compiled regular expression pattern for validating email addresses.
             *
             * The pattern is derived from the constant `EMAIL_REGEX` and compiled
             * to be used in email validation scenarios. It ensures that email
             * strings conform to a standardized format for consistency and correctness.
             *
             * @see EMAIL_REGEX
             * @since 1.0.0
             */
            val EMAIL_PATTERN = EMAIL_REGEX.toPattern()
            /**
             * A functional variable that validates email addresses.
             * This variable uses the predefined `EMAIL_REGEX` pattern to match the input string for email format correctness.
             * It accepts a string input and returns a boolean value indicating whether the input matches the email format.
             *
             * @since 1.0.0
             */
            val EMAIL_VALIDATOR: Predicate<String> = { EMAIL_REGEX.matches(it) }

            /**
             * Checks whether the given CharSequence represents a valid email address.
             *
             * This function uses the `Email` class to validate the structure of the email.
             * If the `CharSequence` can be successfully converted to an instance of `Email`,
             * the function returns `true`. Otherwise, it returns `false`.
             *
             * @receiver The input CharSequence to validate as an email.
             * @return `true` if the input is a syntactically valid email address, `false` otherwise.
             * @since 1.0.0
             */
            fun CharSequence.isValidEmail() = runCatching { Email(toString()) }.isSuccess

            /**
             * Converts the current [CharSequence] into an instance of the [Email] class.
             *
             * The resulting [Email] object encapsulates the string representation of the email address.
             * This method can be used to create a strongly-typed representation of an email address
             * from a general text-based [CharSequence].
             *
             * @receiver [CharSequence] to be converted to an [Email].
             * @return an [Email] instance representing the same email address as the [CharSequence],
             * wrapped in a [Result].
             * @since 1.0.0
             */
            fun CharSequence.toEmail() = runCatching { Email(toString()) }

            class Serializer : ValueSerializer<Email>() {
                override fun serialize(
                    value: Email,
                    gen: tools.jackson.core.JsonGenerator,
                    ctxt: SerializationContext
                ) { gen.writeString(value.value) }
            }

            class Deserializer : ValueDeserializer<Email>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = Email(p.string)
            }

            class OldSerializer : JsonSerializer<Email>() {
                override fun serialize(value: Email, gen: JsonGenerator, serializers: SerializerProvider) =
                    gen.writeString(value.value)
            }

            class OldDeserializer : JsonDeserializer<Email>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Email = Email(p.text)
            }

            @jakarta.persistence.Converter(autoApply = true)
            class Converter : AttributeConverter<Email?, String?> {
                override fun convertToDatabaseColumn(attribute: Email?): String? = attribute?.value
                override fun convertToEntityAttribute(dbData: String?): Email? = dbData?.let { Email(it) }
            }
        }

        /**
         * Returns the character at the specified [index] from the underlying string value.
         *
         * @param index the position of the character to be returned. It must be within the valid range of the string.
         * @return the character located at the specified [index].
         * @throws IndexOutOfBoundsException if the [index] is out of range (index < 0 || index >= length).
         * @since 1.0.0
         */
        override operator fun get(index: Int) = value[index]

        /**
         * Returns a new character sequence that is a subsequence of this character sequence.
         *
         * @param startIndex the start index of the subsequence (inclusive).
         * @param endIndex the end index of the subsequence (exclusive).
         * @return a new character sequence that represents the specified subsequence.
         * @throws IndexOutOfBoundsException if the start or end index is out of bounds of the character sequence.
         * @since 1.0.0
         */
        override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)

        /**
         * Returns the string representation of the Email value.
         *
         * @return the email address as a String
         * @since 1.0.0
         */
        override fun toString(): String = value
    }


    /**
     * A value class representing a phone number.
     *
     * This class ensures that phone numbers abide by specific input criteria and provides
     * functionalities to inspect and manipulate the phone number, such as normalization
     * and determining the presence of a country code. The phone number is stored as a
     * string and can be serialized or deserialized using the provided custom tools.
     *
     * Restrictions on phone number formatting are enforced during initialization to
     * ensure compliance with the expected structure.
     *
     * @property value The raw phone number string provided during instantiation.
     * The value can include numeric digits, spaces, dashes, parentheses, and an optional "+".
     * Invalid formats result in a `MalformedInputException` being thrown.
     * @since 1.0.0
     * @author Tommaso Pastorelli
     */
    @JvmInline
    @JsonSerialize(using = PhoneNumber.Companion.Serializer::class)
    @JsonDeserialize(using = PhoneNumber.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = PhoneNumber.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = PhoneNumber.Companion.OldDeserializer::class)
    @Suppress("unused")
    value class PhoneNumber(val value: String) {
        /**
         * Represents the length of the normalized phone number string.
         *
         * This property provides the total number of characters contained within
         * the normalized version of the phone number.
         *
         * @return the length of the normalized phone number string.
         * @since 1.0.0
         */
        val length: Int
            get() = normalized.length

        /**
         * Indicates whether the phone number value is in a normalized format.
         *
         * A normalized format ensures that any transformations or formatting
         * applied to the phone number have been reverted to a standard state,
         * suitable for storage or further processing.
         *
         * This property compares the `value` of the phone number with its
         * `normalized` representation to determine equality. If they are equal,
         * it signifies that the phone number is normalized.
         *
         * @return `true` if the phone number is normalized, otherwise `false`.
         * @since 1.0.0
         */
        val isNormalized: Boolean
            get() = normalized == value

        /**
         * Provides a normalized representation of the phone number.
         *
         * The normalization process ensures that the resulting string includes only numeric characters
         * and, if the phone number starts with a "+", retains this prefix. Any other characters, such as
         * spaces, dashes, or parentheses, are stripped. This ensures a clean representation of the
         * phone number for easier handling and comparisons.
         *
         * @return a normalized phone number string containing only numeric digits and an optional "+" prefix.
         * @since 1.0.0
         */
        val normalized: String
            get() {
                val digitsAndPlus = value.filter { it.isDigit() || it == '+' }
                if (digitsAndPlus.startsWith("+")) {
                    return "+" + (-1)(digitsAndPlus).filter { it.isDigit() }
                }
                return digitsAndPlus.filter { it.isDigit() }
            }

        /**
         * Indicates whether the phone number contains a country code.
         *
         * This property checks if the phone number string starts with the '+' character,
         * which is typically used to denote the presence of an international country code
         * in phone numbers.
         *
         * @return `true` if the phone number starts with a '+', indicating the presence
         * of a country code; `false` otherwise.
         * @since 1.0.0
         */
        val hasCountryCode: Boolean
            get() = value.startsWith("+") || value.startsWith("00")

        /**
         * Retrieves the country code from the phone number value if available.
         *
         * The `countryCode` property calculates and returns the country code extracted
         * from the `value` field of the `PhoneNumber` class. The value is determined
         * based on the prefix of the `value` string and follows country-specific rules.
         *
         * If the `value` does not contain a valid country code or if `hasCountryCode`
         * is `false`, this property returns `null`.
         *
         * @return The extracted country code as a nullable string, or `null` if the country code cannot be determined.
         * @since 1.0.0
         */
        val countryCode: String?
            get() {
                if (!hasCountryCode) return null
                val newVal = if (value.startsWith("+")) (-1)(value) else (-2)(value)
                return newVal.run {
                    if (startsWith("1")) {
                        if (startsWith("1 (") || startsWith("1("))
                            substring(0, indexOf(')'))
                        else "1"
                    } else if (startsWith("20")) "20"
                    else if (startsWith("21")) substring(0, 3)
                    else if (startsWith("22")) substring(0, 3)
                    else if (startsWith("23")) substring(0, 3)
                    else if (startsWith("24")) substring(0, 3)
                    else if (startsWith("25")) substring(0, 3)
                    else if (startsWith("262 (269)") || startsWith("262(269)")) "262 (269)"
                    else if (startsWith("262 (639)") || startsWith("262(639)")) "262 (639)"
                    else if (startsWith("26")) substring(0, 3)
                    else if (startsWith("27")) "27"
                    else if (startsWith("29")) substring(0, 3)
                    else if (startsWith("30")) "30"
                    else if (startsWith("31")) "31"
                    else if (startsWith("32")) "32"
                    else if (startsWith("33")) "33"
                    else if (startsWith("34")) "34"
                    else if (startsWith("358 (18)") || startsWith("358(18)")) "358 (18)"
                    else if (startsWith("358")) "358"
                    else if (startsWith("35")) substring(0, 3)
                    else if (startsWith("36")) "36"
                    else if (startsWith("37")) substring(0, 3)
                    else if (startsWith("38")) substring(0, 3)
                    else if (startsWith("39")) "39"
                    else if (startsWith("40")) "40"
                    else if (startsWith("41")) "41"
                    else if (startsWith("42")) substring(0, 3)
                    else if (startsWith("43")) "43"
                    else if (startsWith("44 (1481)") || startsWith("44(1481)")) "44 (1481)"
                    else if (startsWith("44 (1534)") || startsWith("44(1534)")) "44 (1534)"
                    else if (startsWith("44 (1624)") || startsWith("44(1624)")) "44 (1624)"
                    else if (startsWith("44")) "44"
                    else if (startsWith("45")) "45"
                    else if (startsWith("46")) "46"
                    else if (startsWith("47")) "47"
                    else if (startsWith("48")) "48"
                    else if (startsWith("49")) "49"
                    else if (startsWith("50")) substring(0, 3)
                    else if (startsWith("51")) "51"
                    else if (startsWith("52")) "52"
                    else if (startsWith("53")) "53"
                    else if (startsWith("54")) "54"
                    else if (startsWith("55")) "55"
                    else if (startsWith("56")) "56"
                    else if (startsWith("57")) "57"
                    else if (startsWith("58")) "58"
                    else if (startsWith("59")) substring(0, 3)
                    else if (startsWith("60")) "60"
                    else if (startsWith("61 (8 9162)") || startsWith("61(89162)")) "61 (8 9162)"
                    else if (startsWith("61 (8 9164)") || startsWith("61(89164)")) "61 (8 9164)"
                    else if (startsWith("61")) "61"
                    else if (startsWith("62")) "62"
                    else if (startsWith("63")) "63"
                    else if (startsWith("64")) "64"
                    else if (startsWith("65")) "65"
                    else if (startsWith("66")) "66"
                    else if (startsWith("67")) substring(0, 3)
                    else if (startsWith("68")) substring(0, 3)
                    else if (startsWith("69")) substring(0, 3)
                    else if (startsWith("7")) "7"
                    else if (startsWith("8")) substring(0, 3)
                    else if (startsWith("90")) "90"
                    else if (startsWith("91")) "91"
                    else if (startsWith("92")) "92"
                    else if (startsWith("93")) "93"
                    else if (startsWith("94")) "94"
                    else if (startsWith("95")) "95"
                    else if (startsWith("96")) substring(0, 3)
                    else if (startsWith("97")) substring(0, 3)
                    else if (startsWith("98")) "98"
                    else if (startsWith("99")) substring(0, 3)
                    else null
                }
            }

        /**
         * Retrieves a list of countries based on the specified country code.
         * The `country` variable maps international dialing codes to their corresponding
         * countries or regions. This information is represented as a list of `Country` instances.
         *
         * The mapping supports a wide range of country codes, including specific ones for regions
         * with multiple dialing prefixes. If a country code matches one of the cases in the mapping,
         * the corresponding countries are returned.
         *
         * This property is evaluated dynamically using a `when` expression to determine the list of
         * countries based on the provided `countryCode`.
         *
         * @property country Provides the list of countries associated with a given country code.
         * @see Country
         * @since 1.0.0
         */
        val country: List<Country>
            get() = when (countryCode) {
                "1" -> listOf(Country.UNITED_STATES, Country.CANADA)
                "1 (340)", "1(340)" -> listOf(Country.US_VIRGIN_ISLANDS)
                "1 (670)", "1(670)" -> listOf(Country.NORTHERN_MARIANA_ISLANDS)
                "1 (671)", "1(671)" -> listOf(Country.GUAM)
                "1 (684)", "1(684)" -> listOf(Country.AMERICAN_SAMOA)
                "1 (787)", "1(787)", "1 (939)", "1(939)" -> listOf(Country.PUERTO_RICO)
                "1 (242)", "1(242)" -> listOf(Country.BAHAMAS)
                "1 (246)", "1(246)" -> listOf(Country.BARBADOS)
                "1 (264)", "1(264)" -> listOf(Country.ANGUILLA)
                "1 (268)", "1(268)" -> listOf(Country.ANTIGUA_AND_BARBUDA)
                "1 (284)", "1(284)" -> listOf(Country.BRITISH_VIRGIN_ISLANDS)
                "1 (345)", "1(345)" -> listOf(Country.CAYMAN_ISLANDS)
                "1 (441)", "1(441)" -> listOf(Country.BERMUDA)
                "1 (473)", "1(473)" -> listOf(Country.GRENADA)
                "1 (649)", "1(649)" -> listOf(Country.TURKS_AND_CAICOS_ISLANDS)
                "1 (658)", "1(658)", "1 (876)", "1(876)" -> listOf(Country.JAMAICA)
                "1 (664)", "1(664)" -> listOf(Country.MONTSERRAT)
                "1 (721)", "1(721)" -> listOf(Country.SINT_MAARTEN)
                "1 (758)", "1(758)" -> listOf(Country.SAINT_LUCIA)
                "1 (767)", "1(767)" -> listOf(Country.DOMINICA)
                "1 (784)", "1(784)" -> listOf(Country.SAINT_VINCENT_AND_THE_GRENADINES)
                "1 (809)", "1(809)", "1 (829)", "1(829)", "1 (849)", "1(849)" -> listOf(Country.DOMINICAN_REPUBLIC)
                "1 (868)", "1(868)" -> listOf(Country.TRINIDAD_AND_TOBAGO)
                "1 (869)", "1(869)" -> listOf(Country.SAINT_KITTS_AND_NEVIS)
                "20" -> listOf(Country.EGYPT)
                "211" -> listOf(Country.SOUTH_SUDAN)
                "212" -> listOf(Country.MOROCCO, Country.WESTERN_SAHARA)
                "213" -> listOf(Country.ALGERIA)
                "216" -> listOf(Country.TUNISIA)
                "218" -> listOf(Country.LIBYA)
                "220" -> listOf(Country.GAMBIA)
                "221" -> listOf(Country.SENEGAL)
                "222" -> listOf(Country.MAURITANIA)
                "223" -> listOf(Country.MALI)
                "224" -> listOf(Country.GUINEA)
                "225" -> listOf(Country.COTE_D_IVORIE)
                "226" -> listOf(Country.BURKINA_FASO)
                "227" -> listOf(Country.NIGER)
                "228" -> listOf(Country.TOGO)
                "229" -> listOf(Country.BENIN)
                "230" -> listOf(Country.MAURITIUS)
                "231" -> listOf(Country.LIBERIA)
                "232" -> listOf(Country.SIERRA_LEONE)
                "233" -> listOf(Country.GHANA)
                "234" -> listOf(Country.NIGERIA)
                "235" -> listOf(Country.CHAD)
                "236" -> listOf(Country.CENTRAL_AFRICAN_REPUBLIC)
                "237" -> listOf(Country.CAMEROON)
                "238" -> listOf(Country.CABO_VERDE)
                "239" -> listOf(Country.SAO_TOME_AND_PRINCIPE)
                "240" -> listOf(Country.EQUATORIAL_GUINEA)
                "241" -> listOf(Country.GABON)
                "242" -> listOf(Country.CONGO)
                "243" -> listOf(Country.DEMOCRATIC_REPUBLIC_OF_THE_CONGO)
                "244" -> listOf(Country.ANGOLA)
                "245" -> listOf(Country.GUINEA_BISSAU)
                "246" -> listOf(Country.BRITISH_INDIAN_OCEAN_TERRITORY)
                "247" -> listOf(Country.SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA)
                "248" -> listOf(Country.SEYCHELLES)
                "249" -> listOf(Country.SUDAN)
                "250" -> listOf(Country.RWANDA)
                "251" -> listOf(Country.ETHIOPIA)
                "252" -> listOf(Country.SOMALIA)
                "253" -> listOf(Country.DJIBOUTI)
                "254" -> listOf(Country.KENYA)
                "255" -> listOf(Country.TANZANIA)
                "256" -> listOf(Country.UGANDA)
                "257" -> listOf(Country.BURUNDI)
                "258" -> listOf(Country.MOZAMBIQUE)
                "260" -> listOf(Country.ZAMBIA)
                "261" -> listOf(Country.MADAGASCAR)
                "262" -> listOf(Country.REUNION)
                "262 (269)", "262(269)", "262 (639)", "262(639)" -> listOf(Country.MAYOTTE)
                "263" -> listOf(Country.ZIMBABWE)
                "264" -> listOf(Country.NAMIBIA)
                "265" -> listOf(Country.MALAWI)
                "266" -> listOf(Country.LESOTHO)
                "267" -> listOf(Country.BOTSWANA)
                "268" -> listOf(Country.ESWATINI)
                "269" -> listOf(Country.COMOROS)
                "27" -> listOf(Country.SOUTH_AFRICA)
                "290" -> listOf(Country.SAINT_HELENA_ASCENSION_AND_TRISTAN_DA_CUNHA)
                "291" -> listOf(Country.ERITREA)
                "297" -> listOf(Country.ARUBA)
                "298" -> listOf(Country.FAROE_ISLANDS)
                "299" -> listOf(Country.GREENLAND)
                "30" -> listOf(Country.GREECE)
                "31" -> listOf(Country.NETHERLANDS)
                "32" -> listOf(Country.BELGIUM)
                "33" -> listOf(Country.FRANCE)
                "34" -> listOf(Country.SPAIN)
                "350" -> listOf(Country.GIBRALTAR)
                "351" -> listOf(Country.PORTUGAL)
                "352" -> listOf(Country.LUXEMBOURG)
                "353" -> listOf(Country.IRELAND)
                "354" -> listOf(Country.ICELAND)
                "355" -> listOf(Country.ALBANIA)
                "356" -> listOf(Country.MALTA)
                "357" -> listOf(Country.CYPRUS)
                "358" -> listOf(Country.FINLAND)
                "358 (18)", "358(18)" -> listOf(Country.ALAND_ISLAND)
                "359" -> listOf(Country.BULGARIA)
                "36" -> listOf(Country.HUNGARY)
                "370" -> listOf(Country.LITHUANIA)
                "371" -> listOf(Country.LATVIA)
                "372" -> listOf(Country.ESTONIA)
                "373" -> listOf(Country.MOLDOVA)
                "374" -> listOf(Country.ARMENIA)
                "375" -> listOf(Country.BELARUS)
                "376" -> listOf(Country.ANDORRA)
                "377" -> listOf(Country.MONACO)
                "378" -> listOf(Country.SAN_MARINO)
                "379" -> listOf(Country.VATICAN_CITY)
                "380" -> listOf(Country.UKRAINE)
                "381" -> listOf(Country.SERBIA)
                "382" -> listOf(Country.MONTENEGRO)
                "383" -> listOf(Country.KOSOVO)
                "385" -> listOf(Country.CROATIA)
                "386" -> listOf(Country.SLOVENIA)
                "387" -> listOf(Country.BOSNIA_AND_HERZEGOVINA)
                "389" -> listOf(Country.NORTH_MACEDONIA)
                "39" -> listOf(Country.ITALY, Country.SAN_MARINO, Country.VATICAN_CITY)
                "40" -> listOf(Country.ROMANIA)
                "41" -> listOf(Country.SWITZERLAND)
                "420" -> listOf(Country.CZECH_REPUBLIC)
                "421" -> listOf(Country.SLOVAKIA)
                "423" -> listOf(Country.LIECHTENSTEIN)
                "43" -> listOf(Country.AUSTRIA)
                "44" -> listOf(Country.UNITED_KINGDOM)
                "44 (1481)", "44(1481)" -> listOf(Country.GUERNSEY)
                "44 (1534)", "44(1534)" -> listOf(Country.JERSEY)
                "44 (1624)", "44(1624)" -> listOf(Country.ISLE_OF_MAN)
                "45" -> listOf(Country.DENMARK)
                "46" -> listOf(Country.SWEDEN)
                "47" -> listOf(Country.NORWAY)
                "48" -> listOf(Country.POLAND)
                "49" -> listOf(Country.GERMANY)
                "500" -> listOf(Country.FALKLAND_ISLANDS, Country.SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS)
                "501" -> listOf(Country.BELIZE)
                "502" -> listOf(Country.GUATEMALA)
                "503" -> listOf(Country.EL_SALVADOR)
                "504" -> listOf(Country.HONDURAS)
                "505" -> listOf(Country.NICARAGUA)
                "506" -> listOf(Country.COSTA_RICA)
                "507" -> listOf(Country.PANAMA)
                "508" -> listOf(Country.SAINT_PIERRE_AND_MIQUELON)
                "509" -> listOf(Country.HAITI)
                "51" -> listOf(Country.PERU)
                "52" -> listOf(Country.MEXICO)
                "53" -> listOf(Country.CUBA)
                "54" -> listOf(Country.ARGENTINA)
                "55" -> listOf(Country.BRAZIL)
                "56" -> listOf(Country.CHILE)
                "57" -> listOf(Country.COLOMBIA)
                "58" -> listOf(Country.VENEZUELA)
                "590" -> listOf(Country.GUADELOUPE, Country.SAINT_BARTHELEMY, Country.SAINT_MARTIN)
                "591" -> listOf(Country.BOLIVIA)
                "592" -> listOf(Country.GUYANA)
                "593" -> listOf(Country.ECUADOR)
                "594" -> listOf(Country.FRENCH_GUIANA)
                "595" -> listOf(Country.PARAGUAY)
                "596" -> listOf(Country.MARTINIQUE)
                "597" -> listOf(Country.SURINAME)
                "598" -> listOf(Country.URUGUAY)
                "599" -> listOf(Country.BONAIRE_SINT_EUSTATIUS_AND_SABA, Country.CURACAO)
                "60" -> listOf(Country.MALAYSIA)
                "61" -> listOf(Country.AUSTRALIA)
                "61 (8 9162)", "61 (89162)" -> listOf(Country.COCOS_ISLANDS)
                "61 (8 9164)", "61 (89164)" -> listOf(Country.CHRISTMAS_ISLAND)
                "62" -> listOf(Country.INDONESIA)
                "63" -> listOf(Country.PHILIPPINES)
                "64" -> listOf(Country.NEW_ZEALAND, Country.PITCAIRN_ISLANDS)
                "65" -> listOf(Country.SINGAPORE)
                "66" -> listOf(Country.THAILAND)
                "670" -> listOf(Country.TIMOR_LESTE)
                "672" -> listOf(Country.NORFOLK_ISLAND)
                "673" -> listOf(Country.BRUNEI)
                "674" -> listOf(Country.NAURU)
                "675" -> listOf(Country.PAPUA_NEW_GUINEA)
                "676" -> listOf(Country.TONGA)
                "677" -> listOf(Country.SOLOMON_ISLANDS)
                "678" -> listOf(Country.VANUATU)
                "679" -> listOf(Country.FIJI)
                "680" -> listOf(Country.PALAU)
                "681" -> listOf(Country.WALLIS_AND_FUTUNA)
                "682" -> listOf(Country.COOK_ISLANDS)
                "683" -> listOf(Country.NIUE)
                "685" -> listOf(Country.SAMOA)
                "686" -> listOf(Country.KIRIBATI)
                "687" -> listOf(Country.NEW_CALEDONIA)
                "688" -> listOf(Country.TUVALU)
                "689" -> listOf(Country.FRENCH_POLYNESIA)
                "690" -> listOf(Country.TOKELAU)
                "691" -> listOf(Country.MICRONESIA)
                "692" -> listOf(Country.MARSHALL_ISLANDS)
                "7" -> listOf(Country.RUSSIAN_FEDERATION, Country.KAZAKHSTAN)
                "81" -> listOf(Country.JAPAN)
                "82" -> listOf(Country.SOUTH_KOREA)
                "84" -> listOf(Country.VIETNAM)
                "850" -> listOf(Country.NORTH_KOREA)
                "852" -> listOf(Country.HONG_KONG)
                "853" -> listOf(Country.MACAO)
                "855" -> listOf(Country.CAMBODIA)
                "856" -> listOf(Country.LAOS)
                "86" -> listOf(Country.CHINA)
                "880" -> listOf(Country.BANGLADESH)
                "886" -> listOf(Country.TAIWAN)
                "90" -> listOf(Country.TURKEY)
                "91" -> listOf(Country.INDIA)
                "92" -> listOf(Country.PAKISTAN)
                "93" -> listOf(Country.AFGHANISTAN)
                "94" -> listOf(Country.SRI_LANKA)
                "95" -> listOf(Country.MYANMAR)
                "960" -> listOf(Country.MALDIVES)
                "961" -> listOf(Country.LEBANON)
                "962" -> listOf(Country.JORDAN)
                "963" -> listOf(Country.SYRIA)
                "964" -> listOf(Country.IRAQ)
                "965" -> listOf(Country.KUWAIT)
                "966" -> listOf(Country.SAUDI_ARABIA)
                "967" -> listOf(Country.YEMEN)
                "968" -> listOf(Country.OMAN)
                "970" -> listOf(Country.PALESTINE)
                "971" -> listOf(Country.UNITED_ARAB_EMIRATES)
                "972" -> listOf(Country.ISRAEL, Country.PALESTINE)
                "973" -> listOf(Country.BAHRAIN)
                "974" -> listOf(Country.QATAR)
                "975" -> listOf(Country.BHUTAN)
                "976" -> listOf(Country.MONGOLIA)
                "977" -> listOf(Country.NEPAL)
                "98" -> listOf(Country.IRAN)
                "992" -> listOf(Country.TAJIKISTAN)
                "993" -> listOf(Country.TURKMENISTAN)
                "994" -> listOf(Country.AZERBAIJAN)
                "995" -> listOf(Country.GEORGIA)
                "996" -> listOf(Country.KYRGYZSTAN)
                "998" -> listOf(Country.UZBEKISTAN)
                else -> emptyList()
            }

        init {
            value.matches(Regex("^[+]?[0-9\\s\\-()/]*$")) || throw MalformedInputException("Phone number format is not valid.")
        }

        companion object {
            /**
             * Validates if the current character sequence represents a valid phone number.
             *
             * This method attempts to construct a `PhoneNumber` object using the current sequence
             * and checks if the operation is successful. If the creation does not throw an
             * exception, the sequence is considered a valid phone number.
             *
             * @receiver The character sequence that is being validated as a phone number.
             * @return `true` if the character sequence represents a valid phone number, `false` otherwise.
             * @since 1.0.0
             */
            fun CharSequence.isValidPhoneNumber() = runCatching { PhoneNumber(toString()) }.isSuccess

            /**
             * Converts the current [CharSequence] into an instance of the [PhoneNumber] class.
             *
             * This method creates a strongly-typed representation of a phone number
             * from a general text-based [CharSequence].
             *
             * The resulting [PhoneNumber] object encapsulates the string representation
             * of the phone number.
             *
             * @receiver [CharSequence] to be converted to a [PhoneNumber].
             * @return a [Result] wrapping the [PhoneNumber] instance created from the [CharSequence].
             * If the conversion fails, the [Result] will contain the corresponding exception.
             * @since 1.0.0
             */
            fun CharSequence.toPhoneNumber() = runCatching { PhoneNumber(toString()) }

            class Serializer : ValueSerializer<PhoneNumber>() {
                override fun serialize(
                    value: PhoneNumber,
                    gen: tools.jackson.core.JsonGenerator,
                    ctxt: SerializationContext
                ) { gen.writeString(value.value) }
            }

            class Deserializer : ValueDeserializer<PhoneNumber>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = PhoneNumber(p.string)
            }

            class OldSerializer : JsonSerializer<PhoneNumber>() {
                override fun serialize(value: PhoneNumber, gen: JsonGenerator, serializers: SerializerProvider) =
                    gen.writeString(value.value)
            }

            class OldDeserializer : JsonDeserializer<PhoneNumber>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): PhoneNumber = PhoneNumber(p.text)
            }

            @jakarta.persistence.Converter(autoApply = true)
            class Converter : AttributeConverter<PhoneNumber?, String?> {
                override fun convertToDatabaseColumn(attribute: PhoneNumber?): String? = attribute?.value
                override fun convertToEntityAttribute(dbData: String?): PhoneNumber? = dbData?.let { PhoneNumber(it) }
            }
        }

        /**
         * Returns the string representation of the PhoneNumber value.
         *
         * @return the phone number as a String
         * @since 1.0.0
         */
        override fun toString(): String = value

        /**
         * Converts the internal state of the `PhoneNumber` object into a map representation.
         *
         * The resulting map contains the following key-value pairs:
         * - "normalized": the normalized representation of the phone number.
         * - "countryCode": the country code associated with the phone number.
         * - "country": the country name derived from the phone number.
         *
         * This method is intended for internal use and provides a structured way to
         * access key properties of the `PhoneNumber` object.
         *
         * @return a map containing the normalized phone number, country code, and country name.
         * @since 1.0.0
         */
        @Suppress("functionName")
        private fun _toMap() = mapOf(
            "normalized" to normalized,
            "countryCode" to countryCode,
            "country" to country
        )

        /**
         * Retrieves the value associated with the given property from the internal map representation.
         *
         * This operator function allows access to specific properties of the underlying object
         * by delegating the property retrieval to a map that represents the object's state.
         *
         * - `normalized` - TYPE: [String]
         * - `countryCode` - TYPE: [String]
         * - `country` - TYPE: `List<Country>`
         *
         * @param thisRef the reference to the object for which the property is being accessed. May be null.
         * @param property the metadata about the property being accessed, such as its name.
         * @return the value of the property, cast to the expected type [R].
         * @since 1.0.0
         */
        @Suppress("UNCHECKED_CAST")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = _toMap().getValue(property.name) as R
    }
}
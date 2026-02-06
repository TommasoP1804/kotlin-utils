package dev.tommasop1804.kutils.classes.time

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.LocalTime
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.asSet
import dev.tommasop1804.kutils.classes.colors.Color
import dev.tommasop1804.kutils.classes.registry.Contact
import dev.tommasop1804.kutils.classes.time.TemporalInterval.Companion.intervalTo
import dev.tommasop1804.kutils.expectClass
import dev.tommasop1804.kutils.invoke
import dev.tommasop1804.kutils.isNotDecimal
import dev.tommasop1804.kutils.isNull
import dev.tommasop1804.kutils.toReflectionMap
import dev.tommasop1804.kutils.validate
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueDeserializer
import tools.jackson.databind.ValueSerializer
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import tools.jackson.databind.node.ObjectNode
import java.net.URI
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.reflect.KProperty
import kotlin.text.startsWith

/**
 * Represents a calendar event with detailed properties for scheduling and managing events,
 * including time period, participants, recurrence, reminders, and more. A `CalendarEvent` can
 * be all-day or span a specific time frame, optionally repeating over a defined interval.
 *
 * @property name The name or title of the event.
 * @property period The time interval of the event, represented as a `TemporalInterval`.
 * @property repeat Recurrence configuration for the event, if applicable.
 * @property partecipants The set of participants for the event.
 * @property conferencing The URL for a conferencing service associated with the event.
 * @property description A textual description providing additional details about the event.
 * @property location The location of the event, which can be a physical address or null.
 * @property busy Indicates whether the event should mark the time as busy.
 * @property private Indicates whether the event is private.
 * @property reminders A set of durations representing reminders before the event starts.
 * @since 1.0.0
 * @author Tommaso Pastorelli
 */
@JsonSerialize(using = CalendarEvent.Companion.Serializer::class)
@JsonDeserialize(using = CalendarEvent.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = CalendarEvent.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = CalendarEvent.Companion.OldDeserializer::class)
@Suppress("unused", "kutils_getorthrow_as_invoke")
data class CalendarEvent(
    var name: String,
    var period: TemporalInterval,
    var repeat: Repeat? = null,
    var partecipants: Set<EventPartecipation> = emptySet(),
    var conferencing: URI? = null,
    var description: String? = null,
    var location: String? = null,
    var busy: Boolean = true,
    var private: Boolean = false,
    var reminders: Set<Duration> = emptySet()
) {
    /**
     * Indicates whether the event occupies an entire day. This is determined by
     * checking if the duration of the associated time period is exactly one day
     * and if the starting time of the period is set to midnight.
     *
     * @return `true` if the event spans an entire day, otherwise `false`.
     * @since 1.0.0
     */
    val allDay: Boolean
        get() = period.duration.toDays(period.start).isNotDecimal
                && (period.start as OffsetDateTime).toLocalTime().equals(LocalTime(0,0))

    /**
     * Constructs a new instance of the CalendarEvent class with the specified properties.
     *
     * @param name The name of the calendar event.
     * @param period The temporal interval during which the event occurs.
     * @param repeat The optional repeat configuration for the event.
     * @param partecipants The set of participants involved in the event. Default is an empty set.
     * @param conferencing The optional URI for a conferencing link associated with the event.
     * @param description The optional description of the event.
     * @param location The address or location where the event is held. This will be formatted during initialization.
     * @param busy Indicates whether the time block for this event should be marked as busy. Default is false.
     * @param private Indicates whether the event should be marked as private. Default is false.
     * @param reminders A set of durations specifying reminders for the event. Default is an empty set.
     * @since 1.0.0
     */
    constructor(
        name: String,
        period: TemporalInterval,
        repeat: Repeat? = null,
        partecipants: Set<EventPartecipation> = emptySet(),
        conferencing: URI? = null,
        description: String? = null,
        location: Contact.Address,
        busy: Boolean = true,
        private: Boolean = false,
        reminders: Set<Duration> = emptySet()
    ) : this(
        name = name,
        period = period,
        repeat = repeat,
        partecipants = partecipants,
        conferencing = conferencing,
        description = description,
        location = location.format(),
        busy = busy,
        private = private
    )

    /**
     * Secondary constructor for creating a `CalendarEvent` instance with additional configuration options.
     *
     * @param name The name of the event.
     * @param start The start date and time of the event.
     * @param end The optional end date and time of the event. If not provided, the event will default to a one-day period.
     * @param repeat The repetition configuration for the event, specifying duration, days of the week, and optional end condition.
     * @param partecipants A set of participant contacts associated with the event.
     * @param conferencing An optional conferencing URL for the event.
     * @param description An optional description providing additional details about the event.
     * @param location The optional physical or virtual location where the event takes place.
     * @param busy A flag indicating whether the event will mark the participant's time as busy.
     * @param private A flag indicating whether the event is private.
     * @param reminders A set of reminder durations before the event starts.
     *
     * @since 1.0.0
     */
    constructor(
        name: String,
        start: OffsetDateTime,
        end: OffsetDateTime? = null,
        repeat: Repeat? = null,
        partecipants: Set<EventPartecipation> = emptySet(),
        conferencing: URI? = null,
        description: String? = null,
        location: String? = null,
        busy: Boolean = true,
        private: Boolean = false,
        reminders: Set<Duration> = emptySet()
    ) : this(
        name = name,
        period = if (end.isNull()) start.withHour(0).withMinute(0).withSecond(0).withNano(0) intervalTo start.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1) else start intervalTo end,
        repeat = repeat,
        partecipants = partecipants,
        conferencing = conferencing,
        description = description,
        location = location,
        busy = busy,
        private = private,
        reminders = reminders
    )

    /**
     * Secondary constructor for creating a `CalendarEvent` instance.
     *
     * This constructor allows for the more simplified initialization of a `CalendarEvent`
     * by internally calculating the event's `period` based on the provided `start` and `end` parameters.
     *
     * If `end` is provided, the event period is defined as the interval between the `start` and `end` dates.
     * If `end` is not provided (i.e., null), the event period defaults to a single day starting at midnight of the `start` date.
     *
     * @param name Name of the event.
     * @param start The start date and time of the event.
     * @param end Optional end date and time of the event. Defaults to null.
     * @param repeat Optional recurrence rules for the event. Defaults to null.
     * @param partecipants A set of `Contact` objects representing the event participants. Defaults to an empty set.
     * @param conferencing Optional URL link to an online meeting or conferencing tool. Defaults to null.
     * @param description An optional description or details about the event. Defaults to null.
     * @param location Optional location of the event represented as a formatted `Contact.Address`. Defaults to null.
     * @param busy Flag indicating whether the organizer's schedule should be marked as busy during the event. Defaults to false.
     * @param private Flag indicating whether the event is private or visible to others. Defaults to false.
     * @param reminders A set of reminder durations specifying when notifications should trigger before the event. Defaults to an empty set.
     * @since 1.0.0
     */
    constructor(
        name: String,
        start: OffsetDateTime,
        end: OffsetDateTime? = null,
        repeat: Repeat? = null,
        partecipants: Set<EventPartecipation> = emptySet(),
        conferencing: URI? = null,
        description: String? = null,
        location: Contact.Address,
        busy: Boolean = true,
        private: Boolean = false,
        reminders: Set<Duration> = emptySet()
    ) : this(
        name = name,
        period = if (end.isNull()) start.withHour(0).withMinute(0).withSecond(0).withNano(0) intervalTo start.withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(1) else start intervalTo end,
        repeat = repeat,
        partecipants = partecipants,
        conferencing = conferencing,
        description = description,
        location = location.format(),
        busy = busy,
        private = private,
        reminders = reminders
    )

    init {
        validate(name.isNotBlank()) { "The name of the event cannot be blank." }
        period.start.expectClass(OffsetDateTime::class) { "The end date of the event must be OffsetDateTime instance." }
        period.end.expectClass(OffsetDateTime::class) { "The start date of the event must be OffsetDateTime instance." }
        validate((period.start as OffsetDateTime).isBefore(period.end as OffsetDateTime)) { "The start date of the event must be before the end date." }
        reminders.forEach { validate(it > Duration()) { "The reminder duration must be greater than zero." } }
    }

    companion object {
        class Serializer : ValueSerializer<CalendarEvent>() {
            override fun serialize(
                value: CalendarEvent,
                gen: tools.jackson.core.JsonGenerator,
                ctxt: SerializationContext
            ) {
                gen.writeStartObject()
                gen.writeStringProperty("name", value.name)
                gen.writeStringProperty("period", value.period.toString())
                gen.writePOJOProperty("repeat", value.repeat)
                gen.writeArrayPropertyStart("partecipants")
                value.partecipants.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeStringProperty("conferencing", value.conferencing?.toString())
                gen.writeStringProperty("description", value.description)
                gen.writeStringProperty("location", value.location)
                gen.writeBooleanProperty("busy", value.busy)
                gen.writeBooleanProperty("private", value.private)
                gen.writeArrayPropertyStart("reminders")
                value.reminders.forEach { gen.writePOJO(it) }
                gen.writeEndArray()
                gen.writeBooleanProperty("allDay", value.allDay)
                gen.writeEndObject()
            }
        }

        class Deserializer : ValueDeserializer<CalendarEvent>() {
            @Suppress("unchecked_cast")
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): CalendarEvent {
                val node = p.objectReadContext().readTree<ObjectNode>(p)
                return CalendarEvent(
                    name = node["name"].asString(),
                    period = TemporalInterval.parse(node["period"].asString()).getOrThrow(),
                    repeat = node["repeat"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), Repeat::class.java) },
                    partecipants = node["partecipants"]?.asSet<EventPartecipation>()?.getOrNull() ?: emptySet(),
                    conferencing = node["conferencing"]?.let { URI(it.asString()) },
                    description = node["description"]?.asString(),
                    location = node["location"]?.asString(),
                    busy = node["busy"].asBoolean(),
                    private = node["private"].asBoolean(),
                    reminders = node["reminders"]?.asSet<Duration>()?.getOrNull() ?: emptySet()
                )
            }
        }

        class OldSerializer : JsonSerializer<CalendarEvent>() {
            override fun serialize(value: CalendarEvent, gen: JsonGenerator, serializers: SerializerProvider) {
                gen.writeStartObject()
                gen.writeStringField("name", value.name)
                gen.writeStringField("period", value.period.toString())
                gen.writeObjectField("repeat", value.repeat)
                gen.writeFieldName("partecipants")
                gen.writeStartArray()
                value.partecipants.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeStringField("conferencing", value.conferencing?.toString())
                gen.writeStringField("description", value.description)
                gen.writeStringField("location", value.location)
                gen.writeBooleanField("busy", value.busy)
                gen.writeBooleanField("private", value.private)
                gen.writeFieldName("reminders")
                gen.writeStartArray()
                value.reminders.forEach { gen.writeObject(it) }
                gen.writeEndArray()
                gen.writeBooleanField("allDay", value.allDay)
                gen.writeEndObject()
            }
        }

        class OldDeserializer : JsonDeserializer<CalendarEvent>() {
            @Suppress("unchecked_cast")
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): CalendarEvent {
                val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                return CalendarEvent(
                    name = node["name"].asText(),
                    period = TemporalInterval.parse(node["period"].asText()).getOrThrow(),
                    repeat = node["repeat"]?.let { ctxt.readValue(it.traverse(p.codec), Repeat::class.java) },
                    partecipants = node["partecipants"]?.let { partecipantsNode ->
                        p.codec.treeToValue(partecipantsNode, Array<Any>::class.java)?.toSet()
                    } as Set<EventPartecipation>? ?: emptySet(),
                    conferencing = node["conferencing"]?.let { URI(it.asText()) },
                    description = node["description"]?.asText(),
                    location = node["location"]?.asText(),
                    busy = node["busy"].asBoolean(),
                    private = node["private"].asBoolean(),
                    reminders = node["reminders"]?.let { remindersNode ->
                        p.codec.treeToValue(remindersNode, Array<Duration>::class.java)?.toSet()
                    } ?: emptySet()
                )
            }
        }
    }

    /**
     * Resolves the value of a property by its name through reflection.
     *
     * This operator function is used to automatically delegate property access to a map
     * created from the object's properties. It retrieves the value associated with the
     * property name and casts it to the required type.
     *
     * @param thisRef The instance of the object the property belongs to. Not used directly in this implementation.
     * @param property The Kotlin property whose name will be used to fetch the value.
     * @return The value of the property, cast to the expected type.
     * @since 1.0.0
     */
    @Suppress("unchecked_cast")
    operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

    /**
     * Returns a string representation of the CalendarEvent object. The string includes all relevant
     * properties of the event, such as name, period, repeat, participants, conferencing status,
     * description, location, busy status, privacy, reminders, and whether it is an all-day event.
     *
     * @return A string representation of the CalendarEvent object.
     * @since 1.0.0
     */
    override fun toString(): String = "CalendarEvent(name='$name', period=$period, repeat=$repeat, partecipants=$partecipants, conferencing=$conferencing, description=$description, location=$location, busy=$busy, private=$private, reminders=$reminders, allDay=$allDay)"


    /**
     * Represents a repeat configuration for a calendar event.
     * Defines the recurrence pattern for the event, including the interval, the days of the week,
     * and an optional end condition for the repetition.
     *
     * @property every The duration specifying the interval for the recurring event.
     * @property weekDays The set of days of the week on which the event recurs.
     * @property endRepeat Optional condition specifying when the repetition should end.
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    @JsonSerialize(using = Repeat.Companion.Serializer::class)
    @JsonDeserialize(using = Repeat.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = Repeat.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = Repeat.Companion.OldDeserializer::class)
    data class Repeat(
        var every: Duration,
        var weekDays: Set<DayOfWeek>,
        var endRepeat: EndRepeat? = null,
    ) {
        init {
            validate(every > Duration()) { "The interval for the repeat rule must be greater than zero." }
            validate(weekDays.isNotEmpty()) { "The set of days of the week must not be empty." }
        }

        companion object {
            class Serializer : ValueSerializer<Repeat>() {
                override fun serialize(
                    value: Repeat,
                    gen: tools.jackson.core.JsonGenerator,
                    ctxt: SerializationContext
                ) {
                    gen.writeStartObject()
                    gen.writeStringProperty("every", value.every.toString())
                    gen.writeArrayPropertyStart("weekDays")
                    value.weekDays.forEach { gen.writeString(it.name) }
                    gen.writeEndArray()
                    gen.writePOJOProperty("endRepeat", value.endRepeat)
                    gen.writeEndObject()
                }
            }

            @Suppress("kutils_collection_declaration")
            class Deserializer : ValueDeserializer<Repeat>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): Repeat? {
                    val node = try {
                         p.objectReadContext().readTree<ObjectNode>(p)
                    } catch (e: ClassCastException) {
                        if (e.message?.startsWith("class tools.jackson.databind.node.NullNode cannot be cast to class tools.jackson.databind.node.ObjectNode") == true)
                            return null
                        else throw e
                    }
                    return Repeat(
                        every = Duration.parse(node["every"].asString()).getOrThrow(),
                        weekDays = node["weekDays"]?.asSet<String>()?.getOrNull()
                                ?.map(DayOfWeek::valueOf)
                                ?.toSet()
                            ?: emptySet(),
                        endRepeat = node["endRepeat"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), if ("-" in node["endRepeat"].asString()) EndRepeat.ByDate::class.java else EndRepeat.ByTimes::class.java) }
                    )
                }
            }

            class OldSerializer : JsonSerializer<Repeat>() {
                override fun serialize(value: Repeat, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeStringField("every", value.every.toString())
                    gen.writeFieldName("weekDays")
                    gen.writeStartArray()
                    value.weekDays.forEach { gen.writeString(it.name) }
                    gen.writeEndArray()
                    gen.writeObjectField("endRepeat", value.endRepeat)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<Repeat>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): Repeat? {
                    val node = try {
                        p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    } catch (e: ClassCastException) {
                        if (e.message?.startsWith("class com.fasterxml.jackson.databind.node.NullNode cannot be cast to class com.fasterxml.jackson.databind.node.ObjectNode") == true)
                            return null
                        else throw e
                    }
                    return Repeat(
                        every = Duration.parse(node["every"].asText()).getOrThrow(),
                        weekDays = node["weekDays"]?.let { weekDaysNode ->
                            p.codec.treeToValue(weekDaysNode, Array<String>::class.java)
                                ?.map(DayOfWeek::valueOf)
                                ?.toSet()
                        } ?: emptySet(),
                        endRepeat = node["endRepeat"]?.let { ctxt.readValue(it.traverse(p.codec), if ("-" in node["endRepeat"].asText()) EndRepeat.ByDate::class.java else EndRepeat.ByTimes::class.java) }
                    )
                }
            }
        }

        /**
         * Provides a delegate for retrieving a value from a property using reflection.
         *
         * @param thisRef The object instance from which the property is accessed. May be null.
         * @param property The property being accessed.
         * @return The value associated with the property name from the reflection map.
         * @since 1.0.0
         */
        @Suppress("unchecked_cast")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

        /**
         * Converts the `Repeat` instance into its string representation.
         * The string representation includes the values of the fields `every`, `weekDays`, and `endRepeat`.
         *
         * @return A string representation of the `Repeat` instance.
         * @since 1.0.0
         */
        override fun toString() = "Repeat(every=$every, weekDays=$weekDays, endRepeat=$endRepeat)"

        /**
         * Represents the termination condition for a repeating event or action.
         * This sealed class provides different ways to specify the end of repetition.
         * It can be extended with additional termination strategies if needed.
         *
         * @author Tommaso Pastorelli
         * @since 1.0.0
         */
        sealed interface EndRepeat {
            /**
             * Represents a specific end condition for a repetition that is determined by a given date.
             * This value class encapsulates a `LocalDate` instance which specifies the date the repetition ends.
             *
             * @property date The date that marks the end of the repetition.
             *
             * @author Tommaso Pastorelli
             * @since 1.0.0
             */
            @JsonSerialize(using = ByDate.Companion.Serializer::class)
            @JsonDeserialize(using = ByDate.Companion.Deserializer::class)
            @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ByDate.Companion.OldSerializer::class)
            @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ByDate.Companion.OldDeserializer::class)
            @JvmInline
            value class ByDate(val date: LocalDate) : EndRepeat {
                companion object {
                    class Serializer : ValueSerializer<ByDate>() {
                        override fun serialize(
                            value: ByDate,
                            gen: tools.jackson.core.JsonGenerator,
                            ctxt: SerializationContext
                        ) {
                            gen.writeString(value.date.toString())
                        }
                    }

                    class Deserializer : ValueDeserializer<ByDate>() {
                        override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): ByDate =
                            ByDate(dev.tommasop1804.kutils.LocalDate(p.objectReadContext().readValue(p, String::class.java))())
                    }

                    class OldSerializer : JsonSerializer<ByDate>() {
                        override fun serialize(value: ByDate, gen: JsonGenerator, serializers: SerializerProvider) {
                            gen.writeString(value.date.toString())
                        }
                    }

                    class OldDeserializer : JsonDeserializer<ByDate>() {
                        override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ByDate =
                            ByDate(LocalDate.parse(p.codec.readValue(p, String::class.java)))
                    }
                }
            }

            /**
             * Represents a recurrence rule that is repeated a specified number of times.
             *
             * This inline value class is used to encapsulate the number of occurrences
             * for a repeatable event within a scheduling or calendar system.
             *
             * @property times The number of times the recurring event should repeat.
             *
             * @author Tommaso Pastorelli
             * @since 1.0.0
             */
            @JsonSerialize(using = ByTimes.Companion.Serializer::class)
            @JsonDeserialize(using = ByTimes.Companion.Deserializer::class)
            @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = ByTimes.Companion.OldSerializer::class)
            @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = ByTimes.Companion.OldDeserializer::class)
            @JvmInline
            value class ByTimes(val times: Int) : EndRepeat {
                init {
                    validate(times > 0) { "The number of times must be greater than zero." }
                }

                companion object {
                    class Serializer : ValueSerializer<ByTimes>() {
                        override fun serialize(
                            value: ByTimes,
                            gen: tools.jackson.core.JsonGenerator,
                            ctxt: SerializationContext
                        ) {
                            gen.writeNumber(value.times)
                        }
                    }

                    class Deserializer : ValueDeserializer<ByTimes>() {
                        override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): ByTimes =
                            ByTimes(p.objectReadContext().readValue(p, Int::class.java))
                    }

                    class OldSerializer : JsonSerializer<ByTimes>() {
                        override fun serialize(value: ByTimes, gen: JsonGenerator, serializers: SerializerProvider) {
                            gen.writeNumber(value.times)
                        }
                    }

                    class OldDeserializer : JsonDeserializer<ByTimes>() {
                        override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): ByTimes =
                            ByTimes(p.codec.readValue(p, Int::class.java))
                    }
                }
            }
        }
    }

    /**
     * Represents the participation of a user in an event, including their status and an optional reason.
     *
     * This data class encapsulates information about a user's participation status and reasoning
     * (if any), allowing for serialization and deserialization of the data structure.
     *
     * @property user The contact information of the user participating in the event.
     * @property status The participation status of the user. This can be YES, NO, or MAYBE.
     * @property reason An optional reason explaining the user's participation status.
     * @author Tommaso Pastorelli
     * @since 1.0.0
     */
    @JsonSerialize(using = EventPartecipation.Companion.Serializer::class)
    @JsonDeserialize(using = EventPartecipation.Companion.Deserializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonSerialize(using = EventPartecipation.Companion.OldSerializer::class)
    @com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = EventPartecipation.Companion.OldDeserializer::class)
    data class EventPartecipation(
        val user: Contact,
        val status: PartecipationStatus?,
        val reason: String? = null
    ) {
        companion object {
            class Serializer : ValueSerializer<EventPartecipation>() {
                override fun serialize(
                    value: EventPartecipation,
                    gen: tools.jackson.core.JsonGenerator,
                    ctxt: SerializationContext
                ) {
                    gen.writeStartObject()
                    gen.writePOJOProperty("user", value.user)
                    gen.writeStringProperty("status", value.status?.name)
                    gen.writeStringProperty("reason", value.reason)
                    gen.writeEndObject()
                }
            }

            class Deserializer : ValueDeserializer<EventPartecipation>() {
                override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext): EventPartecipation {
                    val node = p.objectReadContext().readTree<ObjectNode>(p)
                    return EventPartecipation(
                        user = ctxt.readValue(node["user"].traverse(p.objectReadContext()), Contact::class.java),
                        status = node["status"]?.let { ctxt.readValue(it.traverse(p.objectReadContext()), PartecipationStatus::class.java) },
                        reason = node["reason"]?.asString()
                    )
                }
            }

            class OldSerializer : JsonSerializer<EventPartecipation>() {
                override fun serialize(value: EventPartecipation, gen: JsonGenerator, serializers: SerializerProvider) {
                    gen.writeStartObject()
                    gen.writeObjectField("user", value.user)
                    gen.writeStringField("status", value.status?.name)
                    gen.writeStringField("reason", value.reason)
                    gen.writeEndObject()
                }
            }

            class OldDeserializer : JsonDeserializer<EventPartecipation>() {
                override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): EventPartecipation {
                    val node = p.codec.readTree<com.fasterxml.jackson.databind.node.ObjectNode>(p)
                    return EventPartecipation(
                        user = ctxt.readValue(node["user"].traverse(p.codec), Contact::class.java),
                        status = node["status"]?.let { ctxt.readValue(it.traverse(p.codec), PartecipationStatus::class.java) },
                        reason = node["reason"]?.asText()
                    )
                }
            }
        }
        
        /**
         * Retrieves the value of a property associated with the specified key name from an internal reflection-based map.
         *
         * @param thisRef The object instance on which the property is accessed, can be null.
         * @param property The property metadata used to obtain the corresponding value from the map.
         * @return The value of the specified property cast to the expected type.
         * @since 1.0.0
         */
        @Suppress("unchecked_cast")
        operator fun <R> getValue(thisRef: Any?, property: KProperty<*>) = toReflectionMap().getValue(property.name) as R

        /**
         * Converts the `EventPartecipation` instance into a string representation,
         * including its user, status, and reason properties.
         *
         * @return A string representation of the `EventPartecipation` instance.
         * @since 1.0.0
         */
        override fun toString(): String = "EventPartecipation(user=$user, status=$status, reason=$reason)"

        /**
         * Represents the participation status of a user for an event along with a default color
         * indicating the status visually.
         *
         * Each status is associated with a specific color for better recognition:
         * - YES: Represented by the color GREEN.
         * - NO: Represented by the color RED.
         * - MAYBE: Represented by the color LIGHT_ORANGE.
         *
         * @property defaultColor The default color associated with the participation status.
         * @since 1.0.0
         */
        enum class PartecipationStatus(val defaultColor: Color) {
            /**
             * Represents a status indicating affirmative participation.
             *
             * This status is typically associated with the color green,
             * signifying agreement or positive confirmation.
             *
             * @since 1.0.0
             */
            YES(Color.GREEN), 
            /**
             * Represents a "No" participation status with a default color of red.
             * This status indicates a negative response in the context of participation.
             *
             * @param defaultColor The default color associated with the "No" status.
             * @since 1.0.0
             */
            NO(Color.RED), 
            /**
             * Represents a participation status option where the answer is uncertain.
             *
             * This status corresponds to a "maybe" response in the context of participation or decision-making scenarios.
             * It is associated with the color LIGHT_ORANGE as its default visual representation.
             *
             * @param defaultColor The default color representation of the "MAYBE" status.
             * @since 1.0.0
             */
            MAYBE(Color.LIGHT_ORANGE)
        }
    }
}
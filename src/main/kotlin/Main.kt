
import dev.tommasop1804.kutils.classes.coding.Json.Companion.toPrettyJson
import dev.tommasop1804.kutils.classes.constants.SortDirection
import dev.tommasop1804.kutils.classes.pagination.Chunked
import dev.tommasop1804.kutils.classes.pagination.FilterOperator
import dev.tommasop1804.kutils.classes.pagination.FilterOption
import dev.tommasop1804.kutils.classes.pagination.SortOption
import dev.tommasop1804.kutils.classes.registry.Contact
import dev.tommasop1804.kutils.println

fun main() {
    Chunked(
        4,
        0,
        33,
        2,
        listOf(
            FilterOption(
                field = "id",
                operator = FilterOperator.CONTAINS,
                value = 10
            ),
            FilterOption(
                field = Contact::name,
                operator = FilterOperator.IN,
                value = "ciao"
            ),
        ),
        listOf(
            SortOption(
                field = Contact::name,
                direction = SortDirection.ASCENDING
            ),
            SortOption(
                field = Contact::otherFields,
                direction = SortDirection.DESCENDING
            )
        ),
        listOf(1, 2, 3, "è")
    ).toPrettyJson().println()
}
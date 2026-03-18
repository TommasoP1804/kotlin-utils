package dev.tommasop1804.kutils.classes.web

/**
 * Represents the different versions of the HTTP protocol supported in the application.
 *
 * This enum is used to specify and differentiate between various versions of HTTP,
 * providing flexibility for protocols during client-server communication.
 *
 * @since 3.0.0
 * @property notation The display name of the HTTP version.
 * @property version The version number of the HTTP protocol.
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
enum class HttpVersion(val notation: String, val version: Double) {
    /**
     * Represents the HTTP/1.0 protocol version.
     *
     * HTTP/1.0 is the first version of the HTTP protocol that was widely deployed.
     * It introduced features such as basic request-response communication.
     *
     * @since 3.0.0
     */
    HTTP_1_0("HTTP/1.0", 1.0),
    /**
     * Represents the HTTP/1.1 protocol version.
     *
     * HTTP/1.1 is a widely adopted version of the HTTP protocol, introducing key improvements
     * over its predecessor HTTP/1.0, such as persistent connections, chunked transfer encoding,
     * and additional cache control mechanisms.
     *
     * @since 3.0.0
     */
    HTTP_1_1("HTTP/1.1", 1.1),
    /**
     * Represents the HTTP/2 protocol version, which improves upon its predecessor HTTP/1.x by 
     * offering features such as multiplexing, header compression, and binary transmission for
     * better performance and efficiency in web communication.
     *
     * @since 3.0.0
     */
    HTTP_2("HTTP/2", 2.0),
    /**
     * Represents the HTTP/3 protocol version in the HTTP protocol suite.
     *
     * HTTP/3 is the third major version of the Hypertext Transfer Protocol (HTTP). 
     * It is based on the QUIC transport protocol to improve performance and reduce latency 
     * compared to its predecessors, HTTP/1.x and HTTP/2.
     *
     * @since 3.0.0
     */
    HTTP_3("HTTP/3", 3.0);
    
    companion object {
        /**
         * Searches for an entry in the collection that matches the given notation.
         *
         * @param notation The string representation of the entry's notation to search for.
         * @return The matching entry, or null if no match is found.
         * @since 3.0.0
         */
        infix fun of(notation: String) = entries.find { it.notation == notation }
        /**
         * Finds an entry in the collection where the version matches the provided value.
         *
         * @param version The version number to be searched for in the entries.
         * @return The entry with the matching version, or `null` if no such entry exists.
         * @since 3.0.0
         */
        infix fun of(version: Number) = entries.find { it.version == version }
    }

    /**
     * Returns the `notation` value of the current instance.
     * This function enables destructuring declarations to extract `notation`
     * as the first component of the `HttpVersion` class.
     *
     * @return the `notation` field of the `HttpVersion` instance.
     * @since 3.1.0
     */
    operator fun component1() = notation
    /**
     * Retrieves the second component of the HttpVersion instance.
     * 
     * This method returns the `version` property of the HttpVersion class,
     * enabling usage in destructuring declarations.
     *
     * @return the `version` value of the current HttpVersion instance
     * @since 3.1.0
     */
    operator fun component2() = version 
}
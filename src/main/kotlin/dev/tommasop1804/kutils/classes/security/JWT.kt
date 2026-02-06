@file:Suppress("kutils_collection_declaration")

package dev.tommasop1804.kutils.classes.security

import com.auth0.jwt.JWTCreator
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import dev.tommasop1804.kutils.*
import dev.tommasop1804.kutils.classes.coding.JSON
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.MAPPER
import dev.tommasop1804.kutils.classes.coding.JSON.Companion.toJSON
import dev.tommasop1804.kutils.classes.registry.Contact.Email.Companion.toEmail
import dev.tommasop1804.kutils.classes.time.Duration
import dev.tommasop1804.kutils.classes.web.HttpMethod
import dev.tommasop1804.kutils.exceptions.HttpResponseException
import dev.tommasop1804.kutils.exceptions.MalformedInputException
import dev.tommasop1804.kutils.exceptions.RequiredFieldException
import jakarta.persistence.AttributeConverter
import tools.jackson.databind.*
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.time.Instant
import java.util.*
import com.auth0.jwt.algorithms.Algorithm as Auth0JwtAlgorithm

/**
 * Represents a JSON Web Token (JWT) implementation, allowing parsing and validation of JWTs as well as
 * generation of signed tokens using various algorithms.
 *
 * @constructor Creates a JWT object by parsing a JWT string. 
 * @param value The raw string representation of the JWT.
 * @throws MalformedInputException If the input is not a valid three-part JWT.
 * @since 1.0.0
 */
@JsonSerialize(using = JWT.Companion.Serializer::class)
@JsonDeserialize(using = JWT.Companion.Deserializer::class)
@com.fasterxml.jackson.databind.annotation.JsonSerialize(using = JWT.Companion.OldSerializer::class)
@com.fasterxml.jackson.databind.annotation.JsonDeserialize(using = JWT.Companion.OldDeserializer::class)
@Suppress("unused", "UNCHECKED_CAST", "PropertyName")
class JWT private constructor(private val value: String) : CharSequence {
    /**
     * Represents the decoded JSON object of the JWT header.
     * The header typically contains metadata about the token, such as the signing algorithm
     * and token type.
     *
     * @since 1.0.0
     */
    val header: DataMap

    /**
     * Represents the payload section of a JSON Web Token (JWT).
     * The payload contains claims, which are statements about
     * an entity (typically, the user) and additional metadata.
     *
     * This property holds the claims in JSON format, allowing
     * further verification and processing.
     *
     * @since 1.0.0
     */
    val payload: DataMap

    /**
     * Represents the cryptographic signature component of a JSON Web Token (JWT).
     * The signature is used to validate the authenticity and integrity of the token.
     *
     * @since 1.0.0
     */
    val signature: String

    /**
     * Represents the length of the value.
     * This property retrieves the number of characters in the associated value.
     *
     * @return The length of the value as an integer.
     * @since 1.0.0
     */
    override val length: Int get() = value.length

    // -- COMPUTED --

    /**
     * [RFC 7515#4.1.1](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.1)
     *
     * @since 1.0.0
     */
    val hd_algorithm get() = (header["alg"] as String).toEnumConst<Algorithm>()
    /**
     * [RFC 7515#4.1.2](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.2)
     *
     * @since 1.0.0
     */
    val hd_jwkSetURL get() = (header["jku"] as? String)?.toURI()?.invoke()
    /**
     * [RFC 7515#4.1.3](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.3)
     *
     * @since 1.0.0
     */
    val hd_jsonWebKey get() = header["jwk"] as? String
    /**
     * [RFC 7515#4.1.4](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.4)
     *
     * @since 1.0.0
     */
    val hd_keyId get() = header["kid"] as? String
    /**
     * [RFC 7515#4.1.5](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.5)
     *
     * @since 1.0.0
     */
    val hd_x509URL get() = (header["x5u"] as? String)?.toURI()?.invoke()
    /**
     * [RFC 7515#4.1.6](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.6)
     *
     * @since 1.0.0
     */
    val hd_x509CertificateChain get() = tryOr({ JSON(header["x5c"] as String).toList<String>()() }) { header["x5c"] as? StringList }
    /**
     * [RFC 7515#4.1.7](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.7)
     *
     * @since 1.0.0
     */
    val hd_x509CertificateSHA1Thumbripnt get() = header["x5t"] as? String
    /**
     * [RFC 7515#4.1.8](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.8)
     *
     * @since 1.0.0
     */
    val hd_x509CertificateSHA256Thumbripnt get() = header["x5t#S256"] as? String
    /**
     * [RFC 7515#4.1.9](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.9)
     *
     * @since 1.0.0
     */
    val hd_type get() = header["typ"] as? String
    /**
     * [RFC 7515#4.1.10](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.1)
     *
     * @since 1.0.0
     */
    val hd_contentType get() = header["cty"] as? String
    /**
     * [RFC 7515#4.1.11](https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.11)
     *
     * @since 1.0.0
     */
    val hd_critical get() = tryOr({ JSON(header["crit"] as String).toList<String>()() }) { header["crit"] as? StringList }

    /**
     * [RFC 7519#4.1.1](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.1)
     *
     * @since 1.0.0
     */
    val pl_issuer get() = payload["iss"] as? String
    /**
     * [RFC 7519#4.1.2](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.2)
     *
     * @since 1.0.0
     */
    val pl_subject get() = payload["sub"] as? String
    /**
     * [RFC 7519#4.1.3](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.3)
     *
     * @since 1.0.0
     */
    val pl_audience get() = payload["aud"] as? String
    /**
     * [RFC 7519#4.1.4](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.4)
     *
     * @since 1.0.0
     */
    val pl_expiration get() = (payload["exp"] as? Number)?.toString()?.then { if (length == 10) Instant(epochSeconds = toLong()) else Instant(epochMilliseconds = toLong()) }
    /**
     * [RFC 7519#4.1.5](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.5)
     *
     * @since 1.0.0
     */
    val pl_notBefore get() = (payload["nbf"] as? Number)?.toString()?.then { if (length == 10) Instant(epochSeconds = toLong()) else Instant(epochMilliseconds = toLong()) }
    /**
     * [RFC 7519#4.1.6](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.6)
     *
     * @since 1.0.0
     */
    val pl_issuedAt get() = (payload["iat"] as? Number)?.toString()?.then { if (length == 10) Instant(epochSeconds = toLong()) else Instant(epochMilliseconds = toLong()) }
    /**
     * [RFC 7519#4.1.7](https://datatracker.ietf.org/doc/html/rfc7519#section-4.1.7)
     *
     * @since 1.0.0
     */
    val pl_jwtId get() = payload["jti"] as? String

    /**
     * [OPENID Connect Core 1.0#2](https://openid.net/specs/openid-connect-core-1_0-final.html#IDToken)
     *
     * @since 1.0.0
     */
    val pl_authTime get() = (payload["auth_time"] as? Number)?.toString()?.then { if (length == 10) Instant(epochSeconds = toLong()) else Instant(epochMilliseconds = toLong()) }
    /**
     * [OPENID Connect Core 1.0#2](https://openid.net/specs/openid-connect-core-1_0-final.html#IDToken)
     *
     * @since 1.0.0
     */
    val pl_nonce get() = payload["nonce"] as? String
    /**
     * [OPENID Connect Core 1.0#2](https://openid.net/specs/openid-connect-core-1_0-final.html#IDToken)
     *
     * @since 1.0.0
     */
    val pl_authenticationContextClassReference get() = payload["acr"] as? String
    /**
     * [OPENID Connect Core 1.0#2](https://openid.net/specs/openid-connect-core-1_0-final.html#IDToken)
     *
     * @since 1.0.0
     */
    val pl_authenticationMethodsReferences get() = payload["amr"] as? String
    /**
     * [OPENID Connect Core 1.0#2](https://openid.net/specs/openid-connect-core-1_0-final.html#IDToken)
     *
     * @since 1.0.0
     */
    val pl_authorizedParty get() = payload["azp"] as? String

    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_realmAccess get() = payload["realm_access"] as? DataMap
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_realmAccessRoles get() = pl_kc_realmAccess?.get("roles") as? StringList
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_scope get() = payload["scope"] as? String
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_emailVerified get() = payload["email_verified"] as? Boolean
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_name get() = payload["name"] as? String
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_preferredUsername get() = payload["preferred_username"] as? String
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_givenName get() = payload["given_name"] as? String
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_familyName get() = payload["family_name"] as? String
    /**
     * KeyCloak claim.
     *
     * DO NOT SEARCH IF YOU ARE NOT USING KEYCLOAK.
     *
     * @since 1.0.0
     */
    val pl_kc_email get() = (payload["email"] as? String)?.toEmail()?.invoke()

    /**
     * Provides access to an RSA public key for a specified key ID (`hd_keyId`) from a remote OpenID Connect discovery
     * document and JWKS endpoint. This variable fetches and parses the required data over HTTP to generate an RSA public key.
     *
     * The operation involves:
     * - Fetching the `.well-known/openid-configuration` document from the issuer's URL (`pl_issuer`).
     * - Retrieving the `jwks_uri` from the discovery document.
     * - Fetching the JSON Web Key Set (JWKS) from the provided URI.
     * - Locating the specified key using the `kid` field.
     * - Constructing the RSA public key using the modulus (`n`) and exponent (`e`) values from the key data.
     *
     * The process uses lazy evaluation and handles exceptions to return a result encapsulated in a Kotlin `Result`.
     *
     * Exceptions that may be raised during evaluation include:
     * - **RequiredFieldException**: Thrown if the `pl_issuer` field is null or blank.
     * - **HttpResponseException**: Thrown if a non-200 HTTP response is returned while fetching remote resources.
     * - **NoSuchElementException**: Thrown if no key matching the specified key ID (`hd_keyId`) is found in the JWKS.
     *
     * @return A `Result` containing the generated RSA public key or an exception if the operation fails.
     * @since 1.0.0
     */
    @Suppress("RedundantLabeledReturnOnLastExpressionInLambda")
    val RSAPublicKey: Result<RSAPublicKey>
        get() = runCatching {
            if (pl_issuer.isNullOrBlank()) throw RequiredFieldException("issuer")
            val client = HttpClient.newHttpClient()

            fun fetchJSON(url: String): JsonNode {
                val uri = URI.create(url)
                val request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build()
                val response = client.send(request, HttpResponse.BodyHandlers.ofString())
                if (response.statusCode() != 200) throw HttpResponseException(response.statusCode(), uri, HttpMethod.GET)
                return MAPPER.readTree(response.body())
            }

            val discoveryURL = (if (pl_issuer!!.endsWith(Char.SLASH)) pl_issuer else ("$pl_issuer/"))
                .plus(".well-known/openid-configuration")

            val tree = fetchJSON(discoveryURL)
            val jwksUri = tree["jwks_uri"].asString()
            val jwks = fetchJSON(jwksUri)
            val keys = jwks["keys"]
            if (keys.isArray) {
                for (key in keys) {
                    if (key.has("kid") && key.get("kid").asString() == hd_keyId) {
                        val n = key["n"].asString()
                        val e = key["e"].asString()
                        val nBytes = Base64.getUrlDecoder().decode(n)
                        val eBytes = Base64.getUrlDecoder().decode(e)
                        val nBigInt = BigInt(1, nBytes)
                        val eBigInt = BigInt(1, eBytes)
                        val spec = RSAPublicKeySpec(nBigInt, eBigInt)
                        val keyFactory = KeyFactory.getInstance("RSA")
                        return@runCatching keyFactory.generatePublic(spec) as RSAPublicKey
                    }
                }
            }
            throw NoSuchElementException("No corrispondent key found.")
        }

    /**
     * Initializes a new `JWT` instance by parsing the provided JWT string.
     *
     * This constructor attempts to strip the "Bearer " prefix, if present, from the
     * input JWT string. After prefix removal, it trims any leading or trailing whitespace
     * and passes the processed string to the primary constructor of the `JWT` class.
     *
     * @param jwt The character sequence representing the JSON Web Token, which may
     *            optionally include the "Bearer " prefix.
     * @since 1.0.0
     */
    constructor(jwt: CharSequence) : this((jwt.toString() - "Bearer ").trim())

    init {
        val parts = value.split(Char.DOT)
        if (parts.size != 3) throw MalformedInputException("Invalid JWT")

        header = String(Base64.getUrlDecoder().decode(parts[0])).toJSON()().toDataMap()()
        payload = String(Base64.getUrlDecoder().decode(parts[1])).toJSON()().toDataMap()()
        signature = parts[2]
    }

    companion object {

        /**
         * Checks if the current character sequence is a valid JSON Web Token (JWT).
         *
         * This method attempts to parse the character sequence as a JWT using the `JWT` constructor.
         * If the parsing succeeds, the method returns `true`; otherwise, it returns `false`.
         *
         * @receiver The character sequence to validate as a JWT.
         * @return `true` if the character sequence represents a valid JWT; `false` otherwise.
         * @since 1.0.0
         */
        fun CharSequence.isValidJWT() = runCatching { JWT(this) }.isSuccess

        /**
         * Converts the current [CharSequence] into a JWT object by attempting to parse it.
         *
         * This method uses the provided string representation of a JSON Web Token (JWT)
         * to create an instance of the `JWT` class.
         * 
         * The process wraps the creation of the `JWT` object in a `Result` to capture any 
         * potential parsing or initialization errors without throwing an exception.
         *
         * @receiver The [CharSequence] to be converted into a JWT object.
         * @return A [Result] containing the initialized `JWT` object if successful, or
         *         an error if the input is invalid or the parsing fails.
         * @since 1.0.0
         */
        fun CharSequence.toJWT() = runCatching { JWT(this) }

        /**
         * Generates a JWT (JSON Web Token) using a given payload and an optional expiration time.
         * 
         * Iterates over the payload and sets claims in the token for supported data types. If the 
         * `expiresTime` is provided, the token will include an expiration claim.
         *
         * @param payload The data to be used as claims in the JWT. Each key-value pair represents a claim.
         * @param expiresTime Optional duration until the token expires. If not provided, the generated 
         *                     token will not have an expiration claim.
         * @return A builder object for further customization of the JWT.
         * @since 1.0.0
         */
        @OptIn(RiskyApproximationOfTemporal::class)
        @Suppress("KotlinConstantConditions", "UNCHECKED_CAST", "FunctionName")
        private fun _generateJWT(payload: DataMap, expiresTime: Duration? = null): JWTCreator.Builder {
            val builder = com.auth0.jwt.JWT.create()
            for ((key, value) in payload) {
                when (value) {
                    isNull() -> builder.withClaim(key, value as? String)
                    is Boolean -> builder.withClaim(key, value)
                    is Int -> builder.withClaim(key, value)
                    is Long -> builder.withClaim(key, value)
                    is Double -> builder.withClaim(key, value)
                    is String -> builder.withClaim(key, value)
                    is Date -> builder.withClaim(key, value)
                    is Instant -> builder.withClaim(key, value)
                    is Map<*, *> -> tryOrThrow({ -> IllegalArgumentException("Not a valid claim format.") }, includeCause = false) {
                        builder.withClaim(key, value as DataMap)
                    }
                    is List<*> -> builder.withClaim(key, value)
                    else -> throw IllegalArgumentException("Not a valid claim format.")
                }
            }
            expiresTime?.then { builder.withExpiresAt(Date(System.currentTimeMillis() + toMillis().toLong())) }

            return builder
        }

        /**
         * Generates an HMAC256-signed JSON Web Token (JWT) using the given payload and secret.
         * 
         * This method constructs a JWT with the provided payload and signs it using the HMAC256 algorithm
         * and the specified secret key.
         *
         * @param payload A map containing key-value pairs to include as claims in the payload of the JWT.
         * @param secret A secret key used for signing the JWT with the HMAC256 algorithm.
         * @return A signed JWT instance.
         * @since 1.0.0
         */
        fun generateHMAC256(payload: DataMap, secret: String) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.HMAC256(secret))!!)
        /**
         * Generates a signed HMAC256 JSON Web Token (JWT) based on the given payload and secret key.
         *
         * @param payload The JSON payload to be included in the token, which will be converted to a data map.
         * @param secret The secret key used to generate the HMAC256 signature for the token.
         * @since 1.0.0
         */
        fun generateHMAC256(payload: JSON, secret: String) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.HMAC256(secret))!!)
        /**
         * Generates an HMAC384-signed JSON Web Token (JWT) using the provided payload and secret key.
         *
         * @param payload A `DataMap` containing the claims to be included in the JWT payload.
         * Each key-value pair in the map represents a claim.
         * @param secret A `String` representing the secret key used for HMAC384 signing.
         * This key is essential for ensuring the integrity and authenticity of the token.
         * @return A signed JWT encapsulated in a `JWT` object.
         * @since 1.0.0
         */
        fun generateHMAC384(payload: DataMap, secret: String) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.HMAC384(secret))!!)
        /**
         * Generates a JWT signed using the HMAC384 algorithm.
         *
         * @param payload the JSON payload object to be encoded and signed.
         * @param secret the secret key used for generating the HMAC384 signature.
         * @since 1.0.0
         */
        fun generateHMAC384(payload: JSON, secret: String) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.HMAC384(secret))!!)
        /**
         * Generates an HMAC-512 signed JSON Web Token (JWT) based on the provided payload and secret.
         * This method ensures the payload is securely signed using the HMAC-512 algorithm.
         *
         * @param payload A `DataMap` representing the claims or data to be included in the JWT.
         *                The payload can contain various types like strings, numbers, booleans, dates, and more.
         * @param secret A secret key used to sign the JWT using the HMAC-512 algorithm.
         *               It is recommended to use a secure and high-entropy key to maintain the integrity of the token.
         * @since 1.0.0
         */
        fun generateHMAC512(payload: DataMap, secret: String) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.HMAC512(secret))!!)
        /**
         * Generates a JWT token signed using the HMAC512 algorithm.
         *
         * @param payload The JSON object containing the claims to be included in the JWT payload.
         * @param secret The secret key used to sign the JWT using the HMAC512 algorithm.
         * @since 1.0.0
         */
        fun generateHMAC512(payload: JSON, secret: String) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.HMAC512(secret))!!)
        /**
         * Generates a signed RSA256 JWT (JSON Web Token) using the provided payload and private key.
         *
         * The method utilizes the RSA256 algorithm to sign the payload and constructs a JWT object.
         *
         * @param payload The key-value data map to be included as claims within the JWT.
         * @param privateKey The RSA private key used to generate the signature for the JWT.
         * @return A signed JWT object containing the provided claims.
         * @since 1.0.0
         */
        fun generateRSA256(payload: DataMap, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.RSA256(privateKey))!!)
        /**
         * Generates a JWT (JSON Web Token) signed using the RSA256 algorithm.
         *
         * @param payload The JSON payload to be included in the token. 
         * It must be converted to a data map before processing.
         * @param privateKey The RSA private key used to sign the token.
         * @return A signed JWT object.
         * @since 1.0.0
         */
        fun generateRSA256(payload: JSON, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.RSA256(privateKey))!!)
        /**
         * Generates a JWT signed with the RSA384 algorithm based on the given payload and private key.
         *
         * @param payload A `DataMap` containing the claims to be included in the JWT payload. Keys must be strings, 
         *   and values must conform to accepted claim formats such as String, Boolean, Int, Long, Double, Date, Instant, Map, or List.
         * @param privateKey The RSA private key used to sign the JWT using the RSA384 algorithm.
         * @return A `JWT` object representing the generated token.
         * @since 1.0.0
         */
        fun generateRSA384(payload: DataMap, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.RSA384(privateKey))!!)
        /**
         * Generates an RSA384 signed JSON Web Token (JWT) using the provided payload and private key.
         *
         * @param payload The JSON object containing the claims to include in the JWT.
         * @param privateKey The RSA private key used to sign the JWT with the RSA384 algorithm.
         * @since 1.0.0
         */
        fun generateRSA384(payload: JSON, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.RSA384(privateKey))!!)
        /**
         * Generates a signed RSA-512 JSON Web Token (JWT) based on the provided payload and private key.
         * The payload data is included as claims in the JWT, and the token is signed using the RSA-512 algorithm.
         *
         * @param payload Represents the key-value pairs to include as claims in the JWT.
         * @param privateKey The RSA private key used to sign the JWT.
         * @since 1.0.0
         */
        fun generateRSA512(payload: DataMap, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.RSA512(privateKey))!!)
        /**
         * Generates a JWT (JSON Web Token) using the RSA-512 signing algorithm.
         * Takes a JSON payload and an RSA private key as input, 
         * and produces a signed token.
         *
         * @param payload The JSON payload to be included in the JWT.
         * @param privateKey The RSA private key used to sign the token.
         * @since 1.0.0
         */
        fun generateRSA512(payload: JSON, privateKey: RSAPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.RSA512(privateKey))!!)
        /**
         * Generates an ECDSA256 signed JSON Web Token (JWT) using the provided payload and private key.
         *
         * This method constructs a JWT based on the supplied payload, signs it using the ECDSA256 
         * algorithm with the provided EC private key, and returns the resulting JWT instance.
         *
         * @param payload The data to be included as claims in the JWT. Each key-value pair in the payload
         *                represents a claim to be included in the token.
         * @param privateKey The EC (Elliptic Curve) private key used to sign the JWT using the ECDSA256 
         *                   algorithm.
         * @since 1.0.0
         */
        fun generateECDSA256(payload: DataMap, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.ECDSA256(privateKey))!!)
        /**
         * Generates a JSON Web Token (JWT) using the ECDSA-256 (Elliptic Curve Digital Signature Algorithm) algorithm.
         * The method signs the provided payload with the specified private key.
         *
         * @param payload The JSON payload to include in the JWT. This should contain the data to be encoded.
         * @param privateKey The elliptic curve private key used to sign the JWT.
         * @return The generated JWT as a string.
         * @since 1.0.0
         */
        fun generateECDSA256(payload: JSON, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.ECDSA256(privateKey))!!)
        /**
         * Generates a JSON Web Token (JWT) using the ECDSA384 cryptographic algorithm.
         * This method signs a token created from the provided payload using the specified EC private key.
         *
         * @param payload A map containing the data to be included in the JWT payload. The map's keys represent
         *                claim names and its values represent claim values.
         * @param privateKey The EC private key used to sign the JWT.
         * @return A signed JWT instance.
         * @since 1.0.0
         */
        fun generateECDSA384(payload: DataMap, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.ECDSA384(privateKey))!!)
        /**
         * Generates a JSON Web Token (JWT) using the ECDSA algorithm with a 384-bit key.
         *
         * The method takes a payload in JSON format and an elliptic curve private key 
         * to generate and sign the JWT. The resulting token is compatible with the 
         * Auth0 ECDSA384 signing algorithm.
         *
         * @param payload The JSON object to include in the payload section of the token.
         * @param privateKey The elliptic curve private key used for signing the JWT.
         * @since 1.0.0
         */
        fun generateECDSA384(payload: JSON, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.ECDSA384(privateKey))!!)
        /**
         * Generates a JWT (JSON Web Token) signed using the ECDSA-512 algorithm.
         *
         * @param payload The data to include in the payload of the JWT. This is a map of key-value pairs,
         * where the key is a String and the value can be various types, such as Boolean, Int, String, or Date.
         * @param privateKey The ECPrivateKey used to sign the JWT using the ECDSA-512 algorithm.
         * @return A signed JWT as an instance of the JWT class.
         * @since 1.0.0
         */
        fun generateECDSA512(payload: DataMap, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload).sign(Auth0JwtAlgorithm.ECDSA512(privateKey))!!)
        /**
         * Generates a signed JSON Web Token (JWT) using the ECDSA algorithm with a 512-bit key.
         *
         * @param payload The JSON object containing the payload data for the JWT.
         * @param privateKey The EC private key used to sign the JWT.
         * @since 1.0.0
         */
        fun generateECDSA512(payload: JSON, privateKey: ECPrivateKey) =
            JWT(_generateJWT(payload.toDataMap()()).sign(Auth0JwtAlgorithm.ECDSA512(privateKey))!!)

        class Serializer : ValueSerializer<JWT>() {
            override fun serialize(value: JWT, gen: tools.jackson.core.JsonGenerator, ctxt: SerializationContext) {
                gen.writeString(value.toString())
            }
        }

        class Deserializer : ValueDeserializer<JWT>() {
            override fun deserialize(p: tools.jackson.core.JsonParser, ctxt: DeserializationContext) = JWT(p.string)
        }

        class OldSerializer : JsonSerializer<JWT>() {
            override fun serialize(value: JWT, gen: JsonGenerator, serializers: SerializerProvider) =
                gen.writeString(value.toString())
        }

        class OldDeserializer : JsonDeserializer<JWT>() {
            override fun deserialize(p: JsonParser, ctxt: com.fasterxml.jackson.databind.DeserializationContext): JWT = JWT(p.text)
        }

        @jakarta.persistence.Converter(autoApply = true)
        class Converter : AttributeConverter<JWT?, String?> {
            override fun convertToDatabaseColumn(attribute: JWT?): String? = attribute?.toString()
            override fun convertToEntityAttribute(dbData: String?): JWT? = dbData?.let { JWT(it) }
        }
    }

    /**
     * Returns a string representation of the current JWT instance, optionally including
     * the "Bearer " prefix to adhere to the common authorization header format.
     *
     * @param bearerPrefix Specifies whether the returned string should include the "Bearer " prefix.
     * If true, the returned string starts with "Bearer "; otherwise, it does not include the prefix.
     * @since 1.0.0
     */
    fun toString(bearerPrefix: Boolean) = (if (bearerPrefix) "Bearer " else String.EMPTY) + value

    /**
     * Returns a string representation of the object. The exact format may vary
     * depending on the implementation but generally provides a meaningful and 
     * human-readable representation of the object's value.
     *
     * @return the string representation of the object
     * @since 1.0.0
     */
    override fun toString() = value

    /**
     * Retrieves the character at the specified index from the value.
     *
     * @param index The position of the character to retrieve.
     * @return The character at the specified index.
     * @since 1.0.0
     */
    override fun get(index: Int): Char = value[index]

    /**
     * Returns a new character sequence that is a subsequence of this sequence.
     *
     * @param startIndex the start index of the subsequence, inclusive.
     * @param endIndex the end index of the subsequence, exclusive.
     * @return a CharSequence representing the subsequence defined by the specified range.
     * @since 1.0.0
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence = value.subSequence(startIndex, endIndex)

    /**
     * Verifies a JWT token using the specified algorithm and returns a boolean
     * indicating whether the verification was successful.
     *
     * This method uses the Auth0 JWT library to validate the token's signature
     * against the provided algorithm.
     *
     * @param token The JWT token to be verified.
     * @param algorithm The algorithm object representing the cryptographic 
     * method used to sign the token.
     * @since 1.0.0
     */
    @Suppress("functionName")
    private fun _verify(token: String, algorithm: Auth0JwtAlgorithm) =
        tryTrueOrFalse { com.auth0.jwt.JWT.require(algorithm).build().verify(token) }

    /**
     * Verifies the RSA256 signature of the JWT.
     *
     * This method uses the provided RSA public and private keys to validate the integrity 
     * and authenticity of the JSON Web Token (JWT) by applying the RSA256 algorithm.
     *
     * @param publicKey The RSA public key used for verification.
     * @param privateKey The RSA private key paired with the public key.
     * @since 1.0.0
     */
    fun verifyRSA256(publicKey: RSAPublicKey, privateKey: RSAPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.RSA256(publicKey, privateKey))
    /**
     * Verifies the JWT token using the RSA-384 algorithm.
     *
     * This method utilizes the provided RSA public and private key pair
     * to verify the authenticity and integrity of the token.
     *
     * @param publicKey The RSA public key used for verification.
     * @param privateKey The RSA private key used for verification.
     * @since 1.0.0
     */
    fun verifyRSA384(publicKey: RSAPublicKey, privateKey: RSAPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.RSA384(publicKey, privateKey))
    /**
     * Verifies the integrity and authenticity of the JWT using the RSA512 algorithm.
     *
     * This method utilizes the provided RSAPublicKey and RSAPrivateKey to perform the RSA512 verification.
     *
     * @param publicKey The RSA public key used to verify the token's signature.
     * @param privateKey The RSA private key associated with the token for the verification process.
     * @since 1.0.0
     */
    fun verifyRSA512(publicKey: RSAPublicKey, privateKey: RSAPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.RSA512(publicKey, privateKey))
    /**
     * Verifies the HMAC256 signature of the current JWT instance using the provided secret.
     *
     * @param secret The secret key used to verify the HMAC256 signature.
     * @since 1.0.0
     */
    fun verifyHMAC256(secret: String) =
        _verify(toString(), Auth0JwtAlgorithm.HMAC256(secret))
    /**
     * Verifies the HMAC384 signature of the JWT using the provided secret.
     *
     * @param secret The secret key used for generating and verifying the HMAC384 signature.
     * @since 1.0.0
     */
    fun verifyHMAC384(secret: String) =
        _verify(toString(), Auth0JwtAlgorithm.HMAC384(secret))
    /**
     * Verifies the current JWT using the HMAC512 algorithm with the provided secret key.
     *
     * @param secret The secret key used to verify the HMAC512 signature of the JWT.
     * @since 1.0.0
     */
    fun verifyHMAC512(secret: String) =
        _verify(toString(), Auth0JwtAlgorithm.HMAC512(secret))
    /**
     * Verifies the JWT token using the ECDSA256 algorithm with the provided elliptic curve public and private keys.
     *
     * @param publicKey The ECPublicKey instance used for verifying the signature of the token.
     * @param privateKey The ECPrivateKey instance used for internal verification operations.
     * @since 1.0.0
     */
    fun verifyECDSA256(publicKey: ECPublicKey, privateKey: ECPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.ECDSA256(publicKey, privateKey))
    /**
     * Verifies a JWT token using the ECDSA algorithm with a 384-bit curve.
     *
     * This method validates the JWT against the provided ECDSA public key and private key
     * using the ECDSA384 cryptographic standard. It ensures the token's authenticity and 
     * integrity.
     *
     * @param publicKey An instance of [ECPublicKey] representing the public key used to verify the signature.
     * @param privateKey An instance of [ECPrivateKey] representing the private key used in the verification process.
     * @since 1.0.0
     */
    fun verifyECDSA384(publicKey: ECPublicKey, privateKey: ECPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.ECDSA384(publicKey, privateKey))
    /**
     * Verifies the JWT using the ECDSA signature algorithm with a SHA-512 hash.
     *
     * @param publicKey The ECDSA public key used for signature validation.
     * @param privateKey The ECDSA private key used for signature computation.
     * @since 1.0.0
     */
    fun verifyECDSA512(publicKey: ECPublicKey, privateKey: ECPrivateKey) =
        _verify(toString(), Auth0JwtAlgorithm.ECDSA512(publicKey, privateKey))

    /**
     * Represents the cryptographic algorithms supported for JWT (JSON Web Token) signing and verification.
     *
     * Each algorithm corresponds to a specific cryptographic standard, providing options for hashing 
     * and signature generation using RSA, HMAC, or ECDSA methods.
     *
     * Usage of these algorithms ensures the integrity and authenticity of the JWT during verification.
     *
     * @since 1.0.0
     */
    enum class Algorithm {
        HS256,
        HS384,
        HS512,
        RS256,
        RS384,
        RS512,
        ES256,
        ES384,
        ES512,
        PS256,
        PS384,
        PS512
    }
}
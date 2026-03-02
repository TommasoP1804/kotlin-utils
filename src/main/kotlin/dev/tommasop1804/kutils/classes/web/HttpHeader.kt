package dev.tommasop1804.kutils.classes.web

/**
 * An object that defines commonly used HTTP header names as constants.
 *
 * This class provides a centralized collection of constants representing standard HTTP headers,
 * enabling type-safe reference to header names without the risk of typos or inconsistencies.
 *
 * This covers a wide range of categories such as:
 * - Authentication headers (e.g., Authorization, Proxy-Authorization)
 * - Content headers (e.g., Content-Type, Content-Length)
 * - Caching headers (e.g., Cache-Control, ETag)
 * - Request context headers (e.g., Accept, User-Agent)
 * - CORS (Cross-Origin Resource Sharing) headers
 * - Redirect and connection-related headers
 * - Security-related headers
 * - Common custom headers
 * - Cookie-related headers
 * @since 2.0.0
 * @author Tommaso Pastorelli
 */
@Suppress("unused")
object HttpHeader {
    // Authentication
    const val AUTHORIZATION = "Authorization"
    const val WWW_AUTHENTICATE = "WWW-Authenticate"
    const val PROXY_AUTHENTICATE = "Proxy-Authenticate"
    const val PROXY_AUTHORIZATION = "Proxy-Authorization"

    // Content
    const val CONTENT_TYPE = "Content-Type"
    const val CONTENT_LENGTH = "Content-Length"
    const val CONTENT_DISPOSITION = "Content-Disposition"
    const val CONTENT_ENCODING = "Content-Encoding"
    const val CONTENT_LANGUAGE = "Content-Language"
    const val CONTENT_LOCATION = "Content-Location"
    const val CONTENT_RANGE = "Content-Range"

    // Caching
    const val CACHE_CONTROL = "Cache-Control"
    const val ETAG = "ETag"
    const val IF_MATCH = "If-Match"
    const val IF_NONE_MATCH = "If-None-Match"
    const val IF_MODIFIED_SINCE = "If-Modified-Since"
    const val IF_UNMODIFIED_SINCE = "If-Unmodified-Since"
    const val IF_RANGE = "If-Range"
    const val LAST_MODIFIED = "Last-Modified"
    const val EXPIRES = "Expires"
    const val PRAGMA = "Pragma"
    const val AGE = "Age"
    const val VARY = "Vary"

    // Request context
    const val ACCEPT = "Accept"
    const val ACCEPT_CHARSET = "Accept-Charset"
    const val ACCEPT_ENCODING = "Accept-Encoding"
    const val ACCEPT_LANGUAGE = "Accept-Language"
    const val ACCEPT_RANGES = "Accept-Ranges"
    const val HOST = "Host"
    const val REFERER = "Referer"
    const val ORIGIN = "Origin"
    const val USER_AGENT = "User-Agent"

    // CORS
    const val ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin"
    const val ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods"
    const val ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers"
    const val ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials"
    const val ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers"
    const val ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age"
    const val ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method"
    const val ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers"

    // Redirects & connection
    const val LOCATION = "Location"
    const val CONNECTION = "Connection"
    const val UPGRADE = "Upgrade"
    const val RETRY_AFTER = "Retry-After"
    const val TRANSFER_ENCODING = "Transfer-Encoding"

    // Security
    const val STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security"
    const val X_CONTENT_TYPE_OPTIONS = "X-Content-Type-Options"
    const val X_FRAME_OPTIONS = "X-Frame-Options"
    const val X_XSS_PROTECTION = "X-XSS-Protection"
    const val CONTENT_SECURITY_POLICY = "Content-Security-Policy"
    const val REFERRER_POLICY = "Referrer-Policy"

    // Common custom
    const val X_REQUEST_ID = "X-Request-Id"
    const val X_CORRELATION_ID = "X-Correlation-Id"
    const val X_FORWARDED_FOR = "X-Forwarded-For"
    const val X_FORWARDED_HOST = "X-Forwarded-Host"
    const val X_FORWARDED_PROTO = "X-Forwarded-Proto"
    const val X_REAL_IP = "X-Real-IP"

    // Cookies
    const val COOKIE = "Cookie"
    const val SET_COOKIE = "Set-Cookie"

    const val ALLOW = "Allow"
    const val DATE = "Date"
    const val EXPECT = "Expect"
    const val FROM = "From"
    const val LINK = "Link"
    const val MAX_FORWARDS = "Max-Forwards"
    const val RANGE = "Range"
    const val SERVER = "Server"
    const val TE = "TE"
    const val TRAILER = "Trailer"
    const val VIA = "Via"
    const val WARNING = "Warning"
}
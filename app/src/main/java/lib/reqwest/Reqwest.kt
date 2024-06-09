package lib.reqwest

import kotlinx.serialization.json.Json
import lib.future.Future
import lib.future.future
import lib.option.Option
import lib.option.some
import lib.option.toOption
import lib.result.Result
import lib.result.catching
import lib.result.err
import java.net.HttpURLConnection
import java.net.URI
import java.text.ParseException

/**
 * Class representing an HTTP fetch operation.
 *
 * @property url The URL to which the request is sent.
 */
class Reqwest(private val url: String) {
    private var method: Method = Method.GET
    private var headers: Map<String, String> = emptyMap()
    private var body: Any = ""
    private var redirect: Boolean = true
    private var cache: Boolean = false

    /**
     * Executes the HTTP request asynchronously.
     *
     * @return A Future containing a Result object, which holds either the response or an error.
     */
    fun send(): Future<Result<Response, Throwable>> = future {
        val connection = catching {
            (URI(url).toURL().openConnection() as HttpURLConnection).apply {
                requestMethod = method.value
                instanceFollowRedirects = redirect
                useCaches = cache
                headers.forEach { setRequestProperty(it.key, it.value) }
                doInput = true
                doOutput = method.value != "GET"
                if (doOutput) outputStream.use { it.write(body.toString().toByteArray()) }
            }
        }.unwrapOrElse { return@future err(Exception(it.message ?: "")) }

        connection.catching(some { connection.disconnect() }) {
            connect()
            val responseHeaders = headerFields
            val ok = responseCode in 200..299
            val redirect = responseCode in 300..399
            val statusCode = responseCode
            val statusText = responseMessage.toOption()
            val type = contentType.toOption()
            val url = url.toString()
            val content = when (statusCode) {
                in 200..299 -> inputStream.bufferedReader().use { it.readText() }
                else -> errorStream.bufferedReader().use { it.readText() }
            }
            Response(responseHeaders, ok, redirect, statusCode, statusText, type, url, content)
        }
    }

    /**
     * Sets the HTTP request method.
     *
     * @param method The method to be used for the request.
     * @return Reqwest instance for method chaining.
     */
    fun method(method: Method) = apply { this@Reqwest.method = method }

    /**
     * Sets the request headers.
     *
     * @param headers The headers to be set for the request.
     * @return Reqwest instance for method chaining.
     */
    fun headers(headers: Map<String, String>) = apply { this@Reqwest.headers = headers }

    /**
     * Sets the request body.
     *
     * @param body The body to be sent with the request.
     * @return Reqwest instance for method chaining.
     */
    fun body(body: Any) = apply { this@Reqwest.body = body }

    /**
     * Sets whether to follow redirects or not.
     *
     * @param redirect A Boolean indicating whether to follow redirects or not.
     * @return Reqwest instance for method chaining.
     */
    fun redirect(redirect: Boolean) = apply { this@Reqwest.redirect = redirect }

    /**
     * Sets whether to use cache or not.
     *
     * @param cache A Boolean indicating whether to use cache or not.
     * @return Reqwest instance for method chaining.
     */
    fun cache(cache: Boolean) = apply { this@Reqwest.cache = cache }
}

/**
 * Data class representing an HTTP response.
 *
 * @property headers The headers of the response.
 * @property ok A boolean indicating whether the request was successful (status code 2xx).
 * @property redirect A boolean indicating whether the response is a redirect (status code 3xx).
 * @property statusCode The status code of the response.
 * @property statusText The status message of the response.
 * @property type The content type of the response.
 * @property url The URL of the response.
 * @property content The content of the response.
 */
data class Response(
    val headers: Map<String, List<String>>,
    val ok: Boolean,
    val redirect: Boolean,
    val statusCode: Int,
    val statusText: Option<String>,
    val type: Option<String>,
    val url: String,
    val content: String
) {
    /**
     * Parses the content of the response as an object of type T.
     * @return A Result object containing the parsed object or a ParseException.
     */
    inline fun <reified T> parseContent(ignoreKey: Boolean = false): Result<T, ParseException> = catching {
        val json = Json { ignoreUnknownKeys = ignoreKey }
        json.decodeFromString<T>(content)
    }.mapErr { ParseException(it.message, it.hashCode()) }

    /**
     * Parses the content of the response as an object of type T without error handling.
     * This method is intended for internal use cases where error handling is done elsewhere.
     *
     * @return The parsed object of type T.
     */
    inline fun <reified T> parseContentUnchecked(ignoreKey: Boolean = false): T {
        val json = Json { ignoreUnknownKeys = ignoreKey }
        return json.decodeFromString<T>(content)
    }

    override fun toString(): String = """
header: $headers
ok: $ok
redirect: $redirect
statusCode: $statusCode
statusText: $statusText
type: $type
url $url
content: $content
    """.trimIndent()
}

/**
 * Enum class representing HTTP methods.
 *
 * @property value The string representation of the HTTP method.
 */
enum class Method(val value: String) {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    PATCH("PATCH"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    TRACE("TRACE")
}
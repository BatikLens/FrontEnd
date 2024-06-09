package lib.option

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import lib.result.Result

/**
 * Representing an optional value.
 * An instance of [Option] is either an instance of [Some] containing a non-null value,
 * or an instance of [None] representing absence of value.
 *
 * @param T the type of the value contained in [Some]
 */
@Serializable(with = OptionSerializer::class)
sealed class Option<out T> {
    /**
     * Data class representing the presence of a value.
     * @property value the non-null value contained in [Some]
     */
    data class Some<out T>(val value: T): Option<T>() {
        override fun equals(other: Any?): Boolean = other is Some<*> && value == other.value
        override fun toString(): String = "Some($value)"
        override fun hashCode(): Int = value.hashCode()
    }

    /**
     * Object representing absence of value.
     */
    data object None: Option<Nothing>() {
        private fun readResolve(): Any = None
        override fun toString(): String = "None"
    }

    /**
     * Retrieves the value contained in [Some] or throws [IllegalAccessException] if called on [None].
     *
     * @throws IllegalAccessException if called on [None]
     * @return the value contained in [Some]
     */
    @Throws(IllegalAccessException::class)
    fun unwrap(): T = when (this) {
        is Some -> value
        is None -> throw IllegalAccessException("Call `unwrap()` on `None` value")
    }

    /**
     * Retrieves the value contained in [Some] or returns a default value if called on [None].
     *
     * @param default the default value to return if called on [None]
     * @return the value contained in [Some] or [default] if called on [None]
     */
    fun unwrapOr(default: @UnsafeVariance T): T = when (this) {
        is Some -> value
        is None -> default
    }

    /**
     * Retrieves the value contained in [Some] or computes a default value if called on [None].
     *
     * @param f function to compute the default value if called on [None]
     * @return the value contained in [Some] or the result of [f] if called on [None]
     */
    inline fun unwrapOrElse(f: () -> @UnsafeVariance T): T = when (this) {
        is Some -> value
        is None -> f()
    }

    /**
     * Retrieves the value contained in [Some] or throws [IllegalAccessException] with the specified message if called on [None].
     *
     * @param msg the message for the exception if called on [None]
     * @throws IllegalAccessException with the specified message if called on [None]
     * @return the value contained in [Some]
     */
    @Throws(IllegalAccessException::class)
    fun except(msg: String): T = when (this) {
        is Some -> value
        is None -> throw IllegalAccessException(msg)
    }

    /**
     * Combines this [Option] with another [Option].
     * If this [Option] is [Some], returns the other [Option].
     * Otherwise, returns [None].
     *
     * @param opt the other [Option]
     * @return [Some] containing the value of the other [Option] if this [Option] is [Some], otherwise [None]
     */
    fun <U> and(opt: Option<U>): Option<U> = when (this) {
        is Some -> opt
        is None -> None
    }

    /**
     * Maps the value contained in [Some] with the specified function and returns the result.
     * If this [Option] is [Some], applies the function to the value and returns the result.
     * Otherwise, returns [None].
     *
     * @param f the function to apply if this [Option] is [Some]
     * @return [Some] containing the result of applying the function if this [Option] is [Some], otherwise [None]
     */
    inline fun <U> andThen(f: (T) -> Option<U>): Option<U> = when (this) {
        is Some -> f(value)
        is None -> None
    }

    /**
     * Combines this [Option] with another [Option].
     * If this [Option] is [Some], returns it.
     * Otherwise, returns the other [Option].
     *
     * @param opt the other [Option]
     * @return this [Option] if it is [Some], otherwise the other [Option]
     */
    fun or(opt: Option<@UnsafeVariance T>): Option<T> = when (this) {
        is Some -> Some(value)
        is None -> opt
    }

    /**
     * Maps the value contained in [Some] with the specified function and returns the result.
     * If this [Option] is [Some], applies the function to the value and returns the result.
     * Otherwise, returns the result of applying the specified function to [None].
     *
     * @param opt the function to apply if this [Option] is [Some]
     * @return [Some] containing the result of applying the function if this [Option] is [Some],
     * otherwise the result of applying the specified function to [None]
     */
    inline fun orElse(opt: () -> Option<@UnsafeVariance T>): Option<T> = when (this) {
        is Some -> Some(value)
        is None -> opt()
    }

    /**
     * Filters the value contained in [Some] with the specified predicate and returns the result.
     * If this [Option] is [Some] and the value satisfies the predicate, returns [Some] containing the value.
     * Otherwise, returns [None].
     *
     * @param f the predicate function to apply if this [Option] is [Some]
     * @return [Some] containing the value if this [Option] is [Some] and the value satisfies the predicate,
     * otherwise [None]
     */
    inline fun filter(f: (T) -> Boolean): Option<T> = when {
        this is Some && f(value) -> Some(value)
        else -> None
    }

    /**
     * Performs the specified action on the value contained in [Some].
     *
     * @param f the action to perform if this [Option] is [Some]
     * @return this [Option]
     */
    inline fun inspect(f: (T) -> Unit) = apply { if (this is Some) f(value) }

    /**
     * Applies the given function to the value of Some if the option is a Some,
     * otherwise returns None.
     * @param f The function to be applied to the value of Some.
     * @return The result of applying the function to the value of Some, or None if the option is None.
     */
    inline fun <U> map(f: (T) -> U): Option<U> = when (this) {
        is Some -> Some(f(value))
        is None -> None
    }

    /**
     * Applies the given function to the value of Some if the option is a Some,
     * otherwise returns the specified default value.
     * @param default The default value to be returned if the option is None.
     * @param f The function to be applied to the value of Some.
     * @return The result of applying the function to the value of Some, or the default value if the option is None.
     */
    inline fun <U> mapOr(default: U, f: (T) -> U): U = when (this) {
        is Some -> f(value)
        is None -> default
    }

    /**
     * Applies the given function to the value of Some if the option is a Some,
     * otherwise returns the specified default value.
     * @param default The function returning the default value to be returned if the option is None.
     * @param f The function to be applied to the value of Some.
     * @return The result of applying the function to the value of Some, or the default value returned by the specified function if the option is None.
     */
    inline fun <U> mapOrElse(default: () -> U, f: (T) -> U): U = when (this) {
        is Some -> f(value)
        is None -> default()
    }

    /**
     * Transforms the Option into a Result.
     * Returns Result.Ok with the value of Some, or Result.Err with the specified error if the option is None.
     * @param err The error to be returned if the option is None.
     * @return Result.Ok with the value of Some, or Result.Err with the specified error if the option is None.
     */
    fun <E> okOr(err: E): Result<T, E> = when (this) {
        is Some -> Result.Ok(value)
        is None -> Result.Err(err)
    }

    /**
     * Transforms the Option into a Result.
     * Returns Result.Ok with the value of Some, or Result.Err with the result of the specified error function if the option is None.
     * @param err The function returning the error to be returned if the option is None.
     * @return Result.Ok with the value of Some, or Result.Err with the result of the specified error function if the option is None.
     */
    inline fun <E> okOrElse(err: () -> E): Result<T, E> = when (this) {
        is Some -> Result.Ok(value)
        is None -> Result.Err(err())
    }

    /**
     * Returns the exclusive or (XOR) of two Options.
     * Returns Some if one of the options is Some and the other is None, otherwise returns None.
     * @param opt The Option to be XORed with this Option.
     * @return Some if one of the options is Some and the other is None, otherwise None.
     */
    fun xor(opt: Option<@UnsafeVariance T>): Option<T> = when {
        (this is Some && opt is None) || (this is None && opt is Some) -> this
        else -> None
    }

    /**
     * Zips two Options into a single Option of a Pair.
     * Returns Some containing a Pair of values if both Options are Some, otherwise returns None.
     * @param opt The Option to be zipped with this Option.
     * @return Some containing a Pair of values if both Options are Some, otherwise None.
     */
    fun <U> zip(opt: Option<U>): Option<Pair<T, U>> = when {
        this is Some && opt is Some -> Some(Pair(value, opt.value))
        else -> None
    }

    /**
     * Zips two Options and applies the given function to the resulting Pair.
     * Returns Some containing the result of applying the function if both Options are Some, otherwise returns None.
     * @param opt The Option to be zipped with this Option.
     * @param f The function to be applied to the zipped values.
     * @return Some containing the result of applying the function if both Options are Some, otherwise None.
     */
    inline fun <U, R> zipWith(opt: Option<U>, f: (T, U) -> R): Option<R> = when {
        this is Some && opt is Some -> Some(f(value, opt.value))
        else -> None
    }

    /**
     * Checks if the Option is None.
     * Returns true if the Option is None, false otherwise.
     * @return true if the Option is None, false otherwise.
     */
    fun isNone(): Boolean = this is None

    /**
     * Checks if the option is a Some and satisfies the given predicate.
     * Returns true if the option is a Some and the predicate is satisfied, false otherwise.
     * @param f The predicate function to be applied to the value of Some.
     * @return true if the option is a Some and the predicate is satisfied, false otherwise.
     */
    inline fun isSomeAnd(f: (T) -> Boolean): Boolean = when (this) {
        is Some -> f(value)
        is None -> false
    }

    /**
     * Checks if the Option is a Some.
     * Returns true if the Option is a Some, false otherwise.
     * @return true if the Option is a Some, false otherwise.
     */
    fun isSome(): Boolean = this is Some
}

// -- Extension

/**
 * Converts a nullable value to an Option.
 * @param T The type of the value.
 * @return Option Some if the value is not null, None otherwise.
 */
fun <T> T?.toOption(): Option<T> = when {
    this != null -> Option.Some(this)
    else -> Option.None
}

/**
 * Unzips an option of a pair into a pair of options.
 * @param T The type of the first component of the pair.
 * @param U The type of the second component of the pair.
 * @return A pair of options.
 */
fun <T, U> Option<Pair<T, U>>.unzip(): Pair<Option<T>, Option<U>> = when (this) {
    is Option.Some -> Pair(Option.Some(value.first), Option.Some(value.second))
    is Option.None -> Pair(Option.None, Option.None)
}

/**
 * Transposes an option of a result into a result of an option.
 * @param T The type of the value contained in the inner option.
 * @param E The type of the error in the result.
 * @return A result of an option.
 */
fun <T, E> Option<Result<T, E>>.transpose(): Result<Option<T>, E> = when (this) {
    is Option.Some -> when (value) {
        is Result.Ok -> Result.Ok(Option.Some(value.value))
        is Result.Err -> Result.Err(value.err)
    }
    is Option.None -> Result.Ok(Option.None)
}

/**
 * Flattens an option of an option into a single option.
 * @param T The type of the value contained in the inner option.
 * @return A flattened option.
 */
fun <T> Option<Option<T>>.flatten(): Option<T> = when (this) {
    is Option.Some -> value
    is Option.None -> Option.None
}

/**
 * Returns an iterator over the elements of the option.
 * @param R The type of elements returned by the iterator.
 * @return An iterator.
 */
inline fun <R, reified T: Iterable<R>> Option<T>.iter(): Iterator<R> = when (this) {
    is Option.Some -> value.iterator()
    is Option.None -> iterator {  }
}

/**
 * Converts a collection to an option.
 * @param T The type of the collection.
 * @return Some if the collection is not empty, None otherwise.
 */
fun <T: Collection<*>> T.toOption(): Option<T> = when {
    this.isEmpty() -> Option.None
    else -> some(this)
}

/**
 * Constructs a Some option with the given value.
 * @param T The type of the value.
 * @param value The value to be wrapped in a Some option.
 * @return Some option containing the value.
 */
fun <T> some(value: T): Option<T> = Option.Some(value)

/**
 * Constructs a None option.
 * @param T The type of the value.
 * @return None option.
 */
fun <T> none(): Option<T> = Option.None

typealias Some<T> = Option.Some<T>

typealias None = Option.None

private class OptionSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<Option<T>> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Option") {
        element<JsonElement>("value")
    }

    override fun serialize(encoder: Encoder, value: Option<T>) {
        val jsonEncoder = encoder as? JsonEncoder ?: error("This class can be saved only by JSON")
        when (value) {
            is Option.Some -> jsonEncoder.encodeSerializableValue(dataSerializer, value.value)
            is Option.None -> jsonEncoder.encodeSerializableValue(JsonElement.serializer(), JsonNull)
        }
    }

    override fun deserialize(decoder: Decoder): Option<T> {
        val jsonDecoder = decoder as? JsonDecoder ?: error("This class can be loaded only by JSON")
        val jsonElement = jsonDecoder.decodeJsonElement()
        return if (jsonElement == JsonNull) {
            Option.None
        } else {
            Option.Some(jsonDecoder.json.decodeFromJsonElement(dataSerializer, jsonElement))
        }
    }
}

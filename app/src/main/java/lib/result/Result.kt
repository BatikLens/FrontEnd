package lib.result

import lib.option.Option
import lib.option.none
import lib.option.some
import java.io.Closeable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Represents a result of an operation that can be either successful (Ok) with a value of type [T]
 * or failed (Err) with an error of type [E].
 * @param T The type of the value in case of success.
 * @param E The type of the error in case of failure.
 */
sealed class Result<out T, out E> {
    /** Represents a successful result with a value of type [T]. */
    data class Ok<out T>(val value: T) : Result<T, Nothing>() {
        override fun toString(): String = "Ok($value)"
    }

    /** Represents a failed result with an error of type [E]. */
    data class Err<out E>(val err: E) : Result<Nothing, E>() {
        override fun toString(): String = "Err($err)"
    }

    /**
     * Retrieves the value if this is an Ok result.
     * @throws [IllegalAccessException] with an error message indicating that `unwrap()` is called on an Err value.
     * @return The value of type [T] if this is an Ok result.
     */
    @Throws(IllegalAccessException::class)
    fun unwrap(): T = when (this) {
        is Ok -> value
        is Err -> throw IllegalAccessException("Call `unwrap()` on an Err value $err")
    }

    /**
     * Retrieves the value if this is an Ok result, or the provided default value if it is an Err result.
     * @param default The default value to be returned in case of an Err result.
     * @return The value of type [T] if this is an Ok result, or the provided default value if it is an Err result.
     */
    fun unwrapOr(default: @UnsafeVariance T): T = when (this) {
        is Ok -> value
        is Err -> default
    }

    /**
     * Retrieves the error if this is an Err result.
     * @throws [IllegalAccessException] with an error message indicating that `unwrapErr()` is called on an Ok value.
     * @return The error of type [E] if this is an Err result.
     */
    @Throws(IllegalAccessException::class)
    fun unwrapErr(): E = when (this) {
        is Ok -> throw IllegalAccessException("Call `unwrapErr()` on an Ok value $value")
        is Err -> err
    }

    /**
     * Retrieves the contained [`Ok`] value or computes it from a closure.
     * @param f The closure to compute the value in case of an Err result.
     * @return The value of type [T] if this is an Ok result, or the computed value from the closure if it is an Err result.
     */
    inline fun unwrapOrElse(f: (E) -> @UnsafeVariance T): T = when (this) {
        is Ok -> value
        is Err -> f(err)
    }

    /**
     * Retrieves the value if this is an Ok result, or throws an [IllegalAccessException] with the provided message if it is an Err result.
     * @param msg The message to be included in the [IllegalAccessException] thrown in case of an Err result.
     * @throws [IllegalAccessException] with the provided message if this is an Err result.
     * @return The value of type [T] if this is an Ok result.
     */
    @Throws(IllegalAccessException::class)
    fun except(msg: String): T = when (this) {
        is Ok -> value
        is Err -> throw IllegalAccessException(msg)
    }

    /**
     * Retrieves the error if this is an Err result, or throws an [IllegalAccessException] with the provided message if it is an Ok result.
     * @param msg The message to be included in the [IllegalAccessException] thrown in case of an Ok result.
     * @throws [IllegalAccessException] with the provided message if this is an Ok result.
     * @return The error of type [E] if this is an Err result.
     */
    @Throws(IllegalAccessException::class)
    fun exceptErr(msg: String): E = when (this) {
        is Ok -> throw IllegalAccessException(msg)
        is Err -> err
    }

    /**
     * Maps the value of an Ok result using the provided function, or returns an Err result with the same error if this is an Err result.
     * @param f The function to be applied to the value of type [T] in case of an Ok result.
     * @return A new Result with the mapped value of type [U] if this is an Ok result, or an Err result with the same error if this is an Err result.
     */
    inline fun <U> map(f: (T) -> U): Result<U, E> = when (this) {
        is Ok -> Ok(f(value))
        is Err -> Err(err)
    }

    /**
     * Maps the error of an Err result using the provided function, or returns an Ok result with the same value if this is an Ok result.
     * @param f The function to be applied to the error of type [E] in case of an Err result.
     * @return A new Result with the same value if this is an Ok result, or an Err result with the mapped error of type [R] if this is an Err result.
     */
    inline fun <R> mapErr(f: (E) -> R): Result<T, R> = when (this) {
        is Ok -> Ok(value)
        is Err -> Err(f(err))
    }

    /**
     * Retrieves the value of an Ok result by applying the provided function, or returns the provided default value if this is an Err result.
     * @param default The default value to be returned in case of an Err result.
     * @param f The function to be applied to the value of type [T] in case of an Ok result.
     * @return The result of applying the provided function to the value if this is an Ok result, or the provided default value if this is an Err result.
     */
    inline fun <U> mapOr(default: U, f: (T) -> U): U = when (this) {
        is Ok -> f(value)
        is Err -> default
    }

    /**
     * Retrieves the value of an Ok result by applying the provided function, or computes the value using the provided closure in case of an Err result.
     * @param default The closure to compute the value in case of an Err result.
     * @param f The function to be applied to the value of type [T] in case of an Ok result.
     * @return The result of applying the provided function to the value if this is an Ok result, or the computed value from the closure if this is an Err result.
     */
    inline fun <U> mapOrElse(default: (E) -> U, f: (T) -> U): U = when (this) {
        is Ok -> f(value)
        is Err -> default(err)
    }

    /**
     * Retrieves the value of an Ok result as an [Option.Some], or [Option.None] if this is an Err result.
     * @return An [Option.Some] with the value of type [T] if this is an Ok result, or [Option.None] if this is an Err result.
     */
    fun ok(): Option<T> = when (this) {
        is Ok -> Option.Some(value)
        is Err -> Option.None
    }

    /**
     * Retrieves the error of an Err result as an [Option.Some], or [Option.None] if this is an Ok result.
     * @return An [Option.Some] with the error of type [E] if this is an Err result, or [Option.None] if this is an Ok result.
     */
    fun err(): Option<E> = when (this) {
        is Ok -> Option.None
        is Err -> Option.Some(err)
    }

    /**
     * Combines this result with another result. If this result is Ok, returns the other result.
     * If this result is Err, returns a new result with the same error as this one.
     * @param res The result to combine with.
     * @return The combined result.
     */
    fun <U> and(res: Result<U, @UnsafeVariance E>): Result<U, E> = when (this) {
        is Ok -> res
        is Err -> Err(err)
    }

    /**
     * Maps this result to another result using the provided function.
     * If this result is Ok, applies the function to its value and returns the result.
     * If this result is Err, returns a new result with the same error as this one.
     * @param f The function to apply to the value of this result.
     * @return The result of applying the function.
     */
    inline fun <U> andThen(f: (T) -> Result<U, @UnsafeVariance E>): Result<U, E> = when (this) {
        is Ok -> f(value)
        is Err -> Err(err)
    }

    /**
     * Combines this result with another result. If this result is Ok, returns this result.
     * If this result is Err, returns the other result.
     * @param res The result to combine with.
     * @return The combined result.
     */
    fun <R> or(res: Result<@UnsafeVariance T, R>): Result<T, R> = when (this) {
        is Ok -> Ok(value)
        is Err -> res
    }

    /**
     * Maps the error of this result to another result using the provided function.
     * If this result is Ok, returns this result.
     * If this result is Err, applies the function to its error and returns the result.
     * @param f The function to apply to the error of this result.
     * @return The result of applying the function.
     */
    inline fun <R> orElse(f: (E) -> Result<@UnsafeVariance T, R>): Result<T, R> = when (this) {
        is Ok -> Ok(value)
        is Err -> f(err)
    }

    /**
     * Inspects the value of this result if it is Ok.
     * @param f The function to apply to the value of this result if it is Ok.
     * @return This result.
     */
    inline fun inspect(f: (T) -> Unit) = apply { if (this is Ok) f(value) }

    /**
     * Inspects the error of this result if it is Err.
     * @param f The function to apply to the error of this result if it is Err.
     * @return This result.
     */
    inline fun inspectErr(f: (E) -> Unit) = apply { if (this is Err) f(err) }

    /**
     * Checks if this result is Ok and satisfies the given predicate.
     * @param f The predicate to apply to the value of this result if it is Ok.
     * @return True if this result is Ok and the predicate returns true, false otherwise.
     */
    inline fun isOkAnd(f: (T) -> Boolean): Boolean = when (this) {
        is Ok -> f(value)
        is Err -> false
    }

    /**
     * Checks if this result is Err and satisfies the given predicate.
     * @param f The predicate to apply to the error of this result if it is Err.
     * @return True if this result is Err and the predicate returns true, false otherwise.
     */
    inline fun isErrAnd(f: (E) -> Boolean): Boolean = when (this) {
        is Ok -> false
        is Err -> f(err)
    }

    /**
     * Checks if this result is Ok.
     * @return True if this result is Ok, false otherwise.
     */
    fun isOk(): Boolean = this is Ok

    /**
     * Checks if this result is Err.
     * @return True if this result is Err, false otherwise.
     */
    fun isErr(): Boolean = this is Err
}

// ----- Extension

/**
 * Transposes a result of an option to an option of a result.
 * @param T The type of the value in the option.
 * @param E The type of the error.
 * @return An option of result.
 */
fun <T, E> Result<Option<T>, E>.transpose(): Option<Result<T, E>> = when (this) {
    is Result.Ok -> when (value) {
        is Option.Some -> Option.Some(Result.Ok(value.value))
        is Option.None -> Option.None
    }
    is Result.Err -> Option.Some(Result.Err(err))
}

/**
 * Flattens a nested result into a single level result.
 * @param T The type of the value.
 * @param E The type of the error.
 * @return A flattened result.
 */
fun <T, E> Result<Result<T, E>, E>.flatten(): Result<T, E> = when (this) {
    is Result.Ok -> value
    is Result.Err -> Result.Err(err)
}

/**
 * Returns an iterator over the elements of the collection.
 *
 * @return an iterator
 */
fun <R, T: Iterable<R>, E> Result<T, E>.iter(): Iterator<R> = when (this) {
    is Result.Ok -> value.iterator()
    is Result.Err -> iterator {  }
}

/**
 * Returns a successful [Result] wrapping the given [value].
 *
 * @param value the value to wrap
 * @return a successful result containing the value
 */
fun <T, E> ok(value: T): Result<T, E> = Result.Ok(value)

/**
 * Returns an error [Result] wrapping the given [err].
 *
 * @param err the error to wrap
 * @return an error result containing the error
 */
fun <T, E> err(err: E): Result<T, E> = Result.Err(err)


/**
 * Executes the provided function [fn] and returns a Result object containing the result of the function execution.
 * If the function execution completes successfully, the result is wrapped in a Success object.
 * If the function throws an exception, the exception is wrapped in a Failure object.
 * Optionally, a final action can be specified using the [final] parameter, which will be executed regardless of whether an exception is thrown or not.
 *
 * @param final Optional final action to be executed, default is None.
 * @param fn The function to be executed.
 * @return A Result object containing the result of the function execution.
 */
inline fun <T> catching(final: Option<() -> Unit> = none(), fn: () -> T): Result<T, Throwable> = try {
    ok(fn())
} catch(e: Throwable) {
    err(e)
} finally {
    if (final is Option.Some) final.value()
}

/**
 * Executes the provided function [fn] with the receiver as its context and returns a Result object containing the result of the function execution.
 * If the function execution completes successfully, the result is wrapped in a Success object.
 * If the function throws an exception, the exception is wrapped in a Failure object.
 * Optionally, a final action can be specified using the [final] parameter, which will be executed regardless of whether an exception is thrown or not.
 *
 * @param final Optional final action to be executed, default is None.
 * @param fn The function to be executed with the receiver as its context.
 * @return A Result object containing the result of the function execution.
 */
inline fun <T, V> T.catching(final: Option<() -> Unit> = none(), fn: T.() -> V): Result<V, Throwable> = try {
    ok(fn())
} catch (e: Throwable) {
    err(e)
} finally {
    if (final is Option.Some) final.value()
}

/**
 * Executes the given function block with a resource that is automatically closed
 * upon completion or error. The function ensures that the resource is closed even
 * if an exception is thrown during the execution of the block.
 *
 * @param fn The function to execute with the resource.
 * @return A [Result] object containing the result of the function if it completes
 *         successfully, or the exception if an error occurs.
 *
 * @receiver The resource that is being managed, which must implement [Closeable].
 */
@OptIn(ExperimentalContracts::class)
inline fun <T: Closeable, V> T.useAndCatch(fn: (T) -> V): Result<V, Throwable> {
    contract {
        callsInPlace(fn, InvocationKind.EXACTLY_ONCE)
    }

    var exception: Option<Throwable> = none()

    return try {
        ok(fn(this))
    } catch (e: Throwable) {
        exception = some(e)
        err(e)
    } finally {
        when (exception) {
            is Option.None -> this.close()
            is Option.Some -> try {
                this.close()
            } catch (closeException: Throwable) {
                exception.unwrap().addSuppressed(closeException)
            }
        }
    }
}
package lib.future

import kotlinx.coroutines.*
import lib.option.Option
import lib.option.none
import lib.option.some
import lib.result.Result
import lib.result.err
import lib.result.ok

/**
 * Indicates whether a value is available or if the current task has been scheduled to receive a wakeup instead.
 *
 * @param V The type of the result produced by the `Future`.
 */
sealed class Poll<out V> {
    /**
     * Represents a completed result with a value.
     *
     * @param V The type of the result value.
     * @property value The result value produced by the `Future`.
     */
    data class Ready<out V>(val value: V) : Poll<V>()

    /**
     * Represents a pending state where the result is not yet available.
     */
    data object Pending : Poll<Nothing>()

    /**
     * Maps a `Poll<V>` to `Poll<U>` by applying a function to a contained value.
     *
     * This function transforms the contained value of a `Poll` object using the provided `block` function.
     * If the `Poll` is in the `Ready` state, it applies the `block` function to the contained value and
     * returns a new `Poll` in the `Ready` state with the transformed value. If the `Poll` is in the `Pending`
     * state, it returns a `Poll` in the `Pending` state.
     *
     * @param block A function that takes a value of type `V` and returns a value of type `U`.
     * @return A `Poll` containing a value transformed by the given `block` function if it was in the `Ready` state,
     *         otherwise, a `Poll` in the `Pending` state.
     */
    fun <U> map(block: (V) -> U): Poll<U> = when (this) {
        is Ready -> Ready(block(value))
        is Pending -> Pending
    }

    /**
     * Checks if the current state is `Pending`.
     *
     * @return `true` if the state is `Pending`, `false` otherwise.
     */
    fun isPending() = this is Pending

    /**
     * Checks if the current state is `Ready`.
     *
     * @return `true` if the state is `Ready`, `false` otherwise.
     */
    fun isReady() = this is Ready
}

// ---------------- Extension Poll ---------------- //

/**
 * Maps a `Poll<Result<T, E>>` to `Poll<Result<T, U>>` by applying a
 * function to the error value of a contained `Poll.Ready(Result.Err)`,
 * leaving all other variants untouched.
 *
 * @param block The function to apply to the error value.
 * @return A `Poll<Result<T, U>>` with the error value mapped.
 */
fun <U, T, E> Poll<Result<T, E>>.mapErr(block: (E) -> U): Poll<Result<T, U>> = when (this) {
    is Poll.Ready -> when (value) {
        is Result.Ok -> Poll.Ready(ok(value.value))
        is Result.Err -> Poll.Ready(err(block(value.err)))
    }
    is Poll.Pending -> Poll.Pending
}

/**
 * Maps a `Poll<Result<T, E>>` to `Poll<Result<U, E>>` by applying a
 * function to the success value of a contained `Poll.Ready(Result.Ok)`,
 * leaving all other variants untouched.
 *
 * @param block The function to apply to the success value.
 * @return A `Poll<Result<U, E>>` with the success value mapped.
 */
fun <U, T, E> Poll<Result<T, E>>.mapOk(block: (T) -> U): Poll<Result<U, E>> = when (this) {
    is Poll.Ready -> when (value) {
        is Result.Ok -> Poll.Ready(ok(block(value.value)))
        is Result.Err -> Poll.Ready(err(value.err))
    }
    is Poll.Pending -> Poll.Pending
}

/**
 * Maps a `Poll<Option<Result<T, E>>>` to `Poll<Option<Result<T, U>>>` by applying a
 * function to the error value of a contained `Poll.Ready(Option.Some(Result.Err))`,
 * leaving all other variants untouched.
 *
 * @param block The function to apply to the error value.
 * @return A `Poll<Option<Result<T, U>>>` with the error value mapped.
 */
fun <U, T, E> Poll<Option<Result<T, E>>>.mapErrOpt(block: (E) -> U): Poll<Option<Result<T, U>>> = when (this) {
    is Poll.Ready -> when (value) {
        is Option.Some -> when (val result = value.value) {
            is Result.Ok -> Poll.Ready(some(ok(result.value)))
            is Result.Err -> Poll.Ready(some(err(block(result.err))))
        }
        is Option.None -> Poll.Ready(none())
    }
    is Poll.Pending -> Poll.Pending
}

/**
 * Maps a `Poll<Option<Result<T, E>>>` to `Poll<Option<Result<U, E>>>` by applying a
 * function to the success value of a contained `Poll.Ready(Option.Some(Result.Ok))`,
 * leaving all other variants untouched.
 *
 * @param block The function to apply to the success value.
 * @return A `Poll<Option<Result<U, E>>>` with the success value mapped.
 */
fun <U, T, E> Poll<Option<Result<T, E>>>.mapOkOpt(block: (T) -> U): Poll<Option<Result<U, E>>> = when (this) {
    is Poll.Ready -> when (value) {
        is Option.Some -> when (val result = value.value) {
            is Result.Ok -> Poll.Ready(some(ok(block(result.value))))
            is Result.Err -> Poll.Ready(some(err(result.err)))
        }
        is Option.None -> Poll.Ready(none())
    }
    is Poll.Pending -> Poll.Pending
}

/**
 * A future represents an asynchronous computation obtained by use of async.
 * A future is a value that might not have finished computing yet.
 * This kind of “asynchronous value” makes it possible for a thread to continue doing useful work while it waits for the value to become available.
 *
 * @param Output The type of the result produced by the future computation.
 */
interface Future<Output> {
    /**
     * Suspends the current coroutine until the future computation completes.
     *
     * This method will suspend the calling coroutine until the result of the computation
     * is available. Once the computation is complete, it will return the result.
     *
     * @return The result of the computation.
     */
    suspend fun await(): Output

    /**
     * Attempts to resolve the future into a final value.
     * This method does not block if the value is not ready.
     * Instead, the current task is scheduled to be woken up when it’s possible to make further progress
     * by polling again. The context passed to the poll method can provide a Waker,
     * which is a handle for waking up the current task.
     *
     * When using a future, you generally won’t call poll directly, but instead await the value.
     *
     * @return A [Poll] object representing the current state of the computation.
     */
    fun poll(): Poll<Output>
}

private class FutureImplementation <V> private constructor(private val block: suspend () -> V): Future<V> {
    @OptIn(DelicateCoroutinesApi::class)
    private val deferred = GlobalScope.async(start = CoroutineStart.LAZY) { block() }

    override suspend fun await(): V = deferred.await()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun poll(): Poll<V> {
        if (!deferred.isActive && !deferred.isCompleted) deferred.start()

        return when {
            deferred.isCompleted -> Poll.Ready(deferred.getCompleted())
            else -> Poll.Pending
        }
    }

    companion object { internal fun <V> invoke(block: suspend () -> V) = FutureImplementation(block) }
}

/**
 * Creates a [Future] with a suspended function.
 *
 * @param block The suspended function to be executed.
 * @return A [Future] that executes the given suspended function.
 */
fun <V> future(block: suspend () -> V): Future<V> = FutureImplementation.invoke(block)
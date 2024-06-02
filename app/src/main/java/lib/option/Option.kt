package lib.option

sealed class Option<out V> {
    data class Some<out V>(val value: V): Option<V>()
    data object None: Option<Nothing>()

    
}
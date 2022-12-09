package gamemodel.world

interface Replicatable<T> {
    fun replicate(): T
}

package mapgen.predicate

import mathutils.*


interface CellPredicate2d<T> {
    fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean
}

fun <T> CellPredicate2d<T>.negated(): CellPredicate2d<T> {
    val saved = this
    return object : CellPredicate2d<T> {
        override fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean {
            return !saved.belongs(generatedMap2d, x, y)
        }
    }
}

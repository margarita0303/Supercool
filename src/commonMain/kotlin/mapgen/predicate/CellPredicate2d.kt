package mapgen.predicate

import mathutils.*


interface CellPredicate2d<T> {
    fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean
}

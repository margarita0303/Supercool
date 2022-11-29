package mapgen.predicate

import mathutils.*

class AndPredicate2d<T>(private val predicates: Array<CellPredicate2d<T>>) : CellPredicate2d<T> {

    override fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean {
        for (cellPredicate in predicates) {
            if (!cellPredicate.belongs(generatedMap2d, x, y)) {
                return false
            }
        }
        return true
    }


}

package mapgen.predicate

import mathutils.*

class CellEquals2d<T>(private val tileType: T) : CellPredicate2d<T> {

    override fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean {
        return generatedMap2d.get(x, y) === tileType
    }


}

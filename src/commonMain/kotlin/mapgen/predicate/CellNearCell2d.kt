package mapgen.predicate

import mathutils.*


class CellNearCell2d<T>(private val cellType: T) : CellPredicate2d<T> {

    override fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean {
        for (ix in -1..1) {
            for (iy in -1..1) {
                if (ix == 0 && iy == 0) continue
                if (generatedMap2d.outside(x + ix, y + iy)) continue
                if (generatedMap2d.get(x + ix, y + iy) === cellType) return true
            }
        }
        return false
    }


}

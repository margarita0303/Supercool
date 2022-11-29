package mapgen

import mapgen.brush.*
import mapgen.predicate.*
import mathutils.*

object MapGen2d {

    fun <T> fill(map: Matrix2d<T>, predicate: CellPredicate2d<T>, brush2d: Brush2d<T>) {
        map.forEach { x, y ->
            if (predicate.belongs(map, x, y)) {
                brush2d.draw(map, x, y)
            }
        }
    }

    fun <T> fill(map: Matrix2d<T>, predicate: CellPredicate2d<T>, brush2d: (x: Int, y: Int) -> Unit) {
        map.forEach { x, y ->
            if (predicate.belongs(map, x, y)) {
                brush2d(x, y)
            }
        }
    }
}

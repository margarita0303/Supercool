package mapgen

import mapgen.brush.*
import mapgen.predicate.*
import mathutils.*

fun <T> Matrix2d<T>.fill(predicate: CellPredicate2d<T>, brush2d: Brush2d<T>) {
    this.forEach { x, y ->
        if (predicate.belongs(this, x, y)) {
            brush2d.draw(this, x, y)
        }
    }
}

fun <T> Matrix2d<T>.fill(predicate: CellPredicate2d<T>, brush2d: (x: Int, y: Int) -> Unit) {
    this.forEach { x, y ->
        if (predicate.belongs(this, x, y)) {
            brush2d(x, y)
        }
    }
}

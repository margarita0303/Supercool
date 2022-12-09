package mapgen.brush

import mathutils.*


interface Brush2d<T> {
    fun draw(generatedMap2d: Matrix2d<T>, x: Int, y: Int)
}

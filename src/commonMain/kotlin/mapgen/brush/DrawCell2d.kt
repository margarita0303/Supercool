import mapgen.brush.*
import mathutils.*


class DrawCell2d<T>(private val cellType: T) : Brush2d<T> {

    override fun draw(generatedMap2d: Matrix2d<T>, x: Int, y: Int) {
        generatedMap2d[x, y] = cellType
    }
}

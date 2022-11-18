import mapgen.brush.*
import mathutils.*


class DrawCell2d<T>(val cellType: T) : Brush2d<T> {

    override fun draw(generatedMap2d: Matrix2d<T>, x: Int, y: Int) {
        generatedMap2d.set(x, y, cellType)
    }
}

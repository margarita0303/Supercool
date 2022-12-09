package mapgen.predicate

import mathutils.*
import kotlin.random.*

class RandomPredicate2d<T>(val percetage: Float) : CellPredicate2d<T> {

    override fun belongs(generatedMap2d: Matrix2d<T>, x: Int, y: Int): Boolean = Random.nextFloat() < percetage

}

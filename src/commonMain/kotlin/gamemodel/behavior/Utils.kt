package gamemodel.behavior

import gamemodel.world.*
import math.*
import mathutils.*

fun isSecondEntityVisibleByFirst(firstPos: Vec2, secondPos: Vec2, dist: Double, world: World): Boolean {
    return getEuclideanDistance(firstPos, secondPos) < dist && los(firstPos.x, firstPos.y, secondPos.x, secondPos.y)
    { x, y -> !world.tiles[x, y].tileType.blocks }
}


package gamemodel.behavior

import gamemodel.world.*
import math.*

fun isSecondEntityVisibleByFirst(firstPos: Vec2, secondPos: Vec2, dist: Double, world: World): Boolean {
    return getEuclideanDistance(firstPos, secondPos) < dist && los(firstPos.x, firstPos.y, secondPos.x, secondPos.y)
    { x, y -> !world.tiles[x, y].tileType.blocks }
}

fun findFireDirection(firstEntity: Entity, secondEntity: Entity): Vec2 {
    return (secondEntity.absPos - firstEntity.absPos).getNormilizedWithTileSize()
}

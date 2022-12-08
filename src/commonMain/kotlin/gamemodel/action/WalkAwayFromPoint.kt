package gamemodel.action

import gamemodel.world.*
import math.*

class WalkAwayFromPoint(
    val destination: Vec2
) : Action {
    override fun onPerform(world: World, entity: Entity): ActionResult {
        if (entity.pos == destination) {
            walkInOtherDirection(west, world, entity, true)
        }

        if (getManhattanDistance(destination, entity.pos) <= 1) {
            return walkInOtherDirection(destination - entity.pos, world, entity, false)
        }

        // if not adjacent, path to point
        val path = findPath2d(
            size = world.tiles.getSize(),
            cost = { 1.0 },
            heuristic = { one, two -> getEuclideanDistance(one, two) },
            neighbors = { our ->
                our.vonNeumanNeighborhood().filter { vec ->
                    world.tiles.contains(vec) &&
                        !world.tiles[vec].isBlocked() &&
                        world.entities.filter { it.pos == vec && it != entity && it.isAlive() && !it.player }.isEmpty()
                }
            },
            start = entity.pos,
            end = destination
        )


        if (path == null || path.size < 2)
            return Failed

        val offset = path[1] - entity.pos

        return walkInOtherDirection(offset, world, entity, false)
    }

    private fun walkInOtherDirection(badDir: Vec2, world: World, entity: Entity, anyDir: Boolean): ActionResult {
        val startDir = badDir * -1.0
        val directions = listOf(west, east, north, south)
        val dirsToWalk = if(!anyDir) listOf(startDir) + directions.filter { it != badDir } else directions
        dirsToWalk.forEach{ dir ->
            val res = Walk(dir).onPerform(world, entity)
            if(res == Succeeded)
                return res
        }
        return Failed
    }
}

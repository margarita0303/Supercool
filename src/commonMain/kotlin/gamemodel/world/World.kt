package gamemodel.world

import GameConfig
import mathutils.Matrix2d
import math.getEuclideanDistance
import math.los

class World(
    val tiles: Matrix2d<Cell>,
    val entities: MutableList<Entity>,
    var player: Entity,
) {
    var playerMovementTimeEffectDelay = 0.0
    var timeSpeed = 1.0

    fun passTime() {
        recalculateTimeSpeed()

        entities.forEach { entity ->
            entity.behavior?.getNextAction(entity, this)?.onPerform(this, entity)
            entity.onWorldUpdated(timeSpeed)
        }

    }

    fun recalculateLight() {
        // set all to false
        // find player, in radius make visible
        tiles.forEach { it -> it.lit = false }
        val players = entities.filter { it.player }
        players.forEach { player ->

            for (xD in (-10..10)) {
                for (yD in (-10..10)) {

                    val x = player.pos.x + xD
                    val y = player.pos.y + yD

                    if (tiles.contains(x, y)
                            && getEuclideanDistance(0.0, 0.0, xD.toDouble(), yD.toDouble()) < 10
                            && (los(player.pos.x, player.pos.y, x, y) { x, y -> !tiles[x, y].tileType.blocks }
                                    || los(x, y, player.pos.x, player.pos.y) { x, y -> !tiles[x, y].tileType.blocks })
                    ) {
                        tiles[x, y].lit = true
                        tiles[x, y].wasLit = true
                    }
                }
            }
        }
    }

    private fun recalculateTimeSpeed() {
        playerMovementTimeEffectDelay -= 1.0 / GameConfig.worldUpdateRate
        val moveCoef = if(playerMovementTimeEffectDelay <= 0) 1.0 else 4.0 // TODO: find function for coef
        timeSpeed = moveCoef
    }

    fun removeDeadEntities() {
        entities.removeIf {
            !it.isAlive()
        }
    }
}

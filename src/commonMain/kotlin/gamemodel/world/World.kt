package gamemodel.world

import GameConfig
import math.*
import mathutils.*

/**
 *  Current state of game world
 * */
class World(
    val tiles: Matrix2d<Cell>,
    val entities: MutableList<Entity>,
    val collectables: MutableList<Collectable>,
    var player: Entity,
) {
    var playerMovementTimeEffectDelay = 0.0
    var timeSpeed = 1.0
    var gameState: GameState = GameState.Active
        private set

    /**
     * Calling this method causes the world to be updated
     * */
    fun passTime() {
        recalculateTimeSpeed()

        entities.forEach { entity ->
            entity.behavior.getNextAction(entity, this)?.onPerform(this, entity)
            entity.onWorldUpdated(timeSpeed)
        }

    }

    /**
     * Update cell illumination
     * */
    fun recalculateLight() {
        // set all to false
        // find player, in radius make visible
        tiles.forEach { it -> it.lit = false }
        val players = entities.filter { it.player }
        players.forEach { player ->
            val min = Vec2(-10, -10)
            val max = Vec2(10, 10)
            for (diff in min..max) {
                val position = player.pos + diff

                if (tiles.contains(position)
                    && diff.length < 10
                    && (los(player.pos, position) { x, y -> !tiles[x, y].tileType.blocks }
                        || los(position, player.pos) { x, y -> !tiles[x, y].tileType.blocks })
                ) {
                    tiles[position].lit = true
                }
            }
        }
    }

    private fun recalculateTimeSpeed() {
        playerMovementTimeEffectDelay -= 1.0 / GameConfig.worldUpdateRate
        val moveCoef = if (playerMovementTimeEffectDelay <= 0) 1.0 else 4.0 // TODO: find function for coef
        timeSpeed = moveCoef
    }

    fun removeDeadEntities() {
        entities.removeIf {
            !it.isAlive()
        }
    }

    fun removeNonExistentCollectables() {
        collectables.removeIf {
            !it.exists
        }
    }

    fun updateGameState() {
        if (!player.isAlive())
            gameState = GameState.Lost
        else if (entities.none { !it.player })
            gameState = GameState.Won
    }

    fun canSpawnOn(it: Vec2): Boolean {
        return !tiles[it].isBlocked() && entities.all { entity -> entity.pos != it }
    }

    sealed interface GameState {
        object Active : GameState
        object Won : GameState
        object Lost : GameState
    }
}

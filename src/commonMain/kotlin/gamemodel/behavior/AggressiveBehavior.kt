package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*
import math.*
import kotlin.random.*

class AggressiveBehavior(private val duplicates: Boolean = false) : Behavior {
    private var lastPlayerPos: Vec2? = null
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action {
        val isVisible = isSecondEntityVisibleByFirst(entity.pos, world.player.pos, 10.0, world)

        if (isVisible) {
            if (duplicates) {
                if (Random.nextDouble(0.0, 1000.0) < 1) {
                    return SpawnDuplicate()
                }
            }
            lastPlayerPos = world.player.pos
            return WalkToPoint(world.player.pos)
        } else {
            lastPlayerPos?.let {
                if (entity.pos == it) {
                    lastPlayerPos = null
                } else {
                    return WalkToPoint(it)
                }
            }
            return Walk(listOf(west, east, north, south).random())
        }
    }

    override fun onWorldUpdated(timeSpeed: Double) {

    }

    override fun replicate(): Behavior {
        val beh = AggressiveBehavior(duplicates)
        beh.lastPlayerPos = lastPlayerPos?.copy()
        return beh
    }

}

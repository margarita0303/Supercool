package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*
import math.*

class AggressiveBehavior : Behavior {
    private var lastPlayerPos: Vec2? = null
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action {
        val isVisible = isSecondEntityVisibleByFirst(entity.pos, world.player.pos, 10.0, world)

        if (isVisible) {
            lastPlayerPos = world.player.pos
            return WalkToPoint(world.player.pos)
        } else {
            lastPlayerPos?.let {
                if(entity.pos == it) {
                    lastPlayerPos = null
                } else {
                    return WalkToPoint(it)
                }
            }
            return Walk(listOf(west, east, north, south).random())
        }
    }

    override fun onWorldUpdated(timeSpeed: Double) {}

}

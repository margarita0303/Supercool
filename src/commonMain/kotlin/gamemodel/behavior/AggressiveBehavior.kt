package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*
import math.*

class AggressiveBehavior : Behavior {
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action {

        // see enemy
        val isVisible = isSecondEntityVisibleByFirst(entity.pos, world.player.pos, 10.0, world)
        return if (isVisible) { //charge
            WalkToPoint(world.player.pos)
        } else {
            Walk(listOf(west, east, north, south).random())
        }

    }

    override fun onWorldUpdated(timeSpeed: Double) {}

}

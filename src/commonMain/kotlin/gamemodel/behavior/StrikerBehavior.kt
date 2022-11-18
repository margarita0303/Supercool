package gamemodel.behavior

import gamemodel.action.Action
import gamemodel.action.*
import gamemodel.world.*
import math.*

class StrikerBehavior : Behavior {
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action {

        // see enemy
        val isVisible = isSecondEntityVisibleByFirst(entity.pos, world.player.pos, 10.0, world)
        return if (isVisible) { //charge
            val dir = (world.player.absPos - entity.absPos).getNormilizedWithTileSize()
            Fire(dir)
        } else {
            Walk(listOf(west, east, north, south).random())
        }

    }

}

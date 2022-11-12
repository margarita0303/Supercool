package gamemodel.behavior

import gamemodel.action.Action
import gamemodel.action.*
import gamemodel.world.*
import math.Vec2

class StrikerBehavior : Behavior {
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action {

        // see enemy
        val isVisible = isSecondEntityVisibleByFirst(entity.pos, world.player.pos, 10.0, world)
        return if (isVisible) { //charge
            val dir = (world.player.absPos - entity.absPos).getNormilizedWithTileSize()
            Fire(dir)
        } else {
            Walk(listOf(Vec2(1, 0), Vec2(1, 0), Vec2(1, 0), Vec2(1, 0)).random())
        }

    }

}

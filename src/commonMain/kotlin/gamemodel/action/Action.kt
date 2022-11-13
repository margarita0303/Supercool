package gamemodel.action

import gamemodel.world.Entity
import gamemodel.world.World


interface Action {
    fun onPerform(world: World, entity: Entity): ActionResult
}

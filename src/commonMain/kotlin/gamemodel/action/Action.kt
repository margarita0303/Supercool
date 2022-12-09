package gamemodel.action

import gamemodel.world.Entity
import gamemodel.world.World


/**
 * Encapsulates logic of the entity action
 * */
interface Action {
    fun onPerform(world: World, entity: Entity): ActionResult
}

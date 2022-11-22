package game.action

import gamemodel.action.*
import gamemodel.world.Entity
import gamemodel.world.World


class Wait : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        return Succeeded
    }

}

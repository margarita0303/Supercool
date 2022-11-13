package gamemodel.behavior

import gamemodel.action.Action
import gamemodel.world.Entity
import gamemodel.world.World

class NoAiBehavior : Behavior {

    private var action: Action? = null

    override fun getNextAction(entity: Entity, world: World): Action? {
        return action
    }

    override fun setAction(action: Action?) {
        this.action = action
    }

}

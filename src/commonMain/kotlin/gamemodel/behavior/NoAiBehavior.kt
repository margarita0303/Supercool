package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*

class NoAiBehavior : Behavior {

    private var action: Action? = null

    override fun getNextAction(entity: Entity, world: World): Action? {
        val toReturn = action
        action = null
        return toReturn
    }

    override fun onWorldUpdated(timeSpeed: Double) {}

    override fun setAction(action: Action?) {
        this.action = action
    }

}

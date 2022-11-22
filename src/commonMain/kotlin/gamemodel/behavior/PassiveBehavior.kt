package gamemodel.behavior

import game.action.*
import gamemodel.action.*
import gamemodel.world.*

class PassiveBehavior: Behavior {
    override fun setAction(action: Action?) {}

    override fun getNextAction(entity: Entity, world: World): Action? {
        return Wait()
    }
}

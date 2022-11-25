package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*

interface BehaviorChanger {
    fun getChangedBehavior(baseBehavior: Behavior) : Behavior
}

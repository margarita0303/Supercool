package gamemodel.behavior

import gamemodel.action.*
import gamemodel.world.*

/**
 * Get the Behavior and modify it in a particular way
 * */
interface BehaviorChanger {
    fun getChangedBehavior(baseBehavior: Behavior) : Behavior
}

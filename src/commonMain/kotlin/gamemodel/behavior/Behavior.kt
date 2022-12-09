package gamemodel.behavior


import gamemodel.action.*
import gamemodel.world.*

/**
 * Encapsulates logic of entity behavior
 * */
interface Behavior : Replicatable<Behavior> {
    /**
     * Set some action that should to be performed by entity
     * */
    fun setAction(action: Action?)

    /**
     * Get the action that should to be performed by entity
     * */
    fun getNextAction(entity: Entity, world: World): Action?

    fun onWorldUpdated(timeSpeed: Double)

}



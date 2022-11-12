package gamemodel.behavior


import gamemodel.action.Action
import gamemodel.world.*


interface Behavior {
    // receives instructions
    fun setAction(action: Action?)

    // acts on them
    fun getNextAction(entity: Entity, world: World): Action?

}



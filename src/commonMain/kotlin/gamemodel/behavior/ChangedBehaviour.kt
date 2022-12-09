package gamemodel.behavior

import GameConfig
import gamemodel.action.*
import gamemodel.world.*
import math.*

class ChangedBehaviour(private val baseBehavior: Behavior) : Behavior {
    private var effectRemainingTime = 3.0
    override fun setAction(action: Action?) {
        baseBehavior.setAction(action)
    }

    override fun getNextAction(entity: Entity, world: World): Action? {
        return if (effectRemainingTime > 0) {
            Walk(listOf(west, east, north, south).random())
        } else {
            baseBehavior.getNextAction(entity, world)
        }
    }

    override fun onWorldUpdated(timeSpeed: Double) {
        effectRemainingTime -= (1.0 / GameConfig.worldUpdateRate) * timeSpeed
    }

    override fun replicate(): Behavior {
        val beh = ChangedBehaviour(baseBehavior.replicate())
        beh.effectRemainingTime = effectRemainingTime
        return beh
    }

}

package gamemodel.behavior

import GameConfig
import gamemodel.action.*
import gamemodel.world.*
import math.*

class ConfusingBehaviorChanger : BehaviorChanger {
    override fun getChangedBehavior(baseBehavior: Behavior): Behavior {
        return object : Behavior {
            private var effectRemainingTime = 3.0
            override fun setAction(action: Action?) {
                baseBehavior.setAction(action)
            }

            override fun getNextAction(entity: Entity, world: World): Action? {
                return if(effectRemainingTime > 0) {
                    Walk(listOf(west, east, north, south).random())
                } else {
                    baseBehavior.getNextAction(entity, world)
                }
            }

            override fun onWorldUpdated() {
                effectRemainingTime -= 1.0 / GameConfig.worldUpdateRate
            }

        }
    }

}

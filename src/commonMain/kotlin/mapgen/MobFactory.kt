package mapgen

import gamemodel.behavior.*
import gamemodel.world.*
import math.*
import kotlin.random.*

class MobFactory {

    fun createNextMob(position: Vec2): Entity {
        return if (Random.nextDouble() < 0.3) {
            Entity(position, EntityType.Slime, AggressiveBehavior(true), blocks = true)
        } else {
            Entity(position, EntityType.EnemyWarrior, AggressiveBehavior(), blocks = true)
        }
    }
}

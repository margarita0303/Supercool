package gamemodel.action

import gamemodel.world.Entity
import gamemodel.world.World
//import gameState
import math.Vec2


class Walk(val dir: Vec2) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        if(!entity.canMoveNow)
            return Failed

        val nextPos = entity.pos + dir

        if (world.tiles.outside(nextPos)) {
            return Failed
        }


        val entitiesOnNextSpace = world.entities.filter { it.pos == nextPos && it.blocks }
        if(entitiesOnNextSpace.isNotEmpty()) {
            entitiesOnNextSpace.forEach {
                val res = Melee(it).onPerform(world, entity)
                if(res is Succeeded && entity.player && !it.isAlive())
                    entity.plusExp(50)
            }
            return Failed
        }


        if (world.tiles[nextPos].isBlocked()) {
            //blocked
            return Failed
        }

        entity.pos = nextPos

        if(entity.player) {
            entity.movingDelay = entity.getMoveTime()
            world.playerMovementTimeEffectDelay = entity.movingDelay
        } else {
            entity.movingDelay = entity.getMoveTime()
        }


        return Succeeded
    }
}

package gamemodel.action

import gamemodel.world.Entity
import gamemodel.world.World
//import gameState
import math.getChebyshevDistance


class Melee(
    private val victim: Entity
) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        if(!entity.canMeleeNow)
            return Failed
        if (victim.player == entity.player)
            return Failed

        if (getChebyshevDistance(victim.pos, entity.pos) > 1) {
            return Failed
        }

        victim.damage(entity.meleeAttack())
        entity.meleeDelay = entity.getMeleeTime()

        return Succeeded

    }

}

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

        val (damage, effect) = entity.meleeAttack()
        victim.applyBehaviorEffect(effect)
        victim.damage(damage)
        entity.resetMeleeDelay()
        if(entity.player && !victim.isAlive())
            entity.plusExp(expForKill)

        return Succeeded
    }

    companion object {
        const val expForKill = 35
    }

}

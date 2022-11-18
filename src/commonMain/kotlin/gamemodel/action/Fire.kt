package gamemodel.action

import gamemodel.behavior.*
import gamemodel.world.*
import math.*

class Fire(private val dir: Vec2): Action {
    override fun onPerform(world: World, entity: Entity): ActionResult {
        if(!entity.canFireNow)
            return Failed
        val bulletType = entity.bulletAttack() ?: return Failed
        val bullet = Entity(entity.pos, bulletType)
        bullet.behavior = NoAiBehavior()
        bullet.behavior?.setAction(BulletFlight(dir))
        world.bullets.add(bullet)
        entity.fireDelay = entity.getFireTime() ?: return Failed
        entity.focusAbsPos = entity.absPos + dir
        return Succeeded
    }
}

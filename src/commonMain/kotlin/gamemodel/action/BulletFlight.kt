package gamemodel.action

import GameConfig
import gamemodel.world.*
import math.*


// TODO Bullet collision. I think handling collision with korge tools will be easier
class BulletFlight(private val dir: Vec2): Action {
    override fun onPerform(world: World, entity: Entity): ActionResult {
        val nextPos = entity.absPos + dir * (1.0 / GameConfig.worldUpdateRate / entity.type.timeForMove * world.timeSpeed)
        if(world.tiles.outside(nextPos)) {
            entity.kill()
            return Failed
        }
        entity.absPos = nextPos
        return Succeeded
    }
}

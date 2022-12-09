package gamemodel.action

import game.world.*
import gamemodel.world.*


class Loot(private val collectable: Collectable) : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        if(!entity.player) {
            return Failed
        }
        val prev = entity.collectItem(collectable.item)
        if(prev == null)
            collectable.exists = false
        else
            collectable.item = prev
        return Succeeded
    }

}

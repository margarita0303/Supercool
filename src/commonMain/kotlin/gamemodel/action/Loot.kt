package gamemodel.action

import game.world.*
import gamemodel.world.Entity
import gamemodel.world.World



class Loot : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        entity.pos.vonNeumanNeighborhood().forEach {
            if(world.tiles[it].decor == Decor.CHEST) {
                // TODO choose random weapon or equipment item
            }
        }
        return Succeeded
    }

}

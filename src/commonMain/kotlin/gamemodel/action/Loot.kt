package gamemodel.action

import game.world.*
import gamemodel.world.Entity
import gamemodel.world.World



class Loot : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        entity.pos.vonNeumanNeighborhood().forEach {
            if(world.tiles[it].decor == Decor.CHEST) {
                world.tiles[it].decor = Decor.CHEST_OPEN
                if (entity.type.hp < 100) {
                    entity.type.hp += 10
                }
            }
        }
        return Succeeded
    }

}

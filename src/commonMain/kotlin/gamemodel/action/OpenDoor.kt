package gamemodel.action

import game.world.*
import gamemodel.world.*


class OpenDoor : Action {

    override fun onPerform(world: World, entity: Entity): ActionResult {
        entity.pos.vonNeumanNeighborhood().forEach {
            val cell = world.tiles[it]
            if (cell.tileType == TileType.DOOR_CLOSED) {
                cell.tileType = TileType.DOOR_OPEN
                return Succeeded
            }
        }
        return Failed
    }

}

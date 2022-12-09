package gamemodel.action

import gamemodel.world.*

class SpawnDuplicate : Action {
    override fun onPerform(world: World, entity: Entity): ActionResult {
        val replicant = entity.replicate()
        entity.pos.vonNeumanNeighborhood().firstOrNull { world.canSpawnOn(it) }
            ?.let { newPos ->
                replicant.pos = newPos
                world.entities.add(replicant)
                return Succeeded
            }
        return Failed
    }
}

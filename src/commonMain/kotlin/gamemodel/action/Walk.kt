package gamemodel.action

import game.world.*
import gamemodel.world.*
//import gameState
import math.Vec2
import kotlin.random.*


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
                Melee(it).onPerform(world, entity)
            }
            return Failed
        }

        val collectablesOnNextSpace = world.collectables.filter { it.pos == nextPos }
        if(collectablesOnNextSpace.isNotEmpty()) {
            Loot(collectablesOnNextSpace.first()).onPerform(world, entity)
            return Failed
        }

        if (entity.player && world.tiles[nextPos].decor == Decor.CHEST) {
            world.tiles[nextPos].decor = Decor.CHEST_OPEN
            val randomValue = kotlin.math.abs(Random.nextInt()) % 2
            if (randomValue == 0) {
                val weaponItem = WeaponItem.values().toList().shuffled().first()
                world.collectables.add(Collectable(nextPos, weaponItem))
            }
            else if (randomValue == 1) {
                val equipmentItem = EquipmentItem.values().toList().shuffled().first()
                world.collectables.add(Collectable(nextPos, equipmentItem))
            }
            return Failed
        }


        if (world.tiles[nextPos].isBlocked()) {
            //blocked
            return Failed
        }

        entity.pos = nextPos


        entity.resetMoveDelay()
        if(entity.player) {
            world.playerMovementTimeEffectDelay = entity.getMoveTime()
        }

        return Succeeded
    }
}

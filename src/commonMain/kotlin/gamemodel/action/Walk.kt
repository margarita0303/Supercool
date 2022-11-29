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

        fun findNearestDirt(): Vec2 {
            val nearestDirt: Vec2
            for (i in (-1..1)) {
                for (j in (-1 .. 1)) {
                    val potentialNearestDirt = Vec2(entity.pos.x + i, entity.pos.y + j)
                    if (world.tiles[potentialNearestDirt].tileType == TileType.DIRT) return potentialNearestDirt
                }
            }
            return Vec2(0, 0)
        }

        if (world.tiles[nextPos].decor == Decor.CHEST) {
            world.tiles[nextPos].decor = Decor.CHEST_OPEN
            val randomValue = kotlin.math.abs(Random.nextInt()) % 2
            if (randomValue == 0) {
                val weaponItem = WeaponItem.values().toList().shuffled().first()
                val nearestDirt = findNearestDirt()
                world.tiles[nearestDirt].collectableEntity = Collectable(nearestDirt, weaponItem, null)
            }
            else if (randomValue == 1) {
                val equipmentItem = EquipmentItem.values().toList().shuffled().first()
                val nearestDirt = findNearestDirt()
                world.tiles[nearestDirt].collectableEntity = Collectable(nearestDirt, null, equipmentItem)
            }
        }


        if (world.tiles[nextPos].isBlocked()) {
            //blocked
            return Failed
        }

        entity.pos = nextPos

        if(entity.player) {
            entity.movingDelay = entity.getMoveTime()
            world.playerMovementTimeEffectDelay = entity.movingDelay
        } else {
            entity.movingDelay = entity.getMoveTime()
        }


        return Succeeded
    }
}

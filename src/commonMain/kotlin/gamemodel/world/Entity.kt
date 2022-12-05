package gamemodel.world

import GameConfig
import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korma.geom.*
import gamemodel.behavior.*
import math.*
import kotlin.math.*
import kotlin.random.*

class Entity(
    var pos: Vec2,
    var type: EntityType,
    var behavior: Behavior,
    private val stats: Stats = Stats(type.hp, type.damage, 0.0,1.0, 1.0, 0),
    private val inventory: Inventory = Inventory(null, null, null, null),
    val player: Boolean = false,
    var blocks: Boolean = false,
) {

    private val initialBehavior = behavior

    var movingDelay: Double = 0.0
    val canMoveNow: Boolean
        get() {
            return movingDelay <= 0
        }

    var meleeDelay: Double = 0.0
    val canMeleeNow: Boolean
        get() {
            return meleeDelay <= 0
        }

    var absPos: Vec2 = pos.toAbsPosCenter()
        get() {
            return pos.toAbsPosCenter()
        }
        set(value) {
            field = value
            pos = field.toTilePos()
        }

    fun isAlive(): Boolean = stats.hp > 0

    fun getHp(): Int = stats.hp

    fun heal(amt: Int) {
        stats.hp = min(type.hp, stats.hp + amt)
        if (stats.hp >= type.hp * type.panicOnHpLevel)
            behavior = initialBehavior
    }

    fun damage(damage: Int) {
        stats.hp = max(0, stats.hp - (damage * (1.0 - stats.protection)).toInt())
        if (stats.hp < type.hp * type.panicOnHpLevel)
            behavior = FearfulBehavior()
    }

    fun kill() {
        stats.hp = 0
    }

    fun meleeAttack(): Attack {
        val damage = (stats.damage * stats.damageMultiplier).toInt()
        val effect = inventory.weapon?.let {
            if (Random.nextDouble() < it.prob) it.effect else null
        }
        return Attack(damage, effect)
    }

    fun getMoveTime(): Double {
        return type.timeForMove / stats.speed
    }

    fun getMeleeTime(): Double {
        return inventory.weapon?.timeForMelee ?: type.timeForMelee
    }

    fun onWorldUpdated(timeSpeed: Double) {
        val relTimeSpeed = if(player) 1.0 else timeSpeed
        if (movingDelay > 0)
            movingDelay -= (1.0 / GameConfig.worldUpdateRate) * relTimeSpeed

        if (meleeDelay > 0)
            meleeDelay -= (1.0 / GameConfig.worldUpdateRate) * relTimeSpeed

        behavior.onWorldUpdated(relTimeSpeed)
    }

    fun plusExp(exp: Int) {
        val prevLevel = getLevel()
        if(prevLevel >= maxLevel)
            return
        stats.exp += exp
        val newLevel = getLevel()
        repeat(newLevel - prevLevel) {
            levelUp()
        }
    }

    fun getLevel(): Int {
        return stats.exp / levelExp + 1
    }

    fun applyBehaviorEffect(effect: BehaviorChanger?) {
        effect?.let {
            behavior = it.getChangedBehavior(behavior)
        }
    }

    fun collectItem(item: Item): Item? {
        var prev: Item? = null
        when(item) {
            is WeaponItem -> {
                prev = inventory.weapon
                inventory.weapon = item
                stats.damage = item.meleeDamage
            }
            is EquipmentItem -> {
                when(item.part) {
                    EquipmentItem.BodyPart.Chest -> {
                        prev = inventory.body
                        inventory.body = item
                    }
                    EquipmentItem.BodyPart.Feet -> {
                        prev = inventory.feet
                        inventory.feet = item
                    }
                    EquipmentItem.BodyPart.Head -> {
                        prev = inventory.head
                        inventory.head = item
                    }
                }
                stats.protection = item.protection
                stats.speed = item.speed
            }
        }
        return prev
    }

    private fun levelUp() {
        type.hp += 10
        type.damage += 5
    }


    data class Stats(var hp: Int, var damage: Int, var protection: Double, var speed: Double, var damageMultiplier: Double, var exp: Int)
    data class Inventory(var weapon: WeaponItem?, var head: EquipmentItem?, var body: EquipmentItem?, var feet: EquipmentItem?)
    data class Attack(val damage: Int, val effect: BehaviorChanger?)

    companion object {
        const val levelExp = 100
        const val maxLevel = 6
    }
}

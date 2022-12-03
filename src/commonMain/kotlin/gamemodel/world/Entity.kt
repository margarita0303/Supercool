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
    private val stats: Stats = Stats(type.hp, 0.0, 1.0, 0),
    private val inventory: Inventory = Inventory(null, null, null),
    val player: Boolean = false,
    var blocks: Boolean = false,
) {
    val sprite: Sprite by lazy {
        Sprite(type.standAnimation).xy(pos.x * tileSize, pos.y * tileSize)
    }

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
        val damage = ((inventory.weapon?.meleeDamage ?: type.damage) * stats.damageMultiplier).toInt()
        val effect = inventory.weapon?.let {
            if (Random.nextDouble() < it.prob) it.effect else null
        }
        return Attack(damage, effect)
    }

    fun getMoveTime(): Double {
        return type.timeForMove
    }

    fun getMeleeTime(): Double {
        return inventory.weapon?.timeForMelee ?: type.timeForMelee
    }

    fun onWorldUpdated(timeSpeed: Double) {
        if (movingDelay > 0)
            movingDelay -= (1.0 / GameConfig.worldUpdateRate) * timeSpeed

        if (meleeDelay > 0)
            meleeDelay -= (1.0 / GameConfig.worldUpdateRate) * timeSpeed

        behavior.onWorldUpdated(timeSpeed)
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

    private fun levelUp() {
        type.hp += 10
        type.damage += 5
    }

    fun updateSprite(isPositionLit: Boolean, timeSpeed: Double) {
        val it = this
        // This does linear interpolation for
        val diff = Point(
            (it.pos.x * tileSize).toDouble() - it.sprite.x,
            (it.pos.y * tileSize).toDouble() - it.sprite.y
        )
        if (diff.length > 3) {
            diff.normalize()
            diff.mul(3.0)
        }

        it.sprite.x += diff.x
        it.sprite.y += diff.y

        it.sprite.visible = isPositionLit
        if (it.isAlive()) {
            val animSpeedCoef = if (it.player) 1.0 else timeSpeed
            if (diff.length > 2) {
                it.sprite.playAnimationLooped(
                    it.type.moveAnimation,
                    spriteDisplayTime = (it.type.timeForMove * 1000 / animSpeedCoef).milliseconds
                )
            } else {
                it.sprite.playAnimationLooped(
                    it.type.standAnimation,
                    spriteDisplayTime = (250 / animSpeedCoef).milliseconds
                )
            }
        } else {
            it.sprite.stopAnimation()
            it.sprite.visible = false
        }
    }


    data class Stats(var hp: Int, var protection: Double, var damageMultiplier: Double, var exp: Int)
    data class Inventory(var weapon: WeaponItem?, var body: EquipmentItem?, var feet: EquipmentItem?)
    data class Attack(val damage: Int, val effect: BehaviorChanger?)

    companion object {
        const val levelExp = 100
        const val maxLevel = 6
    }
}

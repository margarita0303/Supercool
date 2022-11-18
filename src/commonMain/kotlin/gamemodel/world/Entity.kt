package gamemodel.world

import GameConfig
import com.soywiz.korge.view.Sprite
import com.soywiz.korge.view.xy
import gamemodel.behavior.Behavior
import math.RangedValue
import math.Vec2
import GameConfig.tileSize
import kotlin.math.*

class Entity(
    var pos: Vec2,
    var type: EntityType,
    var behavior: Behavior? = null,
    val sprite: Sprite = Sprite(type.standAnimation).xy(pos.x * tileSize, pos.y * tileSize),
    private val stats: Stats = Stats(type.hp, 0.0, type.damage),
    val inventory: Inventory = Inventory(null, null, null),
    val player: Boolean = false,
    var focusAbsPos: Vec2 = Vec2(0, 0),
    var blocks: Boolean = false,
    val stabber: Boolean = false
) {
    var movingDelay: Double = 0.0
    val canMoveNow: Boolean
        get() {
            return movingDelay <= 0
        }

    var fireDelay: Double = 0.0
    val canFireNow: Boolean
        get() {
            return fireDelay <= 0
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

    fun isAlive(): Boolean = stats.hp  > 0

    fun heal(amt: Int) {
        stats.hp = min(type.hp, stats.hp + amt)
    }

    fun damage(damage: Int) {
        stats.hp = max(0, stats.hp - (damage * (1.0 - stats.protection)).toInt())
    }

    fun kill() {
        stats.hp = 0
    }

    fun meleeAttack(): Int {
        return inventory.weapon?.meleeDamage ?: type.damage
    }

    fun bulletAttack(): EntityType? {
        return inventory.weapon?.bulletType
    }

    fun getMoveTime(): Double {
        return type.timeForMove
    }

    fun getMeleeTime(): Double {
        return inventory.weapon?.timeForMelee ?: type.timeForMelee
    }

    fun getFireTime(): Double? {
        return inventory.weapon?.timeForFire
    }

    fun onWorldUpdated() {
        if(movingDelay > 0)
            movingDelay -= 1.0 / GameConfig.worldUpdateRate
        if(fireDelay > 0)
            fireDelay -= 1.0 / GameConfig.worldUpdateRate
        if(meleeDelay > 0)
            meleeDelay -= 1.0 / GameConfig.worldUpdateRate
    }

    data class Stats(var hp: Int, var protection: Double, var damage: Int)
    data class Inventory(var weapon: WeaponItem?, var body: EquipmentItem?, var feet: EquipmentItem?)
}

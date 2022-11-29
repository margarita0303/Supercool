package gamemodel.world

import com.soywiz.korge.view.*
import gamemodel.behavior.*

enum class WeaponItem(val meleeDamage: Int, val timeForMelee: Double, val effect: BehaviorChanger, val prob: Double) {
    SWORD(60,0.5, ConfusingBehaviorChanger(), 0.1),
    AX(140,1.0, ConfusingBehaviorChanger(), 0.1),
    TRIDENT(90,0.9, ConfusingBehaviorChanger(), 0.1);

    lateinit var animation: SpriteAnimation
}

enum class EquipmentItem(val protection: Double, val speed: Double) {
    HELMET(protection = 0.3, speed = 0.9),
    SCAPULAR(protection = 0.5, speed = 0.9),
    BODY_ARMOR(protection = 0.8, speed = 0.9),
    PANTS(protection = 0.7, speed = 0.8),
    SHOES(protection = 0.0, speed = 1.25);

    lateinit var animation: SpriteAnimation
}

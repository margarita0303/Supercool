package gamemodel.world

import com.soywiz.korge.view.*
import gamemodel.behavior.*

sealed interface Item

enum class WeaponItem(val meleeDamage: Int, val timeForMelee: Double, val effect: BehaviorChanger, val prob: Double) : Item {
    SWORD(60,0.5, ConfusingBehaviorChanger(), 0.1),
    AX(140,1.0, ConfusingBehaviorChanger(), 0.1),
    TRIDENT(90,0.9, ConfusingBehaviorChanger(), 0.1);
}

enum class EquipmentItem(val protection: Double, val speed: Double, val part: BodyPart) : Item {
    HELMET(protection = 0.25, speed = 1.0, BodyPart.Head),
    BODY_ARMOR(protection = 0.4, speed = 0.9, BodyPart.Chest),
    SHOES(protection = 0.0, speed = 1.5, BodyPart.Feet);

    sealed interface BodyPart {
        object Head: BodyPart
        object Chest: BodyPart
        object Feet: BodyPart
    }
}

package gamemodel.world

import gamemodel.behavior.*

enum class WeaponItem(val meleeDamage: Int, val timeForMelee: Double, val effect: BehaviorChanger, val prob: Double) {
    Sword(60,0.5, ConfusingBehaviorChanger(), 0.1)
}

enum class EquipmentItem(val protection: Double, val speed: Double) {
    BodyArmor(0.3, 0.9),
    Boots(0.0, 1.25)
}

package gamemodel.world

enum class WeaponItem(val meleeDamage: Int, val timeForMelee: Double) {
    Sword(60,0.5)
}

enum class EquipmentItem(val protection: Double, val speed: Double) {
    BodyArmor(0.3, 0.9),
    Boots(0.0, 1.25)
}

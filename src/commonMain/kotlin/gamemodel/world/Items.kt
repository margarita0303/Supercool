package gamemodel.world

enum class WeaponItem(val meleeDamage: Int, val timeForMelee: Double, val timeForFire: Double, val range: Int, val bulletType: EntityType?) {
    Pistol(10,0.0, 0.5, 10, EntityType.PistolBullet)
}

enum class EquipmentItem(val protection: Double, val speed: Double) {
    BodyArmor(0.3, 0.9),
    Boots(0.0, 1.25)
}

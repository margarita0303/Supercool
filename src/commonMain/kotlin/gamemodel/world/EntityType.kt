package gamemodel.world

enum class EntityType(
    var hp: Int,
    var timeForMove: Double,
    var timeForMelee: Double,
    var damage: Int,
    var panicOnHpLevel: Double,
) {

    Player(100, 0.15, 1.0, 35, 0.0),

    EnemyWarrior(100, 1.0, 1.0, 20, 0.35),

    Slime(30, 10.0, 3.0, 10, 0.1);

}



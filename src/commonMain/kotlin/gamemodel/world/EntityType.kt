package gamemodel.world

import com.soywiz.korge.view.SpriteAnimation

enum class EntityType(
    var hp: Int,
    var timeForMove: Double,
    var timeForMelee: Double,
    var damage: Int,
    var panicOnHpLevel: Double
        ) {

    Player(100, 0.5, 1.0, 35, 0.0),

    Enemy(100, 2.0, 1.0, 20, 0.35);

    lateinit var standAnimation: SpriteAnimation
    lateinit var moveAnimation: SpriteAnimation
}



package gamemodel.world

import com.soywiz.korge.view.SpriteAnimation

enum class EntityType(
        var hp: Int,
        var timeForMove: Double,
        var timeForMelee: Double,
        var damage: Int,
        ) {

    Player(60, 0.5, 1.0, 10),

    Striker(60, 1.0, 1.0, 10),

    PistolBullet(1, 0.1, 1.0, 20);


    lateinit var standAnimation: SpriteAnimation
    lateinit var moveAnimation: SpriteAnimation


}



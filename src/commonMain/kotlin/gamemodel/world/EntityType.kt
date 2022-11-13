package gamemodel.world

import com.soywiz.korge.view.SpriteAnimation

enum class EntityType(
        val hp: Int,
        var timeForMove: Double,
        var timeForMelee: Double,
        var damage: Int,
        ) {

    Player(100, 0.25, 1.0, 10),

    Striker(100, 0.25, 1.0, 10),

    PistolBullet(1, 0.1, 1.0, 20);


    lateinit var standAnimation: SpriteAnimation
    lateinit var moveAnimation: SpriteAnimation


}



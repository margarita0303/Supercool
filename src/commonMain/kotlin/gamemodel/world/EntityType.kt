package gamemodel.world

import com.soywiz.korge.view.SpriteAnimation

enum class EntityType(
    var hp: Int,
    var timeForMove: Double,
    var timeForMelee: Double,
    var damage: Int,
        ) {

    Player(100, 0.5, 1.0, 50),

    Striker(100, 2.0, 1.0, 10);



    lateinit var standAnimation: SpriteAnimation
    lateinit var moveAnimation: SpriteAnimation


}



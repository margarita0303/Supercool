package gamemodel.world

import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import game.world.*

class Cell(
    x: Int,
    y: Int,
    var tileType: TileType,
    var decor: Decor? = null,
    val sprite: Sprite = Sprite(tileType.animation).xy(x * tileSize, y * tileSize),
    val decorSprite: Sprite? = if (decor != null) Sprite(decor.animation).xy(
        x * tileSize,
        y * tileSize
    ) else null,
    var lit: Boolean = false,
    var wasLit: Boolean = false,
) {
    fun rebuild() {
        sprite.playAnimationLooped(tileType.animation, spriteDisplayTime = 250.milliseconds)
        decorSprite?.playAnimationLooped(decor?.animation, spriteDisplayTime = 250.milliseconds)
    }

    fun isBlocked(): Boolean = tileType.blocks || decor?.blocks ?: false
    fun updateSprites(timeSpeed: Double) {
        sprite.visible = lit || wasLit
        sprite.colorMul = if (!lit && wasLit) Colors.DARKGRAY else Colors.WHITE
        sprite.playAnimationLooped(tileType.animation, spriteDisplayTime = 500.milliseconds / timeSpeed)

        decorSprite?.visible = lit || wasLit
        decorSprite?.colorMul = if (!lit && wasLit) Colors.DARKGRAY else Colors.WHITE
        decorSprite?.playAnimationLooped(
            decor?.animation,
            spriteDisplayTime = 500.milliseconds / timeSpeed
        )
    }
}

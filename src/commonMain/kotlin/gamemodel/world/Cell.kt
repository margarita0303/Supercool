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
    var lit: Boolean = false,
    var wasLit: Boolean = false,
) {
    private val sprite: Sprite by lazy { Sprite(tileType.animation).xy(x * tileSize, y * tileSize) }
    private val decorSprite: Sprite? by lazy {
        decor?.let {
            Sprite(it.animation).xy(
                x * tileSize,
                y * tileSize
            )
        }
    }

    fun getAllSprites(): List<Sprite> {
        val list = mutableListOf(sprite)
        if (decorSprite != null)
            list.add(decorSprite!!)
        return list
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

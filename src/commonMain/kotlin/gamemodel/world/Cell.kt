package gamemodel.world

import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import game.world.*

class Cell(
    val x: Int,
    val y: Int,
    var tileType: TileType,
    var decor: Decor? = null,
    var lit: Boolean = false,
    var wasLit: Boolean = false,
) {
    fun isBlocked(): Boolean = tileType.blocks || decor?.blocks ?: false
}

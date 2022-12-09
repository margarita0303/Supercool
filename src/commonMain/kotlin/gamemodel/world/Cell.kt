package gamemodel.world

import game.world.*

/**
 * Represents one cell of world map
 * */
class Cell(
    val x: Int,
    val y: Int,
    var tileType: TileType,
    var decor: Decor? = null,
) {
    var lit: Boolean = false
        set(value) {
            field = value
            if (value)
                wasLit = true
        }

    var wasLit: Boolean = false

    /**
     * Determines whether entity can step into cell
     * */
    fun isBlocked(): Boolean = tileType.blocks || decor?.blocks ?: false
}

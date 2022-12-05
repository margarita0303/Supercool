import game.world.*
import gamemodel.behavior.*
import gamemodel.world.*
import math.*
import mathutils.*

class TestWorldConfig {
    val player = Entity(Vec2(2, 2), EntityType.Player, NoAiBehavior(), player = true, blocks = true)
    val enemy = Entity(Vec2(2, 3), EntityType.Enemy, NoAiBehavior(), player = false, blocks = true)
    val testWorld = World(
        tiles= Matrix2d(Vec2(6, 6)) { i, j ->
            when {
                i == 2 && j == 0 -> {
                    Cell(i, j, TileType.DOOR_CLOSED)
                }
                i == 0 || j == 0 || i == 5 || j == 5 -> {
                    Cell(i, j, TileType.WALL)
                }
                else -> {
                    Cell(i, j, TileType.FLOOR)
                }
            }
        },
        entities = mutableListOf(player, enemy),
        player = player,
        collectables = mutableListOf()
    )
}

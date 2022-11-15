package stage_utils

import com.soywiz.kds.iterators.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import gamemodel.action.*
import gamemodel.world.*
import math.*

class StageHelper(
    private val stage: Stage,
    private val world: World,
    private val flasks: Array<Sprite>,
) {
    fun addSprites() {
        with(stage) {
            world.tiles.forEach { tile ->
                addChild(tile.sprite)
                if (tile.decorSprite != null)
                    addChild(tile.decorSprite)
            }
            world.entities.forEach { addChild(it.sprite) }
        }
    }

    fun addControlKeys() {
        val east = Vec2(1, 0)
        val north = Vec2(0, -1)
        val west = Vec2(-1, 0)
        val south = Vec2(0, 1)
        with(stage) {
            keys {
                down(Key.RIGHT) {
                    world.player.behavior?.setAction(Walk(east))
                }
                down(Key.UP) {
                    world.player.behavior?.setAction(Walk(north))
                }
                down(Key.LEFT) {
                    world.player.behavior?.setAction(Walk(west))
                }
                down(Key.DOWN) {
                    world.player.behavior?.setAction(Walk(south))
                }
            }
        }
    }

    fun setUpHealthPointBar() {
        with(stage) {
            flasks.forEach { addChild(it) }
        }
    }

    fun updateHealthBarState(playerHealth: Int) {
        flasks.fastForEachWithIndex { index, flask ->
            flask.visible = index < playerHealth
            flask.playAnimationLooped()
        }
    }

}

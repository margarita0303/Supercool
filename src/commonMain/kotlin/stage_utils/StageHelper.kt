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
        with(stage) {
            keys {
                down(Key.RIGHT) {
                    world.player.behavior?.setAction(Walk(east))
                }
                down(Key.D) {
                    world.player.behavior?.setAction(Walk(east))
                }
                down(Key.UP) {
                    world.player.behavior?.setAction(Walk(north))
                }
                down(Key.W) {
                    world.player.behavior?.setAction(Walk(north))
                }
                down(Key.LEFT) {
                    world.player.behavior?.setAction(Walk(west))
                }
                down(Key.A) {
                    world.player.behavior?.setAction(Walk(west))
                }
                down(Key.DOWN) {
                    world.player.behavior?.setAction(Walk(south))
                }
                down(Key.S) {
                    world.player.behavior?.setAction(Walk(south))
                }
                down(Key.E) {
                    world.player.behavior?.setAction(Loot())
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

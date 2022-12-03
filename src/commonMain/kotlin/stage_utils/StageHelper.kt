package stage_utils

import com.soywiz.kds.iterators.*
import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import gamemodel.world.*
import game_model_interactor.*
import game_model_interactor.set_player_action_commands.*

class StageHelper(
    private val stage: Stage,
    private val world: World,
    private val gameModelInteractor: GameModelInteractor,
    private val flasks: Array<Sprite>,
    private val expFlasks: Array<Sprite>,
) {
    fun addSprites() {
        with(stage) {
            world.tiles.forEach { tile ->
                tile.getAllSprites().forEach { sprite ->
                    addChild(sprite)
                }
            }
            world.entities.forEach { addChild(it.sprite) }
        }
    }

    fun addControlKeys() {
        with(stage) {
            keys {
                down(Key.RIGHT) {
                    gameModelInteractor.executeCommand(SetPlayerActionWalkEastCommand(world.player))
                }
                down(Key.UP) {
                    gameModelInteractor.executeCommand(SetPlayerActionWalkNorthCommand(world.player))
                }
                down(Key.LEFT) {
                    gameModelInteractor.executeCommand(SetPlayerActionWalkWestCommand(world.player))
                }
                down(Key.DOWN) {
                    gameModelInteractor.executeCommand(SetPlayerActionWalkSouthCommand(world.player))
                }
                down(Key.E) {
                    gameModelInteractor.executeCommand(SetPlayerActionInteractWithDoorsCommand(world.player))
                }
            }
        }
    }

    fun setUpHealthPointBar() {
        with(stage) {
            flasks.forEach { addChild(it) }
        }
    }

    fun setUpExpPointBar() {
        with(stage) {
            expFlasks.forEach { addChild(it) }
        }
    }

    fun updateHealthBarState(playerHealth: Int, maxPlayerHealth: Int) {
        val normalizedHp = kotlin.math.ceil((playerHealth / maxPlayerHealth.toDouble()) * flasks.size)
        flasks.fastForEachWithIndex { index, flask ->
            flask.visible = index < normalizedHp
            flask.playAnimationLooped()
        }
    }

    fun updateExpBarState(playerLevel: Int) {
        expFlasks.fastForEachWithIndex { index, flask ->
            flask.visible = index < playerLevel
            flask.playAnimationLooped()
        }
    }

}

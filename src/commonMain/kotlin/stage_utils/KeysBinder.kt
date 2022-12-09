package stage_utils

import com.soywiz.korev.*
import com.soywiz.korge.input.*
import com.soywiz.korge.view.*
import gamemodel.world.*
import game_model_interactor.*
import game_model_interactor.set_player_action_commands.*

class KeysBinder(
    private val stage: Stage,
    private val world: World,
    private val gameModelInteractor: GameModelInteractor,

) {
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
}

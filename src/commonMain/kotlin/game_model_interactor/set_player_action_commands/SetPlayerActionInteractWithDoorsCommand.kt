package game_model_interactor.set_player_action_commands

import gamemodel.action.*
import gamemodel.world.*
import game_model_interactor.*

class SetPlayerActionInteractWithDoorsCommand(private val player: Entity) : Command {
    override fun execute() {
        player.behavior.setAction(InteractWithDoors())
    }
}

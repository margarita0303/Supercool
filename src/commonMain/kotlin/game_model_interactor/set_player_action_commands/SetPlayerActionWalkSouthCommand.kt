package game_model_interactor.set_player_action_commands

import gamemodel.action.*
import gamemodel.world.*
import math.*
import game_model_interactor.*

class SetPlayerActionWalkSouthCommand(private val player: Entity) : Command {
    override fun execute() {
        player.behavior.setAction(Walk(south))
    }
}

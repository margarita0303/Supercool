package game_model_interactor

import gamemodel.world.*

class HandleGameStateCommand(private val world: World, private val callback: (World.GameState) -> Unit) : Command {
    override fun execute() {
        callback(world.gameState)
    }
}

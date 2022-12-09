package game_model_interactor

import gamemodel.world.*

class UpdateGameState(private val world: World) : Command {
    override fun execute() {
        world.updateGameState()
    }
}

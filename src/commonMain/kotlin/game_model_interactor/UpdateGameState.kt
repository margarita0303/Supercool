package game_model_interactor

import gamemodel.world.*

class UpdateGameState(private val world: World) : Command {
    override fun execute() {
        if (!world.player.isAlive())
            world.gameState = World.GameState.Lost
        else if (world.entities.none { !it.player })
            world.gameState = World.GameState.Won
    }
}

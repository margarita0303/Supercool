package game_model_interactor

import gamemodel.world.*

class RecalculateLightCommand(private val world: World) : Command {
    override fun execute() {
        world.recalculateLight()
    }
}

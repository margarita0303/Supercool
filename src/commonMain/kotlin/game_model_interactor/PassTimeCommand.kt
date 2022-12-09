package game_model_interactor

import gamemodel.world.*

class PassTimeCommand(private val world: World) : Command {
    override fun execute() {
        world.passTime()
    }
}

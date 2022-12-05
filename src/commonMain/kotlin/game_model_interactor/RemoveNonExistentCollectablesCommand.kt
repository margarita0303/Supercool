package game_model_interactor

import gamemodel.world.*

class RemoveNonExistentCollectablesCommand(private val world: World) : Command {
    override fun execute() {
        world.removeNonExistentCollectables()
    }
}

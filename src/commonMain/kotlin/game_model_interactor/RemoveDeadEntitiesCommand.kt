package game_model_interactor

import gamemodel.world.*

class RemoveDeadEntitiesCommand(private val world: World) : Command {
    override fun execute() {
        world.removeDeadEntities()
    }
}

package game_model_interactor

class GameModelInteractor {
    fun executeCommand(command: Command) {
        command.execute()
    }
}

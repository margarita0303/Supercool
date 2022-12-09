package game_model_interactor

/**
 * Provides communication between UI and game model
 * */
class GameModelInteractor {
    fun executeCommand(command: Command) {
        command.execute()
    }
}

package game_model_interactor

import gamemodel.world.*

class UpdateHpAndExpCommand(private val player: Entity, private val callback: (hp: Int, maxHp: Int, level: Int) -> Unit) : Command {
    override fun execute() {
        callback(player.getHp(), player.type.hp, player.getLevel())
    }
}

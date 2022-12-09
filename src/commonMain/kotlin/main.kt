import GameConfig.mapHeight
import GameConfig.mapWidth
import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import gamemodel.world.*
import mapgen.*
import resource_controllers.*
import stage_utils.*
import game_model_interactor.*


suspend fun main() = Korge(width = tileSize * mapWidth, height = tileSize * mapHeight, bgcolor = Colors["#2b2b2b"]) {
    val spriteController = SpriteController(this)
    val world = WorldBuilder().build()
    val gameModelInteractor = GameModelInteractor()
    val binder = KeysBinder(this, world, gameModelInteractor)

    spriteController.initBitmaps()
    spriteController.setUpCellsSprites(world)
    spriteController.setUpEntitySprites(world)
    spriteController.setUpCollectableSprites(world)
    spriteController.setUpHealthPointBar()
    spriteController.setUpExpPointBar()
    spriteController.setUpInventoryBar()

    binder.addControlKeys()

    val winText = text("YOU WIN", textSize = 24.0).xy(400, 500)
    winText.visible = false
    val lostText = text("YOU LOSE", textSize = 24.0).xy(400, 500)
    lostText.visible = false

    text("Life").xy(0, 0)
    text("Level").xy(7 * tileSize, 0)
    text("Inventory").xy(14 * tileSize, 0)

    gameModelInteractor.executeCommand(RecalculateLightCommand(world))
    addFixedUpdater(GameConfig.worldUpdateRate.timesPerSecond) {
        gameModelInteractor.executeCommand(HandleGameStateCommand(world) { state ->
            when (state) {
                World.GameState.Active -> {
                    gameModelInteractor.executeCommand(PassTimeCommand(world))

                    spriteController.updateHealthBarState(world.player.getHp(), world.player.type.hp)
                    spriteController.updateExpBarState(world.player.getLevel())

                    world.tiles.forEach { it ->
                        spriteController.updateTileSprites(it, world.timeSpeed)
                    }

                    world.entities.forEach {
                        spriteController.updateEntitySprite(it, world.tiles[it.pos].lit, world.timeSpeed)
                    }

                    world.collectables.forEach {
                        spriteController.updateCollectableSprite(it, world.timeSpeed)
                    }

                    world.player.getInventoryIfChanged()?.let {
                        spriteController.updateInventoryBar(it)
                    }

                    gameModelInteractor.executeCommand(RemoveDeadEntitiesCommand(world))
                    gameModelInteractor.executeCommand(RemoveNonExistentCollectablesCommand(world))
                    gameModelInteractor.executeCommand(RecalculateLightCommand(world))
                    gameModelInteractor.executeCommand(UpdateGameState(world))
                }
                World.GameState.Won -> {
                    winText.visible = true
                }
                World.GameState.Lost -> {
                    lostText.visible = true
                }
            }
        })
    }
}

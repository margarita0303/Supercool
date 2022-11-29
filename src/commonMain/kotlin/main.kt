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
    spriteController.setUpHealthPointBar()
    spriteController.setUpExpPointBar()
    spriteController.setUpCellsSprites(world)
    spriteController.setUpEntitySprites(world)

    binder.addControlKeys()

    val winText = text("YOU WIN", textSize = 24.0).xy(400, 500)
    winText.visible = false
    val lostText = text("YOU LOSE", textSize = 24.0).xy(400, 500)
    lostText.visible = false

    text("Life").xy(0, 0)
    text("Level").xy(7 * tileSize, 0)

    gameModelInteractor.executeCommand(RecalculateLightCommand(world))
    addFixedUpdater(GameConfig.worldUpdateRate.timesPerSecond) {
        gameModelInteractor.executeCommand(HandleGameStateCommand(world) { state ->
            when (state) {
                World.GameState.Active -> {
                    gameModelInteractor.executeCommand(PassTimeCommand(world))

                    gameModelInteractor.executeCommand(UpdateHpAndExpCommand(world.player) { hp, maxHp, level ->
                        spriteController.updateHealthBarState(hp, maxHp)
                        spriteController.updateExpBarState(level)
                    })

                    world.tiles.forEach { it ->
                        spriteController.updateTileSprites(it, world.timeSpeed)
                    }

                    world.entities.forEach {
                        spriteController.updateEntitySprite(it, world.tiles[it.pos].lit, world.timeSpeed)
                    }

                    gameModelInteractor.executeCommand(RemoveDeadEntitiesCommand(world))
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

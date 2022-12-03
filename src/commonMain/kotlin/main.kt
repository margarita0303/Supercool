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
    val spriteController = SpriteController()
    textureWork(spriteController)
    val world = WorldBuilder().build()
    val flasks: Array<Sprite> = Array(6) { Sprite(spriteController.getHealth()).xy(it * tileSize, tileSize) }
    val expFlasks: Array<Sprite> = Array(6) { Sprite(spriteController.getExp()).xy((it + 7) * tileSize, tileSize) }
    val gameModelInteractor = GameModelInteractor()
    val helper = StageHelper(this, world, gameModelInteractor, flasks, expFlasks)
    helper.addSprites()
    helper.addControlKeys()
    helper.setUpHealthPointBar()
    helper.setUpExpPointBar()

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
                        helper.updateHealthBarState(hp, maxHp)
                        helper.updateExpBarState(level)
                    })

                    world.tiles.forEach { it ->
                        it.updateSprites(world.timeSpeed)
                    }

                    world.entities.forEach {
                        it.updateSprite(world.tiles[it.pos].lit, world.timeSpeed)
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


suspend fun textureWork(spriteController: SpriteController) {
    spriteController.initBitmaps()
    spriteController.setUpEntityAnimations()
    spriteController.initTileTypes()
    spriteController.initDecors()
}

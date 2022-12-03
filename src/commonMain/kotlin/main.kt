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



suspend fun main() = Korge(width = tileSize * mapWidth, height = tileSize * mapHeight, bgcolor = Colors["#2b2b2b"]) {
    val spriteController = SpriteController()
    textureWork(spriteController)
    val world = WorldBuilder().build()
    val flasks: Array<Sprite> = Array(6) { Sprite(spriteController.getHealth()).xy(it * tileSize, tileSize) }
    val expFlasks: Array<Sprite> = Array(6) { Sprite(spriteController.getExp()).xy((it + 7) * tileSize, tileSize) }

    val helper = StageHelper(this, world, flasks, expFlasks)
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


    world.recalculateLight()
    addFixedUpdater(GameConfig.worldUpdateRate.timesPerSecond) {
        when(world.gameState) {
            World.GameState.Active -> {
                val playerHealth = world.player.getHp()
                helper.updateHealthBarState(playerHealth, world.player.type.hp)
                helper.updateExpBarState(world.player.getLevel())
                world.passTime()

                world.tiles.forEach { it ->
                    it.updateSprites(world.timeSpeed)
                }

                world.entities.forEach {
                    it.updateSprite(world.tiles[it.pos].lit, world.timeSpeed)
                }
                world.removeDeadEntities()
                world.recalculateLight()
                if(!world.player.isAlive())
                    world.gameState = World.GameState.Lost
                else if(world.entities.none { !it.player })
                    world.gameState = World.GameState.Won
            }
            World.GameState.Won -> {
                winText.visible = true
            }
            World.GameState.Lost -> {
                lostText.visible = true
            }
        }
    }
}


suspend fun textureWork(spriteController: SpriteController) {
    spriteController.initBitmaps()
    spriteController.setUpEntityAnimations()
    spriteController.initTileTypes()
    spriteController.initDecors()
}

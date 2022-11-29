import GameConfig.mapHeight
import GameConfig.mapWidth
import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import mapgen.*
import resource_controllers.*
import stage_utils.*

lateinit var health: SpriteAnimation
lateinit var exp: SpriteAnimation
//var gameState: GameState = GameState.PLAYING


suspend fun main() = Korge(width = tileSize * mapWidth, height = tileSize * mapHeight, bgcolor = Colors["#2b2b2b"]) {
    textureWork()
    val world = WorldGenerator().generateMap()
    val flasks: Array<Sprite> = Array(6) { Sprite(health).xy(it * tileSize, tileSize) }
    val expFlasks: Array<Sprite> = Array(6) { Sprite(exp).xy((it + 7) * tileSize, tileSize) }

    val helper = StageHelper(this, world, flasks, expFlasks)
    helper.addSprites()
    helper.addControlKeys()
    helper.setUpHealthPointBar()
    helper.setUpExpPointBar()
    // Win / Loss Message
    val winText = text("YOU WIN", textSize = 24.0).xy(400, 500)
    winText.visible = false
    val lostText = text("YOU LOSE", textSize = 24.0).xy(400, 500)
    lostText.visible = false

    text("Life").xy(0, 0)
    text("Exp").xy(7 * tileSize, 0)

    // HP flasks


    world.recalculateLight()
    addFixedUpdater(60.timesPerSecond) {

        //winText.visible = gameState == GameState.WON
        //lostText.visible = gameState == GameState.LOST

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
    }
}


suspend fun textureWork() {
    val spriteController = SpriteController()
    spriteController.initBitmaps()
    spriteController.setUpEntityAnimations()
    spriteController.initTileTypes()
    spriteController.initDecors()
    health = spriteController.getHealth()
    exp = spriteController.getExp()
}

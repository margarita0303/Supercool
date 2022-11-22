import GameConfig.mapHeight
import GameConfig.mapWidth
import GameConfig.tileSize
import com.soywiz.klock.*
import com.soywiz.korau.sound.*
import com.soywiz.korge.*
import com.soywiz.korge.view.*
import com.soywiz.korim.color.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import gamemodel.world.*
import mapgen.*
import resource_controllers.*
import stage_utils.*

lateinit var health: SpriteAnimation
lateinit var exp: SpriteAnimation
//var gameState: GameState = GameState.PLAYING


suspend fun main() = Korge(width = tileSize * mapWidth, height = tileSize * mapHeight, bgcolor = Colors["#2b2b2b"]) {
    textureWork()
    val world = generateMap()
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
            it.sprite.visible = it.lit || it.wasLit
            it.sprite.colorMul = if (!it.lit && it.wasLit) Colors.DARKGRAY else Colors.WHITE
            it.sprite.playAnimationLooped(it.tileType.animation, spriteDisplayTime = 250.milliseconds)

            it.decorSprite?.visible = it.lit || it.wasLit
            it.decorSprite?.colorMul = if (!it.lit && it.wasLit) Colors.DARKGRAY else Colors.WHITE
            it.decorSprite?.playAnimationLooped(it.decor?.animation, spriteDisplayTime = 250.milliseconds)
        }

        world.entities.forEach {

            // This does linear interpolation for
            val diff = Point(
                (it.pos.x * tileSize).toDouble() - it.sprite.x,
                (it.pos.y * tileSize).toDouble() - it.sprite.y
            )
            if (diff.length > 3) {
                diff.normalize()
                diff.mul(3.0)
            }

            it.sprite.x += diff.x
            it.sprite.y += diff.y

            it.sprite.visible = world.tiles[it.pos].lit
            if (it.isAlive()) {
                val animSpeedCoef = if(it.player) 1.0 else world.timeSpeed
                if (diff.length > 2) {
                    it.sprite.playAnimationLooped(
                        it.type.moveAnimation,
                        spriteDisplayTime = (it.type.timeForMove * 1000 / animSpeedCoef).milliseconds
                    )
                } else {
                    it.sprite.playAnimationLooped(it.type.standAnimation, spriteDisplayTime = (250 / animSpeedCoef).milliseconds)
                }
            } else {
                it.sprite.stopAnimation()
                it.sprite.visible = false
            }
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

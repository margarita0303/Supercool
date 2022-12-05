package resource_controllers

import GameConfig.tileSize
import com.soywiz.kds.iterators.*
import com.soywiz.klock.*
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.color.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.*
import game.world.*
import gamemodel.world.*

class SpriteController(private val stage: Stage) {

    private lateinit var tileMap: Bitmap
    private lateinit var spriteSheet: Bitmap
    private val entityTypeSpriteAnimations: MutableMap<EntityType, EntityTypeSpriteAnimations> = mutableMapOf()
    private val entitySprites: MutableMap<Entity, Sprite> = mutableMapOf()
    private val tileTypeSpriteAnimations: MutableMap<TileType, SpriteAnimation> = mutableMapOf()
    private val decorSpriteAnimations: MutableMap<Decor, SpriteAnimation> = mutableMapOf()
    private val cellSprites: MutableMap<Cell, Sprite> = mutableMapOf()
    private val cellDecorSprites: MutableMap<Cell, Sprite?> = mutableMapOf()
    private val itemsSpriteAnimations: MutableMap<Item, SpriteAnimation> = mutableMapOf()
    private val itemsSprites: MutableMap<Collectable, Sprite> = mutableMapOf()

    private var flasks: Array<Sprite> = arrayOf()
    private var expFlasks: Array<Sprite> = arrayOf()

    private lateinit var armor: Bitmap
    private lateinit var weapon: Bitmap

    suspend fun initBitmaps() {
        /**
         * This function must be called every time before any other method is used
         */
        tileMap = resourcesVfs["tilemap${tileSize}px.png"].readBitmap()
        spriteSheet = resourcesVfs["spritesheet${tileSize}px.png"].readBitmap()
        armor = resourcesVfs["armor${tileSize}px.png"].readBitmap()
        weapon = resourcesVfs["weapon${tileSize}px.png"].readBitmap()
    }

    private fun getAnimationOfTileSize(
        bitmap: Bitmap,
        indexTop: Int,
        indexLeft: Int,
        columns: Int = 1,
        rows: Int = 1,
    ): SpriteAnimation {
        return SpriteAnimation(bitmap, tileSize, tileSize, indexTop * tileSize, indexLeft * tileSize, columns, rows)
    }


    fun setUpCellsSprites(world: World) {
        TileType.values().forEach {
            when (it) {
                TileType.WALL -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 4, 7)
                TileType.DIRT -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 2, 5)
                TileType.FLOOR -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 1, 3)
                TileType.DOOR_CLOSED -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 5, 7)
                TileType.DOOR_OPEN -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 4, 6)
                TileType.BOOKSHELF -> tileTypeSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 0, 2)
            }
        }
        Decor.values().forEach {
            when (it) {
                Decor.TABLE -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 1, 5)
                Decor.WALL_SKELETON -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 1, 4)
                Decor.GREEN_BANNER -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 0, 3)
                Decor.RED_BANNER -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 0, 4)
                Decor.BARREL -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 0, 1)
                Decor.GOLD -> decorSpriteAnimations[it] = getAnimationOfTileSize(tileMap, 0, 0)
                Decor.TORCH -> decorSpriteAnimations[it] = getAnimationOfTileSize(spriteSheet, 3, 6, 6, 1)
                Decor.CHEST -> decorSpriteAnimations[it] = getAnimationOfTileSize(spriteSheet, 1, 6, 8, 1)
                Decor.CHEST_OPEN -> decorSpriteAnimations[it] = getAnimationOfTileSize(spriteSheet, 1, 14)
            }
        }

        world.tiles.forEach { tile ->
            val tileTypeAnimation = tile.getTileTypeAnimation()
            val decorAnimation = tile.getDecorAnimation()
            val tileSprite = Sprite(tileTypeAnimation).xy(tile.x * tileSize, tile.y * tileSize)
            val decorSprite = decorAnimation?.let { Sprite(it).xy(tile.x * tileSize, tile.y * tileSize) }
            cellSprites[tile] = tileSprite
            cellDecorSprites[tile] = decorSprite
            stage.addChild(tileSprite)
            decorSprite?.let { stage.addChild(it) }
        }
    }

    fun updateTileSprites(tile: Cell, timeSpeed: Double) {
        with(tile) {
            val tileTypeAnimation = tile.getTileTypeAnimation()
            val decorAnimation = tile.getDecorAnimation()
            val sprite = tile.getTileSprite()
            val decorSprite = tile.getDecorSprite()
            sprite.visible = lit || wasLit
            sprite.colorMul = if (!lit && wasLit) Colors.DARKGRAY else Colors.WHITE
            sprite.playAnimationLooped(tileTypeAnimation, spriteDisplayTime = 500.milliseconds / timeSpeed)

            decorSprite?.visible = lit || wasLit
            decorSprite?.colorMul = if (!lit && wasLit) Colors.DARKGRAY else Colors.WHITE
            decorSprite?.playAnimationLooped(
                decorAnimation,
                spriteDisplayTime = 500.milliseconds / timeSpeed
            )
        }
    }

    fun setUpEntitySprites(world: World) {
        EntityType.values().forEach {
            when (it) {
                EntityType.Player -> {
                    entityTypeSpriteAnimations[it] = EntityTypeSpriteAnimations(
                        standAnimation = getAnimationOfTileSize(spriteSheet, 5, 0, 6, 1),
                        moveAnimation = getAnimationOfTileSize(spriteSheet, 6, 0, 6, 1)
                    )
                }
                EntityType.Enemy -> {
                    entityTypeSpriteAnimations[it] = EntityTypeSpriteAnimations(
                        standAnimation = getAnimationOfTileSize(spriteSheet, 5, 0, 6, 1),
                        moveAnimation = getAnimationOfTileSize(spriteSheet, 6, 0, 6, 1)
                    )
                }
            }
        }
        world.entities.forEach {
            val typeSpriteAnimation = it.getTypeSpriteAnimations()
            val sprite = Sprite(typeSpriteAnimation.standAnimation).xy(it.pos.x * tileSize, it.pos.y * tileSize)
            entitySprites[it] = sprite
            stage.addChild(sprite)
        }
    }

    fun updateEntitySprite(entity: Entity, isPositionLit: Boolean, timeSpeed: Double) {
        val sprite = entity.getSprite()
        val typeSpriteAnimations = entity.getTypeSpriteAnimations()

        val diff = Point(
            (entity.pos.x * tileSize).toDouble() - sprite.x,
            (entity.pos.y * tileSize).toDouble() - sprite.y
        )
        if (diff.length > 3) {
            diff.normalize()
            diff.mul(3.0)
        }


        sprite.x += diff.x
        sprite.y += diff.y

        sprite.visible = isPositionLit
        if (entity.isAlive()) {
            val animSpeedCoef = if (entity.player) 1.0 else timeSpeed
            if (diff.length > 2) {
                sprite.playAnimationLooped(
                    typeSpriteAnimations.moveAnimation,
                    spriteDisplayTime = (entity.type.timeForMove * 1000 / animSpeedCoef).milliseconds
                )
            } else {
                sprite.playAnimationLooped(
                    typeSpriteAnimations.standAnimation,
                    spriteDisplayTime = (250 / animSpeedCoef).milliseconds
                )
            }
        } else {
            sprite.stopAnimation()
            sprite.visible = false
        }
    }


    fun setUpCollectableSprites(world: World) {
        EquipmentItem.values().forEach {
            when (it) {
                EquipmentItem.HELMET -> itemsSpriteAnimations[it] = getAnimationOfTileSize(armor, 0, 0)
                EquipmentItem.BODY_ARMOR -> itemsSpriteAnimations[it] = getAnimationOfTileSize(armor, 0, 2)
                EquipmentItem.SHOES -> itemsSpriteAnimations[it] = getAnimationOfTileSize(armor, 0, 4)
            }
        }

        WeaponItem.values().forEach {
            when (it) {
                WeaponItem.SWORD -> itemsSpriteAnimations[it] = getAnimationOfTileSize(weapon, 1, 0)
                WeaponItem.AX -> itemsSpriteAnimations[it] = getAnimationOfTileSize(weapon, 7, 5)
                WeaponItem.TRIDENT -> itemsSpriteAnimations[it] = getAnimationOfTileSize(weapon, 1, 10)
            }
        }
        world.collectables.forEach {
            val itemSpriteAnimation = it.getSpriteAnimation()
            val sprite = Sprite(itemSpriteAnimation).xy(it.pos.x * tileSize, it.pos.y * tileSize)
            itemsSprites[it] = sprite
            stage.addChild(sprite)
        }
    }

    fun updateCollectableSprite(collectable: Collectable, timeSpeed: Double) {
        val itemAnimation = collectable.getSpriteAnimation()
        val itemSprite = collectable.getSprite()
        if (collectable.exists) {
            itemSprite.playAnimationLooped(
                itemAnimation,
                spriteDisplayTime = 500.milliseconds / timeSpeed
            )
        } else {
            itemSprite.stopAnimation()
            itemSprite.visible = false
        }
    }

    private fun getHealthSprite(): SpriteAnimation {
        return getAnimationOfTileSize(tileMap, 0, 7)
    }

    private fun getExpSprite(): SpriteAnimation {
        return getAnimationOfTileSize(tileMap, 0, 8)
    }


    fun setUpHealthPointBar() {
        flasks = Array(6) { Sprite(getHealthSprite()).xy(it * tileSize, tileSize) }

        with(stage) {
            flasks.forEach { addChild(it) }
        }
    }

    fun setUpExpPointBar() {
        expFlasks = Array(6) { Sprite(getExpSprite()).xy((it + 7) * tileSize, tileSize) }

        with(stage) {
            expFlasks.forEach { addChild(it) }
        }
    }

    fun updateHealthBarState(playerHealth: Int, maxPlayerHealth: Int) {
        val normalizedHp = kotlin.math.ceil((playerHealth / maxPlayerHealth.toDouble()) * flasks.size)
        flasks.fastForEachWithIndex { index, flask ->
            flask.visible = index < normalizedHp
            flask.playAnimationLooped()
        }
    }

    fun updateExpBarState(playerLevel: Int) {
        expFlasks.fastForEachWithIndex { index, flask ->
            flask.visible = index < playerLevel
            flask.playAnimationLooped()
        }
    }

    private fun Entity.getSprite(): Sprite {
        return entitySprites.getOrPut(
            this
        ) {
            val typeSpriteAnimations = getTypeSpriteAnimations()
            val sprite = Sprite(
                typeSpriteAnimations.standAnimation
            ).xy(pos.x * tileSize, pos.y * tileSize)
            stage.addChild(sprite)
            sprite
        }
    }

    private fun Entity.getTypeSpriteAnimations(): EntityTypeSpriteAnimations {
        return entityTypeSpriteAnimations[type] ?: throw IllegalStateException()
    }

    private fun Collectable.getSprite(): Sprite {
        return itemsSprites.getOrPut(
            this
        ) {
            val itemSpriteAnimation = getSpriteAnimation()
            val sprite = Sprite(
                itemSpriteAnimation
            ).xy(pos.x * tileSize, pos.y * tileSize)
            stage.addChild(sprite)
            sprite
        }
    }

    private fun Collectable.getSpriteAnimation(): SpriteAnimation {
        return itemsSpriteAnimations[item] ?: throw IllegalStateException()
    }

    private fun Cell.getTileSprite(): Sprite {
        return cellSprites.getOrPut(
            this
        ) {
            val tileTypeAnimation = getTileTypeAnimation()
            val sprite = Sprite(tileTypeAnimation).xy(x * tileSize, y * tileSize)
            stage.addChild(sprite)
            sprite
        }
    }

    private fun Cell.getDecorSprite(): Sprite? {
        return cellDecorSprites.getOrPut(
            this
        ) {
            val decorAnimation = getDecorAnimation()
            decorAnimation?.let { decor ->
                val sprite = Sprite(decor).xy(x * tileSize, y * tileSize)
                stage.addChild(sprite)
                sprite
            }
        }
    }

    private fun Cell.getTileTypeAnimation(): SpriteAnimation {
        return tileTypeSpriteAnimations[tileType] ?: throw IllegalStateException()
    }

    private fun Cell.getDecorAnimation(): SpriteAnimation? {
        return decor?.let { decorSpriteAnimations[it] }
    }

    data class EntityTypeSpriteAnimations(val standAnimation: SpriteAnimation, val moveAnimation: SpriteAnimation)
}

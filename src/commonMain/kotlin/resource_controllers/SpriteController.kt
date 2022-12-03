package resource_controllers

import GameConfig.tileSize
import com.soywiz.korge.view.*
import com.soywiz.korim.bitmap.*
import com.soywiz.korim.format.*
import com.soywiz.korio.file.std.*
import game.world.*
import gamemodel.world.*

class SpriteController {

    private lateinit var tileMap: Bitmap
    private lateinit var spriteSheet: Bitmap


    suspend fun initBitmaps() {
        /**
         * This function must be called every time before any other method is used
         */
        tileMap = resourcesVfs["tilemap${tileSize}px.png"].readBitmap()
        spriteSheet = resourcesVfs["spritesheet${tileSize}px.png"].readBitmap()
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

    fun initTileTypes() {
        TileType.FLOOR.animation = getAnimationOfTileSize(tileMap, 1, 3)
        TileType.WALL.animation = getAnimationOfTileSize(tileMap, 4, 7)
        TileType.DIRT.animation = getAnimationOfTileSize(tileMap, 2, 5)
        TileType.BOOKSHELF.animation = getAnimationOfTileSize(tileMap, 0, 2)
        TileType.DOOR_CLOSED.animation = getAnimationOfTileSize(tileMap, 5, 7)
        TileType.DOOR_OPEN.animation = getAnimationOfTileSize(tileMap, 4, 6)
    }

    fun initDecors() {
        Decor.TABLE.animation = getAnimationOfTileSize(tileMap, 1, 5)
        Decor.WALL_SKELETON.animation = getAnimationOfTileSize(tileMap, 1, 4)
        Decor.GREEN_BANNER.animation = getAnimationOfTileSize(tileMap, 0, 3)
        Decor.RED_BANNER.animation = getAnimationOfTileSize(tileMap, 0, 4)
        Decor.BARREL.animation = getAnimationOfTileSize(tileMap, 0, 1)
        Decor.GOLD.animation = getAnimationOfTileSize(tileMap, 0, 0)
        Decor.TORCH.animation = getAnimationOfTileSize(spriteSheet, 3, 6, 6, 1)
        Decor.CHEST.animation = getAnimationOfTileSize(spriteSheet, 1, 6, 8, 1)
        Decor.CHEST_OPEN.animation = getAnimationOfTileSize(spriteSheet, 1, 14)
    }

    fun getHealth(): SpriteAnimation {
        return getAnimationOfTileSize(tileMap, 0, 7)
    }

    fun getExp(): SpriteAnimation {
        return getAnimationOfTileSize(tileMap, 0, 8)
    }

    fun setUpEntityAnimations() {
        EntityType.Player.apply {
            standAnimation = getAnimationOfTileSize(spriteSheet, 5, 0, 6, 1)
            moveAnimation = getAnimationOfTileSize(spriteSheet, 6, 0, 6, 1)
        }
        EntityType.Enemy.apply {
            standAnimation = getAnimationOfTileSize(spriteSheet, 5, 0, 6, 1)
            moveAnimation = getAnimationOfTileSize(spriteSheet, 6, 0, 6, 1)
        }
    }
}

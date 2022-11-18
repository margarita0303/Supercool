package game.world

import com.soywiz.korge.view.*

enum class TileType(val blocks: Boolean, val transparent: Boolean) {
    WALL(true,  false),
    DIRT(true,  true),
    FLOOR(false,  true),
    DOOR_CLOSED(true,  false),
    DOOR_OPEN(false,  true),
    BOOKSHELF(true,  false);

    lateinit var animation: SpriteAnimation
}

// decor goes over tiles, and modifies it?
enum class Decor(val blocks: Boolean) {
    TABLE(true),
    WALL_SKELETON(true),
    GREEN_BANNER(true),
    RED_BANNER(true),
    BARREL(true),
    GOLD(true),
    TORCH(true),
    CHEST(true),
    CHEST_OPEN(true);

    lateinit var animation: SpriteAnimation
}

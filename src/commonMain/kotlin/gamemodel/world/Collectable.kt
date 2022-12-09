package gamemodel.world

import GameConfig.tileSize
import com.soywiz.korge.view.*
import game.world.*
import gamemodel.behavior.*
import math.*

/**
 * Represents collectable item on the map
 * */
class Collectable (var pos: Vec2, var item: Item, var exists: Boolean = true)


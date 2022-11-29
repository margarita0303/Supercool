package gamemodel.world

import GameConfig.tileSize
import com.soywiz.korge.view.*
import game.world.*
import gamemodel.behavior.*
import math.*

class Collectable (
    var pos: Vec2,
    val weaponItem: WeaponItem?,
    val equipmentItem: EquipmentItem?,
    val animation: SpriteAnimation = weaponItem?.animation ?: if (equipmentItem != null) equipmentItem.animation else throw NullPointerException(),
    val sprite: Sprite = Sprite(animation).xy(pos.x * tileSize, pos.y * tileSize),
    )
{

}

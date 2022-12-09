import game.world.*
import gamemodel.action.*
import math.*
import kotlin.test.*

class ActionTests {
    private val testWorldConfig = TestWorldConfig()
    private val player = testWorldConfig.player
    private val enemy = testWorldConfig.enemy
    private val testWorld = testWorldConfig.testWorld

    private fun resetPlayerPos() {
        player.pos = Vec2(2, 2)
    }

    private fun resetEnemyPos() {
        player.pos = Vec2(2, 3)
    }

    @Test
    fun testInteractWithDoors() {
        player.pos = Vec2(2, 1)
        val res = InteractWithDoors().onPerform(testWorld, player)
        assertEquals(res, Succeeded)
        assertEquals(testWorld.tiles[Vec2(2, 0)].tileType, TileType.DOOR_OPEN)
        resetPlayerPos()
    }

    @Test
    fun testMelee() {
        val res1 = Melee(enemy).onPerform(testWorld, player)
        assertEquals(res1, Succeeded)
        player.pos = Vec2(4, 4)
        val res2 = Melee(enemy).onPerform(testWorld, player)
        assertEquals(res2, Failed)
        resetPlayerPos()
    }

    @Test
    fun testWalk() {
        val res1 = Walk(Vec2(-1, 0)).onPerform(testWorld, player)
        assertEquals(res1, Succeeded)
        val res2 = Walk(Vec2(-1, 0)).onPerform(testWorld, player)
        assertEquals(res2, Failed)
        resetPlayerPos()
    }

    @Test
    fun testWalkToPoint() {
        enemy.pos = Vec2(2, 4)
        val res = WalkToPoint(player.pos).onPerform(testWorld, enemy)
        assertEquals(res, Succeeded)
        assertEquals(enemy.pos, Vec2(2, 3))
    }

    @Test
    fun testWalkAwayFromPoint() {
        val res = WalkAwayFromPoint(player.pos).onPerform(testWorld, enemy)
        assertEquals(res, Succeeded)
        assertEquals(enemy.pos, Vec2(2, 4))
        resetEnemyPos()
    }
}

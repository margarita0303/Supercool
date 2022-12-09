import gamemodel.behavior.*
import math.*
import org.jetbrains.annotations.TestOnly
import kotlin.test.*

class BehaviorTests {
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
    fun testAggressiveBehavior() {
        enemy.pos = Vec2(2, 4)
        enemy.behavior = AggressiveBehavior()
        val hpBefore = player.getHp()
        testWorld.passTime()
        enemy.movingDelay = 0.0
        testWorld.passTime()
        val hpAfter = player.getHp()
        assertEquals(enemy.pos, Vec2(2, 3))
        assert(hpAfter < hpBefore)
    }

    @Test
    fun testFearfulBehavior() {
        enemy.behavior = FearfulBehavior()
        val hpBefore = player.getHp()
        testWorld.passTime()
        val hpAfter = player.getHp()
        assertEquals(enemy.pos, Vec2(2, 4))
        assert(hpAfter == hpBefore)
        resetEnemyPos()
    }

    @Test
    fun testPassiveBehavior() {
        enemy.behavior = PassiveBehavior()
        val hpBefore = player.getHp()
        testWorld.passTime()
        val hpAfter = player.getHp()
        assertEquals(enemy.pos, Vec2(2, 3))
        assert(hpAfter == hpBefore)
    }
}

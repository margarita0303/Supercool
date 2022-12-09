import gamemodel.world.*
import kotlin.test.*


class EntityTests {
    private val testWorldConfig = TestWorldConfig()
    private val player = testWorldConfig.player
    private val enemy = testWorldConfig.enemy
    private val testWorld = testWorldConfig.testWorld
    @Test
    fun testPlusExp() {
        val prevLevel = player.getLevel()
        player.plusExp(Entity.levelExp)
        val nextLevel = player.getLevel()
        player.plusExp(Entity.levelExp)
        player.plusExp(Entity.levelExp)
        player.plusExp(Entity.levelExp)
        player.plusExp(Entity.levelExp)
        player.plusExp(Entity.levelExp)
        player.plusExp(Entity.levelExp)
        assertEquals(prevLevel, 1)
        assertEquals(nextLevel, 2)
        assertEquals(player.getLevel(), Entity.maxLevel)
    }
}

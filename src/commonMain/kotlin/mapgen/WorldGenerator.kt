package mapgen

import DrawCell2d
import GameConfig.mapHeight
import GameConfig.mapWidth
import GameConfig.tileSize
import com.soywiz.korge.view.*
import game.world.*
import gamemodel.behavior.*
import gamemodel.world.*
import mapgen.predicate.*
import math.*
import mathutils.*
import kotlin.math.*
import kotlin.random.*

class WorldGenerator {

    private fun coinFlip() = Random.nextBoolean()


    private val enemies = listOf(EntityType.Striker)

    fun generateMap(): World {
        val player = Entity(Vec2(7, 7), EntityType.Player, NoAiBehavior(), player = true, blocks = true, stabber = true)
        val map = Matrix2d(mapWidth, mapHeight) { _, _ -> TileType.DIRT }
        val decor = Matrix2d<Decor?>(map.getSize()) { _, _ -> null }

        val roomCenters = mutableListOf<Vec2>()
        val entities = mutableListOf(player)

        // dig rooms and find centers
        (1..MAX_ROOMS).forEach { _ ->
            val w = (ROOM_MIN_SIZE..ROOM_MAX_SIZE).random()
            val h = (ROOM_MIN_SIZE..ROOM_MAX_SIZE).random()

            val center = Vec2(
                (w + 1 until map.xSize - w).random(),
                (h + 1 until map.ySize - h).random()
            )

            if (digRoom(map, TileType.DIRT, TileType.FLOOR, center, w, h, TileType.WALL)) {
                roomCenters.add(center)
            }

        }

        // set anything touching the rooms into a wall
        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.DIRT),
                    CellNearCell2d(TileType.FLOOR)
                )
            ),
            DrawCell2d(TileType.WALL)
        )

        // room wall decals
        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.WALL),
                    CellNearCell2d(TileType.FLOOR),
                    RandomPredicate2d(0.05f)
                )
            )
        ) { x: Int, y: Int ->
            decor[x, y] = arrayOf(Decor.WALL_SKELETON, Decor.TORCH, Decor.GREEN_BANNER, Decor.RED_BANNER).random()
        }

        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.FLOOR),
                    CellNotNearCell2d(TileType.WALL),
                    RandomPredicate2d(0.05f)
                )
            )
        ) { x: Int, y: Int ->
            decor[x, y] = arrayOf(Decor.TABLE, Decor.BARREL).random()
        }

        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.FLOOR),
                    CellNearCell2d(TileType.WALL),
                    RandomPredicate2d(0.05f)
                )
            ),
            DrawCell2d(TileType.BOOKSHELF)
        )


        // Link them up.  Dig tunnels using a*
        // anything that is a wall gets turned to a door

        // Cutting paths
        // Link them up.  Dig tunnels using a*
        // anything that is a wall gets turned to a door

        for (roomCenterFrom in roomCenters) {
            for (roomCenterTo in roomCenters) {

                val fullPath2d = findPath2d(
                    size = map.getSize(),
                    cost = {
                        if (map[it].transparent) {
                            1.0
                        } else {
                            10000.0
                        }
                    },
                    heuristic = { one, two -> getEuclideanDistance(one, two) },
                    neighbors = { our ->
                        our.vonNeumanNeighborhood().filter { vec ->
                            map.contains(vec)
                        }
                    },
                    start = roomCenterFrom,
                    end = roomCenterTo
                )

//            val fullPath2d: FullPath2d = pathFinder2d.findPath(hallwayMover2d, roomCenterFrom.x, roomCenterFrom.y, roomCenterTo.x, roomCenterTo.y)
                if (fullPath2d != null) {
                    for (currentStep in fullPath2d) {
                        val tile: TileType = map[currentStep]
                        if (tile === TileType.WALL) {
                            map[currentStep] = TileType.DOOR_CLOSED
                            decor[currentStep] = null
                        } else if (tile !== TileType.DOOR_CLOSED && tile !== TileType.DOOR_OPEN) {
                            map[currentStep] = TileType.FLOOR
                            decor[currentStep] = null
                        }
                    }
                }
            }
        }

        // Gets rid of double doors
        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.DOOR_CLOSED),
                    CellNearCell2d(TileType.DOOR_CLOSED)
                )
            ),
            DrawCell2d(TileType.FLOOR)
        )

        //surround any halls with wall
        map.fill(
            AndPredicate2d(
                arrayOf(
                    CellEquals2d(TileType.DIRT),
                    CellNearCell2d(TileType.FLOOR)
                )
            ),
            DrawCell2d(TileType.WALL)
        )

        // Fill rooms with monstrosities
        roomCenters.forEachIndexed { index, center ->
            if (index == 0) {
                //set player start
                player.pos = center
                player.sprite.xy(center.x * tileSize, center.y * tileSize)
            } else {
                //TODO: add objects
                //placeObjects(map, newRoom,  MAX_ROOM_MONSTERS, MAX_ROOM_ITEMS)
                val surrounds = center.mooreNeighborhood().filter { !map[it].blocks }.shuffled()
                repeat((0 until min(4, surrounds.size)).random()) {
                    entities += Entity(surrounds[it], enemies.random(), AggressiveBehavior(), blocks = true)
                }
            }
        }

        // place the end as far as possible away from the start
        val furthest = roomCenters.maxBy { getEuclideanDistance(roomCenters[0], it) }
        decor[furthest] = Decor.CHEST

        return World(
            tiles = Matrix2d(map.getSize()) { x, y ->
                Cell(x, y, map[x, y], decor[x, y])
            },
            entities = entities,
            player = player
        )
    }

    private fun digRoom(
        mapLevel: Matrix2d<TileType>,
        sourceType: TileType,
        destinationType: TileType,
        center: Vec2,
        xRadius: Int,
        yRadius: Int,
        column: TileType,
    ): Boolean {
        val xMin: Int = center.x - xRadius
        val xMax: Int = center.x + xRadius
        val yMin: Int = center.y - yRadius
        val yMax: Int = center.y + yRadius
        return if (coinFlip() && coinFlip()) {
            //circular room
            val radius: Int = min(xRadius, yRadius)

            for (x in xMin - 2..xMax) {
                for (y in yMin - 2..yMax) {
                    if (getEuclideanDistance(
                            x.toDouble(),
                            y.toDouble(),
                            center.x.toDouble(),
                            center.y.toDouble()
                        ) <= radius + 1 && mapLevel.get(x, y) !== sourceType
                    )
                        return false
                }
            }
            for (x in xMin - 1..xMax) {
                for (y in yMin - 1..yMax) {
                    if (getEuclideanDistance(
                            x.toDouble(),
                            y.toDouble(),
                            center.x.toDouble(),
                            center.y.toDouble()
                        ) <= radius
                    )
                        mapLevel[x, y] = destinationType
                }
            }
            true
        } else {

            //TODO: pillars?  Larger rooms should have a higher pillar likelyhood?
            val pillarsOnWalls: Boolean = coinFlip()
            val vSpacing: Int = (3..5).random()
            val hSpacing: Int = (3..5).random()
            for (x in xMin..xMax) {
                for (y in yMin..yMax) {
                    if (mapLevel[x, y] !== sourceType) return false
                }
            }
            for (x in xMin until xMax) {
                for (y in yMin until yMax) {
                    val hori = min(x - xMin, xMax - x - 1) % hSpacing == 1
                    val vert = min(y - yMin, yMax - y - 1) % vSpacing == 1

                    mapLevel[x, y] = if (pillarsOnWalls && vert && hori) column else destinationType
                }
            }
            true
        }
    }

    companion object {
        private const val ROOM_MAX_SIZE = 6
        private const val ROOM_MIN_SIZE = 3
        private const val MAX_ROOMS = 20
    }

}

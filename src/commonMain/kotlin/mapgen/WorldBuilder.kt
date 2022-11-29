package mapgen

import gamemodel.world.*
import java.io.*

class WorldBuilder {
    sealed class Type {
        class FileGenerated : Type() {
            var file: File? = null
        }

        class ManualGenerated : Type() {
            var width: Int = 32
            var height: Int = 32
        }
    }

    private var type: Type = Type.ManualGenerated()

    fun fromFile(file: File): WorldBuilder {
        if (type is Type.ManualGenerated) {
            throw IllegalStateException("World is set to be manually generated already")
        } else {
            val currentType = type
            currentType as Type.FileGenerated
            currentType.file = file
        }
        return this
    }

    fun setWidth(width: Int): WorldBuilder {
        if (type is Type.FileGenerated) {
            throw IllegalStateException("World is set to be file generated already")
        } else {
            val currentType = type
            currentType as Type.ManualGenerated
            currentType.width = width
        }
        return this
    }

    fun setHeight(height: Int): WorldBuilder {
        if (type is Type.FileGenerated) {
            throw IllegalStateException("World is set to be file generated already")
        } else {
            val currentType = type
            currentType as Type.ManualGenerated
            currentType.height = height
        }
        return this
    }

    fun build(): World {
        return when (type) {
            is Type.FileGenerated -> readFromFile((type as Type.FileGenerated).file)
            is Type.ManualGenerated -> WorldGenerator().generateMap()
        }
    }

    private fun readFromFile(file: File?): World {
        TODO("Not yet implemented")
    }
}

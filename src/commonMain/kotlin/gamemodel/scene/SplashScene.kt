package gamemodel.scene

import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.async.launchImmediately

class SplashScene : Scene() {
    override suspend fun SContainer.sceneInit() {
        text("Dungeon")
        solidRect(100.0, 100.0, Colors.RED).position(100, 100).onClick {
            launchImmediately { sceneContainer.changeTo<SplashScene>() }
        }
    }
}

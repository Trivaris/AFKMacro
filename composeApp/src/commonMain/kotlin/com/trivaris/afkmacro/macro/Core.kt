package com.trivaris.afkmacro.macro

import com.trivaris.afkmacro.Config
import com.trivaris.afkmacro.findImage
import com.trivaris.afkmacro.scrcpy.Emulator.tap
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

object Core {

    suspend fun toMainScreen(delay: Int = Config.delay): Boolean {
        var tries = 0
        while (tries <= Config.maxTries) {
            println("Trying...")
            if (Locations.MAIN.visible) return true
            if (!back()) tries++
            else tries = 0
            delay(delay.milliseconds)
        }
        return false
    }

    suspend fun back() =
        tap("buttons/back") ||
        tap("buttons/back2")
}


enum class Locations(val fileName: String) {
    MAIN("labels/sunandstars"),
    HONOR("tbd/honor");

    val visible: Boolean
        get() = findImage(fileName) != null
}
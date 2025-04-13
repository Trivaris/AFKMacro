package com.trivaris.afkmacro.macro.honor

import com.trivaris.afkmacro.findAllImage
import com.trivaris.afkmacro.macro.Core
import com.trivaris.afkmacro.macro.Locations
import com.trivaris.afkmacro.scrcpy.Emulator.tap
import com.trivaris.afkmacro.midPoint
import com.trivaris.afkmacro.plus
import org.opencv.core.Point
import org.opencv.imgcodecs.Imgcodecs
import java.io.File

object HonorMacro {
    val neededHeroes = mutableListOf<String>("sinbad")
    val neededItems = mutableListOf<String>()
    val possesedHeroes = mutableMapOf<String, Int>()
    val possessedItems = mutableListOf<String>()

    var state = HonorState.UNKNOWN

    suspend fun openHonor(): Boolean {
        if (Locations.HONOR.visible)
            return true

        Core.toMainScreen()
        tap("tbd/battle_modes")
        tap("tbd/fair_play")

        val success = Locations.HONOR.visible
        if (success)
            state = HonorState.MAIN

        return success
    }

    suspend fun start(): Boolean {
        state = HonorState.ARTIFACT_SELECTION
        return tap("tbd/play_honor")
    }

    suspend fun selectArtifact() {
        //if (state != HonorState.ARTIFACT_SELECTION)
            //return
        val found = findAllImage("tbd/select_artifact")
        tap(found.sortedBy { it.y }[0])
        state = HonorState.SHOP
    }

    suspend fun scanShop() {
        //if (state != HonorState.SHOP)
            //return
        val found = mutableListOf<Point>()
        neededHeroes.forEach { hero ->
            val heroImg = Imgcodecs.imread(File("img/tbd/heroes/$hero.png").absolutePath)
            val heroMiddle = heroImg.midPoint()
            if (heroMiddle != null)
                found.add(heroMiddle + Point(0.0, 10 + (0.5 * heroImg.height())))
        }
        found.forEach {
            tap(it)
        }
    }

}

enum class HonorState() {
    ARTIFACT_SELECTION,
    SHOP,
    ITEM_SELECTION,
    FORMATION_1,
    FORMATION_2,
    BATTLE,
    MAIN,
    UNKNOWN;
}
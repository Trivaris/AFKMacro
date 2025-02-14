package com.trivaris.afkmacro.macro.honor

import com.trivaris.afkmacro.macro.Core
import com.trivaris.afkmacro.macro.Locations
import com.trivaris.afkmacro.scrcpy.Emulator.tap
import com.trivaris.afkmacro.scrcpy.Emulator.tapText

object HonorMacro {
    suspend fun openHonor(): Boolean {
        if (Locations.HONOR.visible) return true
        Core.toMainScreen(1500)
        tapText("Battle", 1500)
        tap("labels/honor_duel")

        return Locations.HONOR.visible
    }

}
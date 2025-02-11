package com.trivaris.afkmacro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AFKMacro",
    ) {
        Config.load()
        App()
    }
}
package com.trivaris.afkmacro.backend

open class ADBProcessBuilder(vararg val command: String) {
    fun start(): Process {
        return ProcessBuilder("adb", *command).start()
    }
}

class ADBShellProcessBuilder(vararg command: String) : ADBProcessBuilder("shell", *command)
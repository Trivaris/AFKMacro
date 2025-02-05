package com.trivaris.afkmacro.backend

open class SCRCPProcessBuilder(vararg val command: String) {
    fun start(): Process {
        return ProcessBuilder("scrcpy", *command).start()
    }
}
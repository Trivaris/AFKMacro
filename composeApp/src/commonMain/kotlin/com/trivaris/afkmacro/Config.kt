package com.trivaris.afkmacro

import org.json.JSONObject
import java.io.File

object Config {
    var port = 7555
    var delay = 2000
    var maxTries = 3

    fun load() {
        val file = File("AFKMacroConfig.json")

        if (!file.exists()) {
            val defaultConfig = JSONObject().apply {
                put("port", port)
                put("delay", delay )
                put("maxTries", maxTries)
            }

            file.writeText(defaultConfig.toString())
        }

        val jsonContent = file.readText()
        val jsonObject = JSONObject(jsonContent)

        port = jsonObject.getInt("port")
    }
}
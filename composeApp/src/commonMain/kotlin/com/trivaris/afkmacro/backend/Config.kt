package com.trivaris.afkmacro.backend

import org.json.JSONObject
import java.io.File

object Config {
    var port = 7555

    fun load() {
        val file = File("AFKMacroConfig.json")

        if (!file.exists()) {
            val defaultConfig = JSONObject().apply {
                put("port", port)
            }

            file.writeText(defaultConfig.toString())
        }

        val jsonContent = file.readText()
        val jsonObject = JSONObject(jsonContent)

        port = jsonObject.getInt("port")
    }
}
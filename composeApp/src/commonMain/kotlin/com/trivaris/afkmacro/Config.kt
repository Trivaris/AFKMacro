package com.trivaris.afkmacro

import org.json.JSONObject
import java.io.File

object Config {
    val configFile = File("config.json")

    var port = 7555
    var delay = 2000
    var maxTries = 3

    var stratName = "graveborn"
    val stratFile = JSONObject(File("strats/$stratName.json").readText())
    val gameData = GameData(parseHeroes(stratFile), parseFormations(stratFile))

    fun load() {
        val file = File("AFKMacroConfig.json")

        if (!file.exists()) {
            val defaultConfig = JSONObject().apply {
                put("port", port)
                put("delay", delay )
                put("maxTries", maxTries)
                put("stratName", stratName)
            }

            file.writeText(defaultConfig.toString())
        }

        val jsonContent = file.readText()
        val jsonObject = JSONObject(jsonContent)

        port = jsonObject.getInt("port")
        delay = jsonObject.getInt("delay")
        maxTries = jsonObject.getInt("maxTries")
        stratName = jsonObject.getString("stratName")
    }

    fun parseHeroes(json: JSONObject): Map<String, Hero> {
        val heroesObject = json.getJSONObject("heroes")
        return heroesObject.keys().asSequence().associateWith { key ->
            val heroName = heroesObject.getJSONObject(key).keys().next()
            Hero(id = key, name = heroName)
        }
    }

    fun parseFormations(json: JSONObject): List<Formation> {
        val formationsObject = json.getJSONObject("formations")
        return formationsObject.keys().asSequence().map { mapName ->
            val mapData = formationsObject.getJSONObject(mapName)
            val tiles = mapData.keys().asSequence().map { tile ->
                FormationTile(tileNumber = tile, heroId = mapData.getString(tile))
            }.toList()
            Formation(mapName = mapName, placements = tiles)
        }.toList()
    }

}
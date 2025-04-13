package com.trivaris.afkmacro

data class Hero(
    val name: String,
    val id: String,
    val items: MutableList<String> = mutableListOf()
)

data class FormationTile(val tileNumber: String, val heroId: String)

data class Formation(val mapName: String, val placements: List<FormationTile>)

data class GameData(val heroes: Map<String, Hero>, val formations: List<Formation>)
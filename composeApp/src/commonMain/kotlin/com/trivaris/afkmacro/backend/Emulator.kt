package com.trivaris.afkmacro.backend

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(DelicateCoroutinesApi::class)
object Emulator {

    fun connectADB() {
        GlobalScope.launch {
            withContext( Dispatchers.IO ) {
                try {
                    val process = ADBProcessBuilder("connect", "127.0.0.1:${Config.port}").start()

                    val reader = BufferedReader(InputStreamReader(process.inputStream))
                    var line: String?

                    while (reader.readLine().also { line = it } != null) {
                        if (line.toString().contains("connected to 127.0.0.1:${Config.port}")) {
                            println("Connected Successfully")
                        } else println("Something went wrong")
                    }

                }
                catch (e: Exception) { e.printStackTrace() }

                testConnection()
            }
        }
    }

    fun connectSRCCP() {
        GlobalScope.launch {
            withContext( Dispatchers.IO ) {
                try {

                } catch (e: Exception) {

                }
            }
        }
    }

    private fun testConnection(): Boolean {
        var success = false
        GlobalScope.launch {
            withContext( Dispatchers.IO ) {
                val process = ADBShellProcessBuilder("test").start()
                val reader = BufferedReader(InputStreamReader(process.inputStream))
                var line: String?

                success = reader.readLine().also { line = it } != null
            }
        }
        return success
    }

}
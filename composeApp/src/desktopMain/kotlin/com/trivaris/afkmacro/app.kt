package com.trivaris.afkmacro

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.trivaris.afkmacro.components.LoggingDisplay
import com.trivaris.afkmacro.macro.Core
import com.trivaris.afkmacro.macro.honor.HonorMacro
import com.trivaris.afkmacro.scrcpy.Emulator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.opencv.imgcodecs.Imgcodecs
import java.awt.Button
import java.io.File
import java.util.Date

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun app() {
    val logList = remember { mutableStateListOf<String>() }
    var isConnected = remember { mutableStateOf<Boolean>(Emulator.isConnected()) }

    MaterialTheme {
        Row(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(0.5f)
            ) {
                Box(
                    modifier = Modifier.weight(0.5f).fillMaxWidth().background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Emulator Info", color = Color.White)
                }

                Box(
                    modifier = Modifier.weight(0.5f).fillMaxWidth().background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        Text("Commands", color = Color.White)

                        Button( onClick = {
                            if (!isConnected.value) {
                                val status = Emulator.connect()
                                println(status)
                                isConnected.value = status.isSuccess()
                            }
                            else {
                                ProcessBuilder("adb", "disconnect").start().waitFor()
                                isConnected.value = Emulator.isConnected()
                            }

                        }) { if(!isConnected.value) Text("Connect") else Text("Disconnect") }

                        Button( onClick = {
                            GlobalScope.launch { findAllImage("tbd/select_artifact", Imgcodecs.imread(File("out/screen.png").absolutePath)) }
                        }) { Text("Testing") }

                        Button( onClick = {
                            val screenshot = Emulator.screenshot()
                            Imgcodecs.imwrite(File("out/${Date().time}-screenshot.png").absolutePath, screenshot)
                        }) { Text("Screenshot") }

                    }

                }
            }
            Box(
                modifier = Modifier.weight(0.5f).fillMaxHeight().background(Color.White).padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                LoggingDisplay(logs = logList)
            }
        }
    }
}
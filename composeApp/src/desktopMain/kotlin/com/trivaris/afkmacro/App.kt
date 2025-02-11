package com.trivaris.afkmacro

import afkmacro.composeapp.generated.resources.Res
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
import com.sun.security.jgss.InquireSecContextPermission
import com.trivaris.afkmacro.components.LoggingDisplay
import com.trivaris.afkmacro.sender.Robot
import com.trivaris.afkmacro.sender.Robot.screenshot
import java.io.File
import java.io.OutputStream
import java.io.PrintStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.imageio.ImageIO

@Composable
@Preview
fun App() {
    val logList = remember { mutableStateListOf<String>() }

//    LaunchedEffect(Unit) {
//        System.setOut(object : PrintStream(object : OutputStream() {
//            override fun write(b: Int) {}
//
//            override fun write(b: ByteArray, off: Int, len: Int) {
//                val log = String(b, off, len).trim()
//                if (log.isNotEmpty()) {
//                    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
//                    logList.add("[$timestamp] $log")
//
//                    if (logList.size > 500) {
//                        logList.removeFirst()
//                    }
//                }
//            }
//        }) {
//
//        })
//    }


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
                            Robot.clickFoundImage("debug.png")
                        }) { Text("Send Screenshot") }

                        Button( onClick = {
                            println("[OUT] Clicked!!!!")
                        }) { Text("Debug") }

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
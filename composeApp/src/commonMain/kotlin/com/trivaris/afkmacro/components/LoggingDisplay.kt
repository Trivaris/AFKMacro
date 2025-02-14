package com.trivaris.afkmacro.components

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoggingDisplay(logs: List<String>) {
    val lazyListState = rememberLazyListState()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Text(
            text = "Logging",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        SelectionContainer {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyListState
            ) {
                items(logs) { log ->
                    Text(log, color = Color.White, fontSize = 14.sp)
                }
        }
        }

        VerticalScrollbar( adapter = rememberScrollbarAdapter(lazyListState) )
    }
}
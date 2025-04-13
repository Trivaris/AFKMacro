package com.trivaris.afkmacro.scrcpy

import com.trivaris.afkmacro.Config
import com.trivaris.afkmacro.Config.port
import com.trivaris.afkmacro.findImage
import com.trivaris.afkmacro.findText
import kotlinx.coroutines.delay
import nu.pattern.OpenCV
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgcodecs.Imgcodecs
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds

object Emulator {

    init {
        val cvPath = File(".\\opencv\\opencv_java4100.dll").absolutePath
        System.load(cvPath)
        OpenCV.loadLocally()
    }
    enum class ConnectionState {
        ALREADY_CONNECTED,
        CONNECTED,
        DISCONNECTED,
        UNKNOWN;

        fun isSuccess() = (this == ALREADY_CONNECTED || this == CONNECTED)
    }

    fun connect(): ConnectionState {
        val testProcess = ProcessBuilder("adb", "devices").start()

        val devices = testProcess.outputAsList().apply { removeAt(0) }
        if (devices.any { it.contains("emulator") })
            return ConnectionState.CONNECTED

        val connectProcess = ProcessBuilder("adb", "connect", "127.0.0.1:$port")
            .redirectErrorStream(true)
            .start()

        val output = connectProcess.inputStream.bufferedReader().use { it.readText() }
        connectProcess.waitFor()

        return when {
            output.contains("already connected", ignoreCase = true) -> ConnectionState.ALREADY_CONNECTED
            output.contains("connected to", ignoreCase = true) -> ConnectionState.CONNECTED
            output.contains("cannot connect", ignoreCase = true) -> ConnectionState.DISCONNECTED
            else -> ConnectionState.UNKNOWN
        }
    }

    fun isConnected(): Boolean {
        val verifyProcess = ProcessBuilder("adb", "devices")
            .redirectErrorStream(true)
            .start()

        val output = verifyProcess.inputStream.bufferedReader().use { it.readText() }
        verifyProcess.waitFor()

        return output.contains("127.0.0.1:$port", ignoreCase = true)
    }

    fun screenshot(): Mat {
        val temp = File("temp/${Date().time}-temp.png")

        val screencapProcess = ProcessBuilder("adb", "shell", "screencap", "-p", "/sdcard/screenshot.png")
            .start()
        screencapProcess.waitFor()

        val pullProcess = ProcessBuilder("adb", "pull", "/sdcard/screenshot.png", temp.absolutePath)
            .start()
        pullProcess.waitFor()

        return Imgcodecs.imread(temp.absolutePath)
    }

    suspend fun tap(x: Double, y: Double, delay: Int = 0): Boolean {
        val tapProcess = ProcessBuilder("adb", "shell", "input", "tap", x.toString(), y.toString())
            .start()
        tapProcess.waitFor() == 0
        delay(delay.milliseconds)
        return tapProcess.exitValue() == 0
    }
    suspend fun tap(point: Point?,      delay: Int = Config.delay): Boolean = point?.let { tap(it.x, it.y, delay) }                == true
    suspend fun tap(image: Mat?,        delay: Int = Config.delay): Boolean = image?.let { findImage(it)?.let { tap(it, delay) } } == true
    suspend fun tap(file: File?,        delay: Int = Config.delay): Boolean = file?.takeIf { it.exists() }?.let { tap(Imgcodecs.imread(it.absolutePath), delay) } == true
    suspend fun tap(filePath: String?,  delay: Int = Config.delay): Boolean = filePath?.let { tap(File("img/$it.png"), delay) }    == true
    suspend fun tapText(text: String,   delay: Int = Config.delay): Boolean = tap(findText(text), delay) == true

}

fun Process.outputAsList(): MutableList<String> {
    val reader = BufferedReader(InputStreamReader(this.inputStream))
    val outputLines = mutableListOf<String>()
    reader.forEachLine { if(it != "") outputLines.add(it) }
    this.waitFor()
    return outputLines
}
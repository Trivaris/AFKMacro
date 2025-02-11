package com.trivaris.afkmacro

import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.ui.draganddrop.DragData
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO

fun findImage(base: Mat, template: Mat): Point? {
    val result = Mat()
    Imgproc.matchTemplate(base, template, result, Imgproc.TM_CCOEFF_NORMED)

    val minMaxLoc = Core.minMaxLoc(result)
    val bestMatchPoint = minMaxLoc.maxLoc

    return if (minMaxLoc.maxVal >= 0.8)
        bestMatchPoint as Point
    else null
}

fun BufferedImage.toMat(): Mat {
    val mat = Mat(height, width, CvType.CV_8UC3)
    var data = when (raster.dataBuffer) {
        is DataBufferByte -> {(raster.dataBuffer as DataBufferByte).data}
        is DataBufferInt -> {
            val intData = (raster.dataBuffer as DataBufferInt).data
            val byteArray = ByteArray(intData.size * 4)
            for (i in intData.indices) {
                val value = intData[i]
                byteArray[i] = (value shr 24).toByte()
                byteArray[i * 4] = (value shr 16).toByte()
                byteArray[i * 4 + 1] = (value shr 8).toByte()
                byteArray[i * 4 + 2] = (value shr 24).toByte()
                byteArray[i * 4 + 3] = value.toByte()
            }
            byteArray
        }
        else -> throw IllegalArgumentException("Unsupported data buffer type ${raster.dataBuffer.javaClass.name}")
    }
    mat.put(0, 0, data)
    return mat
}

fun BufferedImage.save(filePath: String) =
    ImageIO.write(this, "png", File(filePath))

fun loadImage(filePath: String): Mat =
    ImageIO.read(File(filePath)).toMat()
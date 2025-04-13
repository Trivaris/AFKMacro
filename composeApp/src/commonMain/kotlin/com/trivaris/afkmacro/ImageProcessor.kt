package com.trivaris.afkmacro

import com.trivaris.afkmacro.scrcpy.Emulator
import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel
import net.sourceforge.tess4j.ITesseract
import net.sourceforge.tess4j.Tesseract
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Rect
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import java.awt.Rectangle
import java.io.File
import java.util.Date
import javax.imageio.ImageIO

fun findAllImage(
    template: Mat,
    base: Mat = Emulator.screenshot(),
    region: Rect? = null
): MutableList<Point> {
    val results = mutableListOf<Point>()
    val maskColor = Scalar(0.0, 0.0, 0.0)
    while (true) {
        val point = findImage(template, base, region)
        if (point == null) break
        results.add(point)
        val roi = Rect(point, template.size())
        Imgproc.rectangle(base, roi.tl(), roi.br(), maskColor, -1)
    }
    return results
}
fun findAllImage(file: File,       base: Mat = Emulator.screenshot(), region: Rect? = null): MutableList<Point> = findAllImage(Imgcodecs.imread(file.absolutePath), base, region)
fun findAllImage(filePath: String, base: Mat = Emulator.screenshot(), region: Rect? = null): MutableList<Point> = findAllImage(File("img/$filePath.png"), base, region)

fun findImage(
    template: Mat,
    base: Mat = Emulator.screenshot(),
    region: Rect? = null
): Point? {
    val result = Mat()
    Imgproc.matchTemplate(base, template, result, Imgproc.TM_CCOEFF_NORMED)

    val minMaxLoc = Core.minMaxLoc(result)
    val bestMatchPoint = minMaxLoc.maxLoc

    val loc = if (minMaxLoc.maxVal >= 0.8)
       bestMatchPoint as Point
    else null

    return if (region == null)
        loc
    else if (loc == null)
        null
    else if (loc.inside(region)) loc
    else null

}
fun findImage(file: File,       base: Mat = Emulator.screenshot(), region: Rect? = null): Point? = findImage(Imgcodecs.imread(file.absolutePath), base, region)
fun findImage(filePath: String, base: Mat = Emulator.screenshot(), region: Rect? = null): Point? = findImage(File("img/$filePath.png"), base, region)

fun findText(text: String,
             base: Mat = Emulator.screenshot(),
             region: Rect? = null
): Point? {
    val gray = Mat()
    Imgproc.cvtColor(base, gray, Imgproc.COLOR_RGB2GRAY)

    val binary = Mat()
    Imgproc.threshold(gray, binary, 0.0, 255.0, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

    val tempFile = File("temp/${Date().time}-temp.png")
    Imgcodecs.imwrite(tempFile.absolutePath, binary)

    val tesseract: ITesseract  = Tesseract()
    tesseract.setDatapath(File("tesseract").absolutePath)
    tesseract.setLanguage("eng")

    val foundWords = tesseract.getWords(ImageIO.read(tempFile), TessPageIteratorLevel.RIL_WORD )

    repeat(Config.maxTries) {
        val currentWord = foundWords.firstOrNull { it.text == text }
        currentWord?.let {
            return it.boundingBox.midPoint()
        }
    }

    return null
}

fun resizeIfPossible(image: Mat, newWidth: Int = 1080, newHeight: Int = 1920): Mat? {
    if (newWidth == image.width() && newHeight == image.height())
        return image

    if (image.height() == 0 || image.width() == 0|| newWidth == 0 || newHeight == 0)
        return  null

    val currentAspectRatio = image.width() / image.height()
    val targetAspectRatio = newWidth / newHeight

    if (targetAspectRatio == currentAspectRatio) {
        val resized = Mat()
        Imgproc.resize(image, resized, Size(newWidth.toDouble(), newHeight.toDouble()))

        if (resized == Mat())
            return null
        return resized
    }

    return null
}

fun Rectangle.midPoint(): Point =
    Point(x + width / 2.0, y + height / 2.0)

fun Rect.midPoint(): Point =
    Point(x + width / 2.0, y + height / 2.0)

fun Mat.midPoint(): Point? {
    val point = findImage(this)
    if (point != null)
        return Rect(point, point + Point(width().toDouble(), height().toDouble())).midPoint()
    return null
}

operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
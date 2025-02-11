package com.trivaris.afkmacro.sender

import com.trivaris.afkmacro.findImage
import com.trivaris.afkmacro.loadImage
import com.trivaris.afkmacro.toMat
import java.awt.Rectangle
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.InputEvent
import java.awt.image.BufferedImage
import java.io.File
import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.win32.StdCallLibrary

const val SRCCOPY = 0x00CC0020

interface User32 : StdCallLibrary {
    fun GetDesktopWindow(): HWND
    fun GetWindowDC(hWnd: HWND): HDC
    fun GetClientRect(hWnd: HWND, lpRect: RECT): Boolean
    fun FindWindow(lpClassName: String?, lpWindowName: String?): HWND
}

interface Gdi32 : StdCallLibrary {
    fun CreateCompatibleDC(hdc: HDC): HDC
    fun CreateCompatibleBitmap(hdc: HDC, nWidth: Int, nHeight: Int): HBITMAP
    fun SelectObject(hdc: HDC, hObject: HGDIOBJ): HGDIOBJ
    fun BitBlt(hdcDest: HDC, xDest: Int, yDest: Int, nWidth: Int, nHeight: Int, hdcSource: HDC, xSrc: Int, ySrc: Int, dwRop: Int): Boolean
    fun DeleteDC(hdc: HDC): Boolean
}

interface Kernel32 : StdCallLibrary {
    fun GetModuleHandle(lpModuleName: String?): HMODULE
}

interface User32Extended : User32 {
    fun GetWindowText(hWnd: HWND, lpString: CharArray, nMaxCount: Int): Int
    fun EnumWindows(lpEnumFunc: WinUser.EnumWindowsProc, lParam: Pointer): Boolean
}


object Robot: Robot() {

    init {
        val cvPath = File(".\\opencv\\opencv_java4100.dll").absolutePath
        System.load(cvPath)
        nu.pattern.OpenCV.loadLocally()
    }

    fun screenshot(): BufferedImage {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val captureRect = Rectangle(screenSize)

        return createScreenCapture(captureRect)
    }

    fun getProcessByName(name: String): Process? {
        val user32 = Native.load("user")
    }

    fun clickFoundImage(imagePath: String): Boolean {
        val toFind = loadImage(imagePath)
        val pos = findImage(screenshot().toMat(), screenshot().toMat())

        if (pos == null) {
            println("Failed to find Mask")
            return false
        }

        mouseMove(pos.x.toInt(), pos.y.toInt())
        mousePress(InputEvent.BUTTON1_DOWN_MASK)

        println("[OUT] Result: $")

        return true
    }
}
package com.vsloong.toolman.core.image


import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import javax.imageio.ImageIO


class QRCodeUseCase {

    fun createQRCodeImage(
        content: String,
        imagePath: Path,
        width: Int = 400,
        height: Int = 400
    ) {
        if (!Files.exists(imagePath)) {
            Files.createFile(imagePath)
        }

        ImageIO.write(
            createBufferedImage(content, width, height),
            "png",
            imagePath.toFile()
        )
    }

    private fun createBufferedImage(
        content: String,
        width: Int,
        height: Int
    ): BufferedImage {
        val map: MutableMap<EncodeHintType, Any> = EnumMap(com.google.zxing.EncodeHintType::class.java)
        map[EncodeHintType.CHARACTER_SET] = "UTF-8"
        map[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.M
        map[EncodeHintType.MARGIN] = 1
        val writer = MultiFormatWriter()

        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_BGR)
        val matrix: BitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, map)

        for (i in 0 until width) {
            for (j in 0 until height) {
                val color: Int = if (matrix.get(i, j)) {
                    0x000000
                } else {
                    0xFFFFFF
                }
                bufferedImage.setRGB(i, j, color)
            }
        }
        return bufferedImage
    }
}
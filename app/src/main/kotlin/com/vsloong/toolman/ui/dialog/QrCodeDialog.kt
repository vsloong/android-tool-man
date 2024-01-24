package com.vsloong.toolman.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppDialog
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.inputStream


@Composable
fun QrCodeDialog(
    qrCodePath: Path,
    visible: Boolean,
    onDismissClick: () -> Unit,
) {

    AppDialog(
        visible = visible,
        size = DpSize(width = 300.dp, height = 400.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(12.dp),
        ) {
            if (qrCodePath.toString().isNotBlank() && Files.exists(qrCodePath)) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = null,
                    bitmap = loadImageBitmap(qrCodePath.inputStream())
                )
            }

            AppButton(
                modifier = Modifier.fillMaxWidth(),
                text = "关闭",
                onClick = {
                    onDismissClick.invoke()
                }
            )
        }
    }
}
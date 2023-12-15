package com.vsloong.toolman

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import com.vsloong.toolman.ui.widget.DragAndDropBox
import com.vsloong.toolman.usecase.AssetsUseCase
import com.vsloong.toolman.usecase.BundleUseCase
import com.vsloong.toolman.utils.logger
import kotlin.io.path.Path


val useCase = AssetsUseCase()
val bundleUseCase = BundleUseCase(useCase)


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = appWindowState(),
    ) {
        App()
    }
}

@Composable
fun appWindowState(
    placement: WindowPlacement = WindowPlacement.Floating,
    isMinimized: Boolean = false,
    position: WindowPosition = WindowPosition.Aligned(Alignment.Center),
    size: DpSize = DpSize(800.dp, 500.dp),
): WindowState = rememberWindowState(
    placement = placement,
    isMinimized = isMinimized,
    position = position,
    size = size
)


@Composable
@Preview
private fun App() {

    MaterialTheme {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(text = "系统文件路径=${useCase.getAssetsDir()}", color = Color.Blue, fontSize = 16.sp)

            DragAndDropBox(
                modifier = Modifier.width(400.dp).height(200.dp),
                dashedBorderColor = Color.Blue,
                onDrop = {
                    logger("文件：$it")

                    if (it.size == 1) {
                        val file = it.first().toFile()

                        if (file.extension == "aab") {
                            bundleUseCase.buildApks(file.toPath())
                        }
                    }
                }) {
            }

            Button(onClick = {
                bundleUseCase.logVersion()
            }) {
                Text(text = "buildApks")
            }

            Button(onClick = {
                bundleUseCase.installApk(apksPath = Path("/Users/dragon/Temp/HC/output.apks"))
            }) {
                Text(text = "installApks")
            }
        }
    }
}

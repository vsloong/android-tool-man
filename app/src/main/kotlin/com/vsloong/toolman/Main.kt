package com.vsloong.toolman

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.*
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.vsloong.toolman.core.common.usecase.AdbUseCase
import com.vsloong.toolman.core.common.usecase.BundleUseCase
import com.vsloong.toolman.manager.AssetsManager
import com.vsloong.toolman.ui.widget.DragAndDropBox
import kotlin.io.path.Path


val bundleUseCase = BundleUseCase(AssetsManager)
val adbUseCase = AdbUseCase(AssetsManager)


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

            Text(text = "系统文件路径=${AssetsManager.getAssetsPath()}", color = Color.Blue, fontSize = 16.sp)

            DragAndDropBox(
                modifier = Modifier.width(400.dp).height(200.dp),
                dashedBorderColor = Color.Blue,
                onDrop = {
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

            Button(onClick = {
                adbUseCase.help()
            }) {
                Text(text = "adb help")
            }

            Button(onClick = {
                adbUseCase.devices()
            }) {
                Text(text = "adb devices")
            }

            Button(onClick = {
                adbUseCase.killServer()
            }) {
                Text(text = "adb kill-server")
            }

        }
    }
}

package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.DragData
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.onExternalDrag
import com.vsloong.toolman.ui.widget.ext.dashBorder
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DragAndDropBox(
    modifier: Modifier,
    dashedBorderColor: Color = Color.Unspecified,
    onDrop: (List<Path>) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .dashBorder(color = dashedBorderColor)
            .clickable {
                showFileChooser {
                    onDrop.invoke(it)
                }
            }
            .onExternalDrag(
                enabled = true,
                onDrop = {
                    val dragData = it.dragData
                    if (dragData is DragData.FilesList) {
                        val pathList = dragData.readFiles()
                            .map { fileUri ->
                                URI(fileUri).toPath()
                            }
                        onDrop.invoke(pathList)
                    }
                }
            ),
        content = content
    )
}
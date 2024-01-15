package com.vsloong.toolman.ui.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.vsloong.toolman.ui.widget.ext.dashBorder
import java.net.URI
import java.nio.file.Path
import kotlin.io.path.toPath


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DragAndDropBox(
    modifier: Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    onDrop: (List<Path>) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {

    val isDragging = remember { mutableStateOf(false) }

    Box(
        modifier = modifier
//            .clickable {
//                showFileChooser {
//                    onDrop.invoke(it)
//                }
//            }
            .drawBehind {
                if (isDragging.value) {
                    drawRoundRect(
                        size = Size(drawContext.size.width, drawContext.size.height),
                        color = Color.Black.copy(alpha = 0.25f),
                    )
                }
            }
            .onExternalDrag(
                enabled = true,
                onDragStart = {
                    isDragging.value = true
                },
                onDragExit = {
                    isDragging.value = false
                },
                onDrop = {
                    isDragging.value = false

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
        contentAlignment = contentAlignment,
        content = content
    )
}
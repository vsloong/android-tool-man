package com.vsloong.toolman.ui.device

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.core.common.model.AdbDeviceInfo
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.DragAndDropBox
import java.nio.file.Path


@Composable
fun DeviceContent(
    devices: List<AdbDeviceInfo>,
    onFileDrop: (List<Path>, AdbDeviceInfo) -> Unit,
    onRefreshClick: () -> Unit,
) {

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(text = "设备", fontSize = 24.sp, modifier = Modifier.wrapContentSize()
            .clickable {
                onRefreshClick.invoke()
            })

        LazyVerticalGrid(
            columns = GridCells.Fixed(count = 2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices) {
                DeviceItem(
                    device = it,
                    onFileDrop = onFileDrop
                )
            }
        }
    }
}

@Composable
private fun DeviceItem(
    device: AdbDeviceInfo,
    onFileDrop: (List<Path>, AdbDeviceInfo) -> Unit
) {
    DragAndDropBox(
        modifier = Modifier.fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = Color(0xFFC498FF)),
        onDrop = { paths ->
            onFileDrop.invoke(paths, device)
        },
        contentAlignment = Alignment.BottomCenter
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
                .height(32.dp)
                .padding(4.dp)
                .clip(RoundedCornerShape(50))
                .background(color = Color.White)
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = device.model,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
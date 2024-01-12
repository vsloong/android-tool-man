package com.vsloong.toolman.ui.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.ui.themes.R


@Composable
fun LeftTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "自定义"
        )

    }
}

@Composable
private fun TabItem(
    resourcePath: String,
    tabName: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(resourcePath),
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Text(text = tabName, fontSize = 20.sp, color = R.colors.text_primary)

    }
}
package com.vsloong.toolman.ui.tab

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.ui.themes.R


@Composable
fun LeftTab(
    currentTab: TabType,
    leftTabEvent: LeftTabEvent
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "ATM",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )

        Text(text = "your android tool man")

        Spacer(modifier = Modifier.size(36.dp))

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Message Feed",
            isSelected = currentTab == TabType.Feed,
            onClick = leftTabEvent.onHomeClick
        )

        TabItem(
            resourcePath = "tab_left_custom.svg",
            tabName = "Feature",
            isSelected = currentTab == TabType.Feature,
            onClick = leftTabEvent.onFeatureClick
        )

    }
}

@Composable
private fun TabItem(
    resourcePath: String,
    tabName: String,
    isSelected: Boolean = false,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (isSelected) {
                    Color.Black
                } else {
                    Color.Transparent
                }
            )
            .clickable {
                onClick.invoke()
            }
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(resourcePath),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(
                color = if (isSelected) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        )

        Text(
            text = tabName,
            fontSize = 16.sp,
            color = if (isSelected) {
                Color.White
            } else {
                Color.Black
            }
        )

    }
}
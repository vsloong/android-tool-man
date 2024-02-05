package com.vsloong.toolman.ui.home

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.ui.screen.channel.ChannelTabScreen
import com.vsloong.toolman.ui.screen.device.DeviceTabScreen
import com.vsloong.toolman.ui.screen.sign.SignTabScreen

@Composable
fun HomeScreen(onExit: () -> Unit) {

    TabNavigator(DeviceTabScreen) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(color = Color.White),
        ) {

            Column(
                modifier = Modifier.fillMaxHeight()
                    .width(160.dp)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(text = "ATM", fontSize = 40.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                HomeTabItemWrapper(tabScreen = DeviceTabScreen)
                HomeTabItemWrapper(tabScreen = ChannelTabScreen)
                HomeTabItemWrapper(tabScreen = SignTabScreen)

                Spacer(modifier = Modifier.fillMaxSize().weight(1f))

                HomeTabIcon(
                    resourcePath = "home_tab_setting.svg",
                    onClick = {

                    }
                )

                HomeTabIcon(
                    resourcePath = "home_tab_exit.svg",
                    onClick = {
                        onExit.invoke()
                    }
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun HomeTabItemWrapper(tabScreen: BaseTabScreen) {
    val tabNavigator = LocalTabNavigator.current

    HomeTabItem(
        title = tabScreen.tabOptions.title,
        icon = tabScreen.tabOptions.icon,
        selected = tabNavigator.current == tabScreen,
        onClick = {
            tabNavigator.current = tabScreen
        }
    )

}

@Composable
private fun HomeTabIcon(
    resourcePath: String,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(resourcePath = resourcePath),
        contentDescription = null,
        modifier = Modifier.size(32.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick.invoke() }
    )
}

@Composable
private fun HomeTabItem(
    title: String,
    icon: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = if (selected) {
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
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(
                color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        )

        Text(
            text = title,
            fontSize = 16.sp,
            color = if (selected) {
                Color.White
            } else {
                Color.Black
            }
        )

    }
}
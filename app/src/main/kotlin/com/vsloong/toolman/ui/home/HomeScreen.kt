package com.vsloong.toolman.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.themes.R
import com.vsloong.toolman.ui.widget.DragAndDropBox

class HomeScreen : BaseScreen {

    @Composable
    override fun Content() {
        val homeViewModel = rememberViewModel { HomeViewModel() }

        val homeEvent = homeViewModel.homeEvent

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DragAndDropBox(
                modifier = Modifier.width(400.dp)
                    .height(200.dp),
                onDrop = {
                    homeViewModel.install(it)
                },
                dashedBorderColor = R.colors.primary
            ) {

            }

            homeViewModel.devices.forEach {
                Text(
                    text = it.model,
                    fontSize = 16.sp,
                    color = R.colors.text_primary,
                    modifier = Modifier.clickable {
                        homeEvent.onDeviceSelect.invoke(it)
                    }
                )
            }


        }
    }
}
package com.vsloong.toolman.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.widget.AppButton


class FeatureScreen : BaseScreen {

    @Composable
    override fun Content() {

        val featureViewModel = rememberViewModel { FeatureViewModel() }

        Column {

            AppButton(
                modifier = Modifier,
                text = "截图",
                onClick = {
                    featureViewModel.featureEvent.onFeatureClick.invoke(ToolManFeature.ScreenCap)
                })
        }
    }
}
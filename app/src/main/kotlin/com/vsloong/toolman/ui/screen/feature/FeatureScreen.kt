package com.vsloong.toolman.ui.screen.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.BaseTabOptions
import com.vsloong.toolman.base.BaseTabScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.widget.AppButton
import java.io.File


object FeatureScreen : BaseTabScreen() {

    override val tabOptions: BaseTabOptions
        get() = BaseTabOptions(
            title = "功能",
            icon = "tab_left_custom.svg"
        )

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

            AppButton(
                text = "顶层Activity",
                onClick = {
                    featureViewModel.currentFocusActivity()
                }
            )

            if (featureViewModel.screenCap.value.isNotBlank()) {
                Image(
                    bitmap = loadImageBitmap(File(featureViewModel.screenCap.value).inputStream()),
                    contentDescription = null,
                    modifier = Modifier.width(270.dp)
                        .height(540.dp)
                )
            }
        }
    }
}
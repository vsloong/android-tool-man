package com.vsloong.toolman.ui.screen.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.base.BaseScreen
import com.vsloong.toolman.base.rememberViewModel
import com.vsloong.toolman.ui.widget.AppButton
import java.io.File


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

            if (featureViewModel.screenCap.value.isNotBlank()) {
                Image(
                    bitmap = loadImageBitmap(File(featureViewModel.screenCap.value).inputStream()),
                    contentDescription = null,
                    modifier = Modifier.width(90.dp)
                        .height(180.dp)
                )
            }
        }
    }
}
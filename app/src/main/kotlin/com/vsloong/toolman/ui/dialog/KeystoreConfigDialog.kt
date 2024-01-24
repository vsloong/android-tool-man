package com.vsloong.toolman.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.vsloong.toolman.ui.widget.AppButton
import com.vsloong.toolman.ui.widget.AppDialog
import com.vsloong.toolman.ui.widget.AppTextFiled
import com.vsloong.toolman.ui.widget.DragAndDropBox
import com.vsloong.toolman.ui.widget.ext.dashBorder
import java.nio.file.Path
import kotlin.io.path.Path


@Composable
fun KeystoreConfigDialog(
    visible: Boolean,
    onSaveClick: (Path, String, String, String) -> Unit,
    onCancelClick: () -> Unit,
) {

    val keystoreFile = remember {
        mutableStateOf(Path(""))
    }

    val keystorePass = remember {
        mutableStateOf("")
    }

    val keyAlias = remember {
        mutableStateOf("")
    }

    val keyPass = remember {
        mutableStateOf("")
    }

    AppDialog(
        visible = visible,
        size = DpSize(width = 600.dp, height = 500.dp),
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            DragAndDropBox(
                modifier = Modifier.fillMaxWidth()
                    .height(100.dp)
                    .dashBorder(color = Color.Black),
                onDrop = {
                    keystoreFile.value = it.first()
                },
                contentAlignment = Alignment.Center
            ) {
                Column {
                    Text(text = "拖拽签名文件到此")
                    Text(text = keystoreFile.value.toString())
                }
            }

            AppTextFiled(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                text = keystorePass.value,
                hint = "请输入keystorePass",
                onValueChange = {
                    keystorePass.value = it
                }
            )

            AppTextFiled(
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp),
                text = keyAlias.value,
                hint = "请输入keyAlias",
                onValueChange = {
                    keyAlias.value = it
                }
            )

            AppTextFiled(
                modifier = Modifier.fillMaxWidth()
                    .height(46.dp),
                text = keyPass.value,
                hint = "请输入keyPass",
                onValueChange = {
                    keyPass.value = it
                }
            )

            Spacer(modifier = Modifier.size(2.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppButton(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                        .height(46.dp),
                    text = "取消",
                    onClick = {
                        onCancelClick.invoke()
                    }
                )

                AppButton(
                    modifier = Modifier.fillMaxWidth()
                        .weight(1f)
                        .height(46.dp),
                    text = "保存",
                    onClick = {
                        onSaveClick.invoke(keystoreFile.value, keystorePass.value, keyAlias.value, keyPass.value)
                    }
                )
            }
        }
    }

}
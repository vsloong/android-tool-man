package com.vsloong.toolman.ui.widget

import java.nio.file.Path
import javax.swing.JFileChooser
import javax.swing.SwingUtilities
import javax.swing.UIManager

fun showFileChooser(
    onFileSelected: (List<Path>) -> Unit,
) {
    JFileChooser().apply {
        //设置页面风格
        try {
            val lookAndFeel = UIManager.getSystemLookAndFeelClassName()
            UIManager.setLookAndFeel(lookAndFeel)
            SwingUtilities.updateComponentTreeUI(this)
        } catch (e: Throwable) {
            e.printStackTrace()
        }

        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        isMultiSelectionEnabled = false

        val result = showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            val file = this.selectedFile
            onFileSelected(listOf(file.toPath()))
        }
    }
}
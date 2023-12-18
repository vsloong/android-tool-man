package com.vsloong.toolman.test

import com.vsloong.toolman.manager.IAssetsPath
import java.nio.file.Path
import kotlin.io.path.toPath


/**
 * 资源（资产）相关用例
 */
object TestAssetsManager : IAssetsPath {
    override
    fun getAssetsPath(): Path {
        val contextClassLoader = Thread.currentThread().contextClassLoader!!
        val uri = contextClassLoader.getResource("")!!

        val path = uri.toURI().toPath()

        return path.parent.parent.parent
            .resolve("compose")
            .resolve("tmp")
            .resolve("prepareAppResources")
    }
}
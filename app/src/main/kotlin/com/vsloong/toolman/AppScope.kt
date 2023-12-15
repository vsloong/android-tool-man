package com.vsloong.toolman

import com.vsloong.toolman.utils.logger
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


/**
 * 全局的协程作用域
 */
object AppScope {

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        logger("协程执行出错：${coroutineContext.toString()} -> ${throwable.message}")
    }

    private val appScope = CoroutineScope(Dispatchers.IO + errorHandler)

    fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ) {
        appScope.launch(context, start, block)
    }
}
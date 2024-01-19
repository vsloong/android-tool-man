package com.vsloong.toolman.base

import cafe.adriel.voyager.core.model.ScreenModelStore
import kotlinx.coroutines.*

public val BaseViewModel.viewModelIoScope: CoroutineScope
    get() = ScreenModelStore.getOrPutDependency(
        screenModel = this,
        name = "ScreenModelCoroutineScope",
        factory = { key -> CoroutineScope(SupervisorJob() + Dispatchers.IO + CoroutineName(key)) },
        onDispose = { scope -> scope.cancel() }
    )



package com.vsloong.toolman.ui.screen.appdetails

sealed interface ApkDetailsState {
    data object Idle : ApkDetailsState
    data object Pulling : ApkDetailsState
    data object PullDone : ApkDetailsState
    data object CheckSign : ApkDetailsState
    data object CheckSignDown : ApkDetailsState
}
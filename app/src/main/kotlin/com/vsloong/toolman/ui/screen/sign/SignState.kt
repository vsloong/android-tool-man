package com.vsloong.toolman.ui.screen.sign

interface SignState {
    object Idle : SignState
    object Checking : SignState
    object Signed : SignState
    object UnSign : SignState
}
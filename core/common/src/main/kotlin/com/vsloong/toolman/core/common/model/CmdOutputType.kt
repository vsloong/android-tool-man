package com.vsloong.toolman.core.common.model

sealed interface CmdOutputType {
    data object Text : CmdOutputType
    data object Image : CmdOutputType
}
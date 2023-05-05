package com.dowplay.dowplay

class DownloadManagerSTATUS{
    //println("${STATUS.ordinal} = ${STATUS.name}")
    companion object {
        const val STATUS_RUNNING = 0
        const val STATUS_FAILED = 2
        const val STATUS_SUCCESSFUL = 3
        const val STATUS_PAUSED = 1
        const val STATUS_UNKNOWN = 2
    }
}
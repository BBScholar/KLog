package io.bbscholar.klog

abstract class Loggable(val defaultTag: String = kDefaultLoggerTag) {

    init {
        Logger.register(this)
    }

}
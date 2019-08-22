package io.bbscholar.klog

import io.bbscholar.klog.annotations.Log
import java.util.concurrent.LinkedBlockingDeque
import kotlin.reflect.full.memberProperties


// map  <tag, () -> Any>
// Logger.tag(tag).trace(value())

private typealias Value = () -> Any?

const val kDefaultLoggerTag = "default"

object Logger {

    private val mConfigurations = mutableListOf<LoggerConfig>()
    private val mFields = mutableListOf<Triple<String, String, Value>>() // tag, name, value getter

    private val mLogQueue = LinkedBlockingDeque<Pair<String, Any?>>(2000)
    private val mLogThread: Thread

    init {
        mLogThread = Thread(LoggingThread())
        mLogThread.start()
    }

    fun addConfiguration(config: LoggerConfig) {
        mConfigurations.add(config)
    }

    fun register(loggable: Loggable) {
        for(field in loggable::class.memberProperties) {
            for (annotation in field.annotations) {
                when(annotation) {
                    is Log -> {
                        mFields.add(Triple(annotation.tag, annotation.name, { field.getter.call() } ))
                    }
                }
            }
        }
    }

    fun update() {
        for ( (tag, name, value) in mFields) {
            mLogQueue.offer(Pair(tag, "$name: ${value()}"))
        }
    }

    data class LoggerConfig(val placeholder: String)

    enum class LogLevel {
        Trace, Debug, Warning, Error, Fatal
    }

    private class LoggingThread : Runnable {
        override fun run() {
            Thread.currentThread().priority = 1
            while(true) {
                try {
                    val log = mLogQueue.take()
                    Logger.tag(log.first).trace(log.second)
                } catch (e: InterruptedException) {
                    println("[ERROR] Logging thread stopped!")
                    Thread.currentThread().interrupt()
                    return
                }
            }
        }
    }

}
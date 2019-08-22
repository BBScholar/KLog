package io.bbscholar.klog.annotations

import io.bbscholar.klog.kDefaultLoggerTag

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class Log(val name: String, val tag: String = kDefaultLoggerTag)
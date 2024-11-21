package com.fadlurahmanfdev.remote_logging_v2.logger

import android.util.Log
import java.util.logging.Level

object RemoteLogger {

    private var listener: Listener? = null

    fun addListener(listener: Listener) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    fun log(level: Level, message: String, labels: Map<String, String>?) {
        when (level) {
            Level.SEVERE -> {
                Log.e(this::class.java.simpleName, message)
            }

            Level.WARNING -> {
                Log.w(this::class.java.simpleName, message)
            }

            else -> {
                Log.i(this::class.java.simpleName, message)
            }
        }
        listener?.log(level, message, labels)
    }

    interface Listener {
        fun log(level: Level, message: String, labels: Map<String, String>?)
    }
}
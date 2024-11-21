package com.fadlurahmanfdev.remote_logging_v2.service

import org.json.JSONObject
import java.util.logging.Level

abstract class RemoteLoggingService {
    private lateinit var sourceToken: String

    abstract fun init()

    val defaultLabels = JSONObject()

    fun addDefaultLabel(labels: Map<String, String>) {
        labels.keys.forEach { key ->
            defaultLabels.put(key, labels[key])
        }
    }

    fun removeDefaultLabel(labels: List<String>) {
        labels.forEach { label ->
            defaultLabels.remove(label)
        }
    }

    abstract fun log(level: Level, message: String, labels: Map<String, String>? = null)
}
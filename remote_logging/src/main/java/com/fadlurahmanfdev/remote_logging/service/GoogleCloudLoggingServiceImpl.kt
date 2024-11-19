package com.fadlurahmanfdev.remote_logging.service

import android.util.Log
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.MonitoredResource
import com.google.cloud.logging.LogEntry
import com.google.cloud.logging.Logging
import com.google.cloud.logging.LoggingOptions
import com.google.cloud.logging.Payload.JsonPayload
import com.google.cloud.logging.Severity
import java.io.InputStream
import java.util.Collections
import java.util.logging.Level


class GoogleCloudLoggingServiceImpl(
    private val serviceAccount: InputStream,
    val projectId: String,
    private val env: String
) :
    RemoteLoggingService() {
    private lateinit var logging: Logging
    private var isInit = false
    override fun init() {
        try {
            logging = LoggingOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(ServiceAccountCredentials.fromStream(serviceAccount))
                .build()
                .service

            addDefaultLabel(
                hashMapOf(
                    "project_id" to projectId,
                    "environment" to env
                )
            )

            isInit = true
        } catch (e: Throwable) {
            isInit = false
            Log.e(this::class.java.simpleName, "failed to init logging: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun getSeverityFromLevel(level: Level): Severity {
        return when (level) {
            Level.SEVERE -> {
                Severity.CRITICAL
            }

            Level.WARNING -> {
                Severity.WARNING
            }

            Level.INFO -> {
                Severity.INFO
            }

            else -> {
                Severity.NOTICE
            }
        }
    }


    override fun log(level: Level, message: String, labels: Map<String, String>?) {
        if (!isInit) {
            Log.e(this::class.java.simpleName, "logging must be init first")
            return
        }

        val severityLevel = getSeverityFromLevel(level)

        val currentLabels = HashMap<String, String>()
        currentLabels["level"] = level.name.toString()

        defaultLabels.keys().forEach { key ->
            currentLabels[key] = defaultLabels.getString(key)
        }

        labels?.keys?.forEach { key ->
            currentLabels[key] = "${labels[key]}"
        }

        val logEntry = LogEntry.newBuilder(JsonPayload.of(hashMapOf("message" to message)))
            .setLogName(env)
            .setResource(MonitoredResource.newBuilder("global").build())
            .setSeverity(severityLevel)
            .setLabels(currentLabels)
            .build()

        try {
            logging.write(Collections.singleton(logEntry))

            logging.flush()

            Log.d(this::class.java.simpleName, "success send remote log")
        } catch (e: Throwable) {
            Log.e(this::class.java.simpleName, "failed send remote log: ${e.message}")
            e.printStackTrace()
        }
    }
}
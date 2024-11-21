package com.fadlurahmanfdev.remote_logging_v2.service

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.logging.Level


class BetterstackLoggingServiceImpl(
    private val sourceToken: String
): RemoteLoggingService() {
    private lateinit var client: OkHttpClient

    override fun init() {
        client = OkHttpClient()
    }

    private fun getSeverityFromLevel(level: Level): String {
        return when(level){
            Level.SEVERE -> {
                "CRITICAL"
            }

            Level.WARNING -> {
                "WARNING"
            }

            else -> {
                "INFO"
            }
        }
    }


    override fun log(level: Level, message: String, labels: Map<String, String>?) {
        val severityLevel = getSeverityFromLevel(level)

        val body = JSONObject()
        body.put("message", "[$severityLevel] - $message")

        val currentLabels = JSONObject()
        currentLabels.put("level", level.name.toString())

        defaultLabels.keys().forEach { key ->
            currentLabels.put(key, defaultLabels[key])
        }

        labels?.keys?.forEach { key ->
            currentLabels.put(key, labels[key])
        }

        body.put("label", currentLabels)

        val request = Request.Builder()
            .addHeader("Authorization", "Bearer $sourceToken")
            .addHeader("Content-Type", "application/json")
            .url("https://in.logs.betterstack.com")
            .post(body.toString().toRequestBody("application/json; charset=utf-8".toMediaType()))
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(
                    this@BetterstackLoggingServiceImpl::class.java.simpleName,
                    "failed to send remote log: ${e.message}"
                )
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(this@BetterstackLoggingServiceImpl::class.java.simpleName, "betterstack success send remote log")
            }

        })
    }
}
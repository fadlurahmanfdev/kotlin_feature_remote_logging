package com.fadlurahmanfdev.example

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.fadlurahmanfdev.remote_logging.service.BetterstackLoggingServiceImpl
import com.fadlurahmanfdev.remote_logging.service.GoogleCloudLoggingServiceImpl
import java.util.logging.Level

class MainActivity : AppCompatActivity() {
    lateinit var betterstackImpl: BetterstackLoggingServiceImpl
    lateinit var googleCloudLoggingServiceImpl: GoogleCloudLoggingServiceImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        betterstackImpl = BetterstackLoggingServiceImpl(sourceToken = BuildConfig.BETTERSTACK_SOURCE_TOKEN)

        googleCloudLoggingServiceImpl = GoogleCloudLoggingServiceImpl(
            serviceAccount = resources.openRawResource(R.raw.example_learn_purpose_4dd93f634791),
            projectId = "example-learn-purpose",
            env = "dev"
        )

        googleCloudLoggingServiceImpl.init()
        betterstackImpl.init()

        googleCloudLoggingServiceImpl.log(Level.INFO, message = "fadlurahmanfdev start app kotlin")
//        betterstackImpl.log(Level.INFO, message = "fadlurahmanfdev start app kotlin")
    }
}
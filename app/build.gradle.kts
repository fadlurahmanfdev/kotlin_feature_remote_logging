import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()

localProperties.apply {
    load(FileInputStream(File(rootProject.rootDir, "local.properties")))
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.fadlurahmanfdev.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fadlurahmanfdev.example"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val betterStackSourceToken = localProperties.getProperty("BETTERSTACK_SOURCE_TOKEN")
        buildConfigField("String", "BETTERSTACK_SOURCE_TOKEN", betterStackSourceToken)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }

    // Important for google cloud logging
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES"
            )
        )
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(project(":remote_logging"))
}
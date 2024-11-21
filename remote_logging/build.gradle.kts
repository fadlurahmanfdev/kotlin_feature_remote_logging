import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("maven-publish")

    id("com.vanniktech.maven.publish") version "0.29.0"
}

android {
    namespace = "com.fadlurahmanfdev.remote_logging"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(platform("com.google.cloud:libraries-bom:26.50.0"))
    implementation("com.google.cloud:google-cloud-logging")
    implementation("io.grpc:grpc-okhttp:1.44.1")
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("com.fadlurahmanfdev", "kotlin_feature_remote_logging", "0.0.2-beta")

    pom {
        name.set("Kotlin Library Feature Remote Logging")
        description.set("A library to simplified remote logging")
        inceptionYear.set("2024")
        url.set("https://github.com/fadlurahmanfdev/kotlin_feature_remote_logging/")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }
        developers {
            developer {
                id.set("fadlurahmanfdev")
                name.set("Taufik Fadlurahman Fajari")
                url.set("https://github.com/fadlurahmanfdev/")
            }
        }
        scm {
            url.set("https://github.com/fadlurahmanfdev/kotlin_feature_remote_logging/")
            connection.set("scm:git:git://github.com/fadlurahmanfdev/kotlin_feature_remote_logging.git")
            developerConnection.set("scm:git:ssh://git@github.com/fadlurahmanfdev/kotlin_feature_remote_logging.git")
        }
    }
}
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.ainsln.core.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

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
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(projects.core.common)

    implementation (libs.retrofit)
    implementation(libs.retrofit.adapters.result)
    implementation(libs.jakarta.inject)
    implementation(libs.retrofit.kotlinx.serialization.converter)
    implementation(libs.kotlinx.coroutines.core)

    api(libs.kotlinx.serialization)
    api(libs.okhttp)
}

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.ainsln.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jakarta.inject)

    api(projects.core.model)
    api(projects.core.common)
    compileOnly(libs.retrofit)
    implementation(projects.core.network)
    implementation(projects.core.database)

    testImplementation(libs.androidx.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.runner)
    testImplementation (libs.mockito.kotlin)
}

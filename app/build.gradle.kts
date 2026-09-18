plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.myexpenseanalyzer.app"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.myexpenseanalyzer.app"
        minSdk = 26
        targetSdk = 37
        versionCode = 6
        versionName = "4.1"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    // Compose
    implementation(platform("androidx.compose:compose-bom:2026.08.00"))

    implementation("androidx.activity:activity-compose:1.13.0")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.4.0")
    implementation("androidx.compose.material:material-icons-extended")

    // AppCompat - App Language
    implementation("androidx.appcompat:appcompat:1.7.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0")
    implementation("androidx.lifecycle:lifecycle-process:2.11.0")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.10.1")

    // Android Core
    implementation("androidx.core:core-ktx:1.19.0")

    // Room Database
    implementation("androidx.room:room-runtime:2.8.5")
    implementation("androidx.room:room-ktx:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.2.1")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
}
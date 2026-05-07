plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.dokka") version "1.9.20"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.kachow_cursach"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kachow_cursach"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.navigation:navigation-compose:2.8.9") // Navigation
    implementation("androidx.core:core-splashscreen:1.0.1") //Экран-заставка
    implementation("io.coil-kt:coil-compose:2.6.0")// фото в полноэкранном режиме

    implementation("io.ktor:ktor-client-core:2.3.12") // ktor client
    implementation("io.ktor:ktor-client-cio:2.3.12") // ktor client
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12") // ktor client
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12") // ktor client
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Kotlinx serialization
    implementation("io.coil-kt:coil-compose:2.6.0") // Coil для загрузки фото


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

tasks.dokkaHtml {
    outputDirectory.set(file("$buildDir/dokka"))
}
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.massa.irecipe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.massa.irecipe"
        minSdk = 29
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview.android)
    ksp(libs.roomCompiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.retrofitGson)
    implementation(libs.koin)
    implementation(libs.koinCompose)
    implementation(libs.room)
    implementation(libs.roomKtx)
    implementation(libs.coil)
    implementation(libs.activity.compose)
    implementation(libs.compose.runtime)
    implementation(libs.androidx.material3.android)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.runtime.livedata)
    implementation(libs.lifecycle.runtime.compose)

    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutinesTest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
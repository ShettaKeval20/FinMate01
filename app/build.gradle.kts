plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)

    alias(libs.plugins.hilt) // Hilt plugin alias
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin") // Required
}

android {
    namespace = "com.example.finmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.finmate"
        minSdk = 33
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)


//    Splash Screen
    implementation ("androidx.core:core-splashscreen:1.0.0")

//    Material Icons
    implementation ("androidx.compose.material:material-icons-extended")

//    Navigation Graph
    implementation ("androidx.navigation:navigation-compose:2.7.7")

    // Lottie for animations
    implementation("com.airbnb.android:lottie-compose:6.1.0")

// Jetpack Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

//    Pager
    implementation("com.google.accompanist:accompanist-pager:0.34.0") // latest version as of now
    implementation("com.google.accompanist:accompanist-pager-indicators:0.34.0")

    implementation("com.google.android.gms:play-services-auth:20.7.0")

////    Firebase Analytics
//    implementation ("com.google.firebase:firebase-analytics-ktx")

//    Country Picker
    implementation("io.github.joelkanyi:komposecountrycodepicker:1.4.0")

    // Facebook Login
    implementation ("com.facebook.android:facebook-login:16.3.0")

// Firebase Auth
    implementation ("com.google.firebase:firebase-auth")
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)

//    Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation("com.google.dagger:hilt-android:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt ("androidx.hilt:hilt-compiler:1.2.0")

    implementation ("com.google.android.material:material:1.11.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
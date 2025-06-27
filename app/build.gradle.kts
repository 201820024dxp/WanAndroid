plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.wanandroid.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wanandroid.app"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

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
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    // leakcanary
    debugImplementation(libs.leakcanary.android)
    // lifecycle-livedata-ktx
    implementation(libs.androidx.lifecycle.livedata.ktx)
    // hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    // swipe refresh
    implementation(libs.androidx.swiperefreshlayout)
    // retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    // Glide
    implementation(libs.glide)
    // banner
    implementation(libs.banner)
    // flexbox
    implementation(libs.flexbox)
    // paging
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.runtime.ktx)
    // agentWeb
    implementation(libs.agentweb.core)
    // dataStore
    implementation(libs.androidx.datastore.preferences)
    // kotlin serial
    implementation(libs.kotlinx.serialization.json)
    // BRVAH
    implementation(libs.baserecyclerviewadapterhelper4)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
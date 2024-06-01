plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id ("kotlin-parcelize")
    id ("androidx.navigation.safeargs.kotlin")
    id ("dagger.hilt.android.plugin")
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.mhs.weatherapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mhs.weatherapp"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
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

    /**Material Design **/
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.appcompat:appcompat:1.6.1")

    /** Scalable Size Units(dimens) **/
    implementation ("com.intuit.sdp:sdp-android:1.0.6")
    implementation ("com.intuit.ssp:ssp-android:1.0.6")

    /** coroutines **/
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    /** viewModel lifecycle **/
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.activity:activity-ktx:1.8.0")

    /** dagger hilt **/
    implementation ("com.google.dagger:hilt-android:2.47")
    kapt ("com.google.dagger:hilt-compiler:2.47")
    kapt ("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")

    /** Gson converter **/
    implementation ("com.google.code.gson:gson:2.9.1")

    /** Retrofit & OkHttp **/
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.1")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")
}
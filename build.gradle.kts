buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.4")
        // Other dependencies...
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id ("com.android.library") version "7.3.1" apply false
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    var hiltVersion = "2.51.1"
    var kotlinVersion = "2.0.0"

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.2.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false

    alias(libs.plugins.compose.compiler) apply false
}
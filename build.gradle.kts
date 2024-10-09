// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false

    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.8.2" apply false
}

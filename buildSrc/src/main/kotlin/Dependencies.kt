package com.developer.amukovozov.nerd.buildSrc

object Versions {
    const val ktlint = "0.40.0"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.0"

    object Accompanist {
        private const val version = "0.18.0"
        const val coil = "io.coil-kt:coil-compose:1.3.2"
        const val insets = "com.google.accompanist:accompanist-insets:$version"
        const val swipeToRefresh = "com.google.accompanist:accompanist-swiperefresh:$version"
    }

    object Kotlin {
        private const val version = "1.5.10"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.4.1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Di {
        private const val version = "2.38.1"
        const val hiltGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:2.28-alpha"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltKapt = "com.google.dagger:hilt-android-compiler:$version"
    }

    object Network {
        object Retrofit {
            private const val version = "2.9.0"
            const val core = "com.squareup.retrofit2:retrofit:$version"
            const val rxJavaAdapter = "com.squareup.retrofit2:adapter-rxjava3:$version"
            const val moshiAdapter = "com.squareup.retrofit2:converter-moshi:$version"
        }

        object Moshi {
            private const val version = "1.12.0"
            const val core = "com.squareup.moshi:moshi-kotlin:$version"
            const val codegen = "com.squareup.moshi:moshi-kotlin-codegen:$version"

        }

        const val rxJava = "io.reactivex.rxjava3:rxjava:3.0.12"
        const val rxAndroid = "io.reactivex.rxjava3:rxandroid:3.0.0"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.6.0"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val browser = "androidx.browser:browser:1.3.0"

        object Compose {
            const val snapshot = ""
            const val version = "1.0.2"

            const val foundation = "androidx.compose.foundation:foundation:${version}"
            const val layout = "androidx.compose.foundation:foundation-layout:${version}"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val runtime = "androidx.compose.runtime:runtime:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val animation = "androidx.compose.animation:animation:${version}"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha05"
            const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-beta01"
        }

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.3.0"
        }

        object Lifecycle {
            const val viewModelCompose =
                "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"
        }

        object Emoji {
            private const val version = "1.2.0-alpha03"
            const val core = "androidx.emoji:emoji:$version"
            const val bundled = "androidx.emoji:emoji-bundled:$version"

        }
    }

    object Debugging {
        private const val timberVersion = "4.7.1"

        const val timber = "com.jakewharton.timber:timber:$timberVersion"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.1"
    }
}

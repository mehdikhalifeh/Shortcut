plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.mehdi.shortcutdemo"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.mehdi.shortcutdemo"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":shortcut-core"))
    implementation(project(":shortcut-compose"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
}

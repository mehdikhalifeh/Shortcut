plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.mehdi.shortcut"
    compileSdk = 37

    defaultConfig {
        // androidx.core 1.19+ requires 23; shortcuts are functionally inert below API 25 regardless.
        minSdk = 23
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(libs.androidx.core.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
}

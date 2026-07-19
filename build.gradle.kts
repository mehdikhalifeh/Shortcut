plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.dokka)
}

// Aggregated API docs for the published modules: ./gradlew :dokkaGenerate
dependencies {
    dokka(project(":shortcut-core"))
    dokka(project(":shortcut-compose"))
}

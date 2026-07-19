import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.binary.compatibility.validator)
}

android {
    namespace = "com.mehdi.shortcut.compose"
    compileSdk = 37

    defaultConfig {
        minSdk = 23
    }

    buildFeatures {
        compose = true
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
    api(project(":shortcut-core"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.core.ktx)

    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.androidx.activity.compose)
    testImplementation(libs.androidx.compose.ui.test.manifest)

    // See shortcut-core: BCV's worker classpath under built-in Kotlin.
    "bcv-rt-jvm-cp"(libs.kotlin.metadata.jvm)
}

mavenPublishing {
    configure(
        AndroidSingleVariantLibrary(
            variant = "release",
            sourcesJar = true,
            publishJavadocJar = true,
        ),
    )
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.mehdikhalifeh", "shortcut-compose", "2.0.0")

    pom {
        name = "Shortcut Compose"
        description =
            "Jetpack Compose bindings for the Shortcut library: lifecycle-aware dynamic shortcuts and pin requests."
        inceptionYear = "2019"
        url = "https://github.com/mehdikhalifeh/Shortcut"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "mehdikhalifeh"
                name = "Mehdi Khalifeh"
                url = "https://github.com/mehdikhalifeh"
            }
        }
        scm {
            url = "https://github.com/mehdikhalifeh/Shortcut"
            connection = "scm:git:git://github.com/mehdikhalifeh/Shortcut.git"
            developerConnection = "scm:git:ssh://git@github.com/mehdikhalifeh/Shortcut.git"
        }
    }
}

// Same manual BCV wiring as shortcut-core (Kotlin/binary-compatibility-validator#312).
afterEvaluate {
    val apiBuild =
        tasks.register<KotlinApiBuildTask>("apiBuild") {
            inputClassesDirs.from(tasks.named("compileReleaseKotlin").map { it.outputs.files })
            inputClassesDirs.from(tasks.named("compileReleaseJavaWithJavac").map { it.outputs.files })
            runtimeClasspath.from(configurations.named("bcv-rt-jvm-cp-resolver"))
            outputApiFile = layout.buildDirectory.file("bcv/shortcut-compose.api")
        }

    val apiCheck =
        tasks.register<KotlinApiCompareTask>("apiCheck") {
            group = "verification"
            description = "Checks the public API against the committed api/shortcut-compose.api dump."
            projectApiFile = layout.projectDirectory.file("api/shortcut-compose.api")
            generatedApiFile = apiBuild.flatMap { it.outputApiFile }
        }

    tasks.register<Copy>("apiDump") {
        group = "verification"
        description = "Updates the committed api/shortcut-compose.api dump."
        from(apiBuild.flatMap { it.outputApiFile })
        into(layout.projectDirectory.dir("api"))
    }

    tasks.named("check") { dependsOn(apiCheck) }
}

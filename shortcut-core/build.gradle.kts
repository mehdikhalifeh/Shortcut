import com.vanniktech.maven.publish.AndroidSingleVariantLibrary
import kotlinx.validation.KotlinApiBuildTask
import kotlinx.validation.KotlinApiCompareTask

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.dokka)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.binary.compatibility.validator)
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

    // Under built-in Kotlin, BCV never populates its worker classpath with the
    // metadata reader (see the BCV wiring note below), so declare it explicitly.
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

    coordinates("io.github.mehdikhalifeh", "shortcut-core", "1.1.0")

    pom {
        name = "Shortcut"
        description =
            "A small Kotlin library for dynamic and pinned Android app shortcuts, built on ShortcutManagerCompat."
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

// ---------------------------------------------------------------------------
// Binary compatibility validation.
//
// BCV registers no tasks under AGP 9 built-in Kotlin
// (Kotlin/binary-compatibility-validator#312) and KGP's built-in abiValidation
// is equally unavailable, so BCV's task types are wired manually against the
// release compile outputs. Remove once upstream support lands (KT-78025).
// ---------------------------------------------------------------------------
afterEvaluate {
    val apiBuild =
        tasks.register<KotlinApiBuildTask>("apiBuild") {
            inputClassesDirs.from(tasks.named("compileReleaseKotlin").map { it.outputs.files })
            inputClassesDirs.from(tasks.named("compileReleaseJavaWithJavac").map { it.outputs.files })
            runtimeClasspath.from(configurations.named("bcv-rt-jvm-cp-resolver"))
            outputApiFile = layout.buildDirectory.file("bcv/shortcut-core.api")
        }

    val apiCheck =
        tasks.register<KotlinApiCompareTask>("apiCheck") {
            group = "verification"
            description = "Checks the public API against the committed api/shortcut-core.api dump."
            projectApiFile = layout.projectDirectory.file("api/shortcut-core.api")
            generatedApiFile = apiBuild.flatMap { it.outputApiFile }
        }

    tasks.register<Copy>("apiDump") {
        group = "verification"
        description = "Updates the committed api/shortcut-core.api dump."
        from(apiBuild.flatMap { it.outputApiFile })
        into(layout.projectDirectory.dir("api"))
    }

    tasks.named("check") { dependsOn(apiCheck) }
}

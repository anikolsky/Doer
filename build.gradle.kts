buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath("com.google.gms:google-services:4.3.15")
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}

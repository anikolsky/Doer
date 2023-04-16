buildscript {
    extra.apply {
        set("compose_ui_version", "1.4.1")
        set("accompanist_version", "0.30.0")
        set("hilt_compose_version", "1.0.0")
        set("hilt_version", "2.45")
        set("lifecycle_version", "2.6.1")
        set("nav_version", "2.5.3")
        set("room_version", "2.5.1")
    }
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${rootProject.extra.get("hilt_version") as String}")
    }
}

tasks {
    register("clean", Delete::class) {
        delete(rootProject.buildDir)
    }
}
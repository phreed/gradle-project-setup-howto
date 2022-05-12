pluginManagement {
    // Get community plugins from the Gradle Plugin Portal
    repositories.gradlePluginPortal()

    // Get our own convention plugins from 'gradle/plugins'
    if (File(rootDir, "gradle/plugins").exists()) {
        includeBuild("gradle/plugins")
    }
    // If not the main build, 'plugins' is located next to the build (e.g. gradle/settings)
    if (File(rootDir, "../plugins").exists()) {
        includeBuild("../plugins")
    }
}

plugins {
    // Use the Gradle Enterprise plugin to publish Build Scan to https://scans.gradle.com
    id("com.gradle.enterprise")
}

dependencyResolutionManagement {
    // Get components from Maven Central
    repositories.mavenCentral()
    // In the main build, find the platform in 'gradle/platform'
    if (File(rootDir, "gradle/platform").exists()) {
        includeBuild("gradle/platform")
    }
}

// Include all subfolders that contain a 'build.gradle.kts' as subprojects
rootDir.listFiles()?.filter { File(it, "build.gradle.kts").exists() }?.forEach { subproject ->
    include(subproject.name)
}

// Configure the Gradle Enterprise plugin
gradleEnterprise {
    buildScan {
        publishAlways()
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}


pluginManagement {
    includeBuild("../settings")
}
plugins {
    id("org.example.settings")
}

dependencyResolutionManagement {
    repositories.gradlePluginPortal()
    repositories.google()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform(project(":plugins-platform")))

    implementation("com.autonomousapps:dependency-analysis-gradle-plugin")
    implementation("de.jjohannes.gradle:java-module-dependencies")
}

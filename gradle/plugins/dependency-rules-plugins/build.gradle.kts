plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform(project(":plugins-platform")))

    implementation("de.jjohannes.gradle:java-ecosystem-capabilities")
    implementation("de.jjohannes.gradle:missing-metadata-guava")
    implementation("dev.jacomet.gradle.plugins:logging-capabilities")
}

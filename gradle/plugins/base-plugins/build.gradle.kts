plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform(project(":plugins-platform")))

    implementation(project(":dependency-rules-plugins"))

    implementation("de.jjohannes.gradle:extra-java-module-info")
}

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform(project(":plugins-platform")))
    
    implementation(project(":base-plugins"))

    implementation("de.jjohannes.gradle:java-module-dependencies")
    implementation("de.jjohannes.gradle:java-module-testing")
}

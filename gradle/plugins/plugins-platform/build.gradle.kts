plugins {
    id("java-platform")
}

dependencies.constraints {
    api("de.jjohannes.gradle:extra-java-module-info:0.12")
    api("de.jjohannes.gradle:java-ecosystem-capabilities:0.4")
    api("de.jjohannes.gradle:java-module-dependencies:0.7")
    api("de.jjohannes.gradle:java-module-testing:0.1")
    api("de.jjohannes.gradle:missing-metadata-guava:31.1.1")
    api("dev.jacomet.gradle.plugins:logging-capabilities:0.10.0")
}

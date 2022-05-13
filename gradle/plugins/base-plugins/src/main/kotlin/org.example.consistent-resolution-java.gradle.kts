plugins {
    id("java")
    id("org.example.consistent-resolution")
}

// Every compile classpath and runtime classpath uses the versions of the
sourceSets.all {
    configurations[compileClasspathConfigurationName].shouldResolveConsistentlyWith(configurations["appRuntimeClasspath"])
    configurations[runtimeClasspathConfigurationName].shouldResolveConsistentlyWith(configurations["appRuntimeClasspath"])
    // Source sets without production code (tests / fixtures) are allowed to have dependencies that are
    // not part of the consistent resolution result and might need additional version information
    if (this != sourceSets.main.get()) {
        dependencies.add(implementationConfigurationName, dependencies.platform("org.example.product:platform"))
    }
}

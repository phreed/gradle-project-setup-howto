plugins {
    id("java")
}

// Expose the ':app' project runtime classpath in every project
val appRuntimeClasspath = configurations.create("appRuntimeClasspath") {
    description = "Runtime classpath of the complete application"
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        // We want the runtime classpath represented by Usage.JAVA_RUNTIME
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
    }
    withDependencies {
        // Depend on ':app' and with this on all its (transitive) dependencies
        add(project.dependencies.create(project(":app")))
        // Get our own version information from the platform project
        add(project.dependencies.create(project.dependencies.platform("org.example.product:platform")))
    }
}

// Every compile classpath and runtime classpath uses the versions of the
sourceSets.all {
    configurations[compileClasspathConfigurationName].shouldResolveConsistentlyWith(appRuntimeClasspath)
    configurations[runtimeClasspathConfigurationName].shouldResolveConsistentlyWith(appRuntimeClasspath)
    // Source sets without production code (tests / fixtures) are allowed to have dependencies that are
    // not part of the consistent resolution result and might need additional version information
    if (this != sourceSets.main.get()) {
        dependencies.add(implementationConfigurationName, dependencies.platform("org.example.product:platform"))
    }
}

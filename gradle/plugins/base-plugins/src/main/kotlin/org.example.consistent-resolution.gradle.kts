import com.android.build.api.attributes.BuildTypeAttr

plugins {
    id("org.example.dependency-analysis-project")
}

// Expose the ':app' project runtime classpath in every project
val appRuntimeClasspath = configurations.create("appRuntimeClasspath") {
    description = "Runtime classpath of the complete application"
    isCanBeConsumed = false
    isCanBeResolved = true
    attributes {
        // We want the runtime classpath represented by Usage.JAVA_RUNTIME
        attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.JAVA_RUNTIME))
        attribute(BuildTypeAttr.ATTRIBUTE, objects.named("release"))
    }
    withDependencies {
        // Depend on ':app' and with this on all its (transitive) dependencies
        add(project.dependencies.create(project(":app")))
        // Get our own version information from the platform project
        add(project.dependencies.create(project.dependencies.platform("org.example.product:platform")))
    }
}

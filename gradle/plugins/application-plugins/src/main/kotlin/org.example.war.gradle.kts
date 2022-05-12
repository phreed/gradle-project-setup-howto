plugins {
    id("war")
    id("org.example.java")
}

// The war plugin used 'providedRuntime' / 'providedCompile' to resolve dependencies for packaging the WAR file
configurations.providedCompile {
    shouldResolveConsistentlyWith(configurations.appRuntimeClasspath.get())
}
configurations.providedRuntime {
    shouldResolveConsistentlyWith(configurations.appRuntimeClasspath.get())
}

tasks.register<Copy>("deployWebApp") {
    group = "distribution"
    description = "Deploy web app into local Tomcat found via CATALINA_HOME"
    from(tasks.war) {
        into("webapps")
    }
    into(providers.environmentVariable("CATALINA_HOME").orElse(provider {
        throw RuntimeException("CATALINA_HOME environment variable not set")
    }))
}

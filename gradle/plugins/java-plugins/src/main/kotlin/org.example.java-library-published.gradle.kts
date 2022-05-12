plugins {
    id("maven-publish")
    id("org.example.java-library")
}

// Publish with sources and Javadoc
java {
    withSourcesJar()
    withJavadocJar()
}

// Configure details of Javadoc generation
tasks.javadoc {
    (options as StandardJavadocDocletOptions).apply {
        memberLevel = JavadocMemberLevel.PUBLIC
        isAuthor = true
    }
}

publishing.publications.create<MavenPublication>("mavenJava") {
    from(components["java"])

    // We use consistent reslution + a platform for controlling versions
    // -> Publish the versions that are the result of the consistent resolution
    versionMapping {
        usage("java-api") {
            fromResolutionResult()
        }
        usage("java-runtime") {
            fromResolutionResult()
        }
    }
}

// The repository to publish to
publishing.repositories {
    maven("/tmp/my-repo")
}

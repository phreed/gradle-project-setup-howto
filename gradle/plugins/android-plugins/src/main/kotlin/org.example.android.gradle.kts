import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.withType

plugins {
    id("com.android.base")
    id("org.example.base")
}

// Configure details for *all* test executions direclty on 'Test' task
tasks.withType<Test>().configureEach {
    useJUnitPlatform() // Use JUnit 5 as test framework

    testLogging.showStandardStreams = true

    maxHeapSize = "1g"
    systemProperty("file.encoding", "UTF-8")
}


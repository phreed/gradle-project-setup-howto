import org.example.application.tasks.MD5DirectoryChecksum
import org.example.application.tasks.VersionXml

plugins {
    id("application")               // For stand-alone application packaging
    id("jacoco-report-aggregation") // get and aggregated coverage report for all tests
    id("test-report-aggregation")   // get and aggregated result report for all tests
    id("org.example.war")           // For web application packaging/deployment
    id("org.example.end2end-testing")
}

configurations.allCodeCoverageReportClassDirectories {
    shouldResolveConsistentlyWith(configurations.appRuntimeClasspath.get())
}

// Generate additional resources required at application runtime
val generateVersionXml = tasks.register<VersionXml>("generateVersionXml") {
    mainVersion.set(providers.fileContents(
        rootProject.layout.projectDirectory.file("gradle/version.txt")).asText)
    xmlFile.set(layout.buildDirectory.file("generated-resources/xml/version.xml"))
}
val resourcesChecksum = tasks.register<MD5DirectoryChecksum>("resourcesChecksum") {
    inputDirectory.set(layout.projectDirectory.dir("src/main/resources"))
    checksumFile.set(layout.buildDirectory.file("generated-resources/md5/resources.MD5"))
}

tasks.processResources {
    from(generateVersionXml)
    from(resourcesChecksum)
}

tasks.check {
    dependsOn(tasks.testAggregateTestReport)
    dependsOn(tasks.testCodeCoverageReport)
}

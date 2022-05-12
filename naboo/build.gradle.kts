plugins {
    id("org.example.java-library")
}

dependencies {
    implementation("org.apache.solr:solr-solrj")
    implementation("org.apache.zookeeper:zookeeper-jute")

    testImplementation("junit:junit")
    testImplementation("org.junit.jupiter:junit-jupiter-api")

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine") // Some JUnit4 tests still around only in this project, use 'vintage engine' to run them
}

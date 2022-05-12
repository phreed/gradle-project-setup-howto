plugins {
    id("org.example.java-library")
}

tasks.test {
    useJUnit()  // We run the old JUnit4 tests directly with JUnit4 because of _some obscure test setup reason_
}

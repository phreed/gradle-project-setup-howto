plugins {
    id("org.example.java-library")
}

dependencies {
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine") // Some JUnit4 tests still around only in this project, use 'vintage engine' to run them
}

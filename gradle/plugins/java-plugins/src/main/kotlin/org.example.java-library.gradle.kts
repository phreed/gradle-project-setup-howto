plugins {
    id("java-library")
    id("de.jjohannes.java-module-testing")
    id("org.example.java")
}

javaModuleTesting.blackbox(testing.suites.getByName("test"))

plugins {
    id("org.example.application")
}

application {
    mainModule.set("org.example.product.app")
    mainClass.set("org.example.product.app.Application")
}

dependencies {
    javaModuleDependencies {
        runtimeOnly(ga("org.slf4j.simple"))
    }
}

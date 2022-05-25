plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(platform(project(":plugins-platform")))
    
    implementation(project(":base-plugins"))

    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-gradle-plugin
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")

}

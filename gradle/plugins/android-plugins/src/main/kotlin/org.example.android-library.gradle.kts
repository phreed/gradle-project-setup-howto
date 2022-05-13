plugins {
    id("com.android.library")
    id("org.example.android")
    id("org.example.consistent-resolution-android-library")
}

android {
    compileSdk = 30

    defaultConfig {
        minSdk = 26
        targetSdk = 31
    }
}

// Configure common test runtime dependencies for *all* android projects
dependencies {
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.slf4j:slf4j-simple")

    androidTestRuntimeOnly("androidx.test.espresso:espresso-core")
}

plugins {
    id("com.android.application")
    id("org.example.android")
    id("org.example.consistent-resolution-android-application")
}

val versionString = version as String
val versionInt = versionString.split(".")[0].toInt() * 1000 + versionString.split(".")[1].toInt()

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "org.example.product.app"
        minSdk = 26
        targetSdk = 31
        versionCode = versionInt
        versionName = versionString

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes.getByName("release") {
        minifyEnabled(false)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    packagingOptions {
        resources.excludes.add("META-INF/**")
    }
}

// Configure common test runtime dependencies for *all* android projects
dependencies {
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.slf4j:slf4j-simple")

    androidTestRuntimeOnly("androidx.test.espresso:espresso-core")
}

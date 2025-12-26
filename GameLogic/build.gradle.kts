plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.gamelogic"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
    dependencies {
        implementation(files("../app/libs/json-20250517.jar"))
        implementation("com.google.code.gson:gson:2.8.9")
        implementation(project(":AndroidEngine"))
    }


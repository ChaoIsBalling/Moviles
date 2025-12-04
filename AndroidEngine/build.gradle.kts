 plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.androidengine"
    compileSdk = 36

    defaultConfig {
        minSdk = 30

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

 tasks.register<Copy>("Copy"){
     from(rootDir.getAbsolutePath() + "/data")
     into("src/main/assets")
 }

 tasks.preBuild(){
     dependsOn("Copy")
 }
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(project(":Engine"))
    implementation(files("../app/libs/json-20250517.jar"))
    implementation(libs.work.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("com.google.android.gms:play-services-ads:24.8.0")
}
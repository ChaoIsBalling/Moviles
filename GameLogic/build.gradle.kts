plugins {
    id("java-library")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
dependencies {
    implementation(project(":Engine"))
    implementation(files("../app/libs/json-20250517.jar"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation(project(":AndroidEngine"))
}

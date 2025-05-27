plugins {
    // Plugins are applied by the parent `backend/build.gradle.kts`
}

dependencies {
    implementation(project(":backend:shared"))
    // implementation(libs.kotlinResult) // Add this if/when kotlin-result is directly used by this module

    // stdlib and spring-boot-starter-test are inherited from parent subprojects block
}

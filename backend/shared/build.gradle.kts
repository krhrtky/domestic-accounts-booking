plugins {
    // Plugins are applied by the parent `backend/build.gradle.kts`
}

dependencies {
    // Common libraries can be added here.
    // For example, kotlin-result, if it's intended for broad use across modules.
    implementation(libs.kotlinResult) // Using alias from libs.versions.toml

    // Consider other shared utilities, e.g., Apache Commons Lang, Guava, etc.
    // implementation(libs.apache.commons.lang) // Example, if defined in toml

    // stdlib and spring-boot-starter-test are inherited from parent subprojects block
}

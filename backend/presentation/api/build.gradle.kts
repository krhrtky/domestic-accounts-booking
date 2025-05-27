plugins {
    // Plugins are applied by the parent `backend/build.gradle.kts`
}

dependencies {
    implementation(project(":backend:shared"))
    implementation(libs.spring.boot.starter.web) // Use alias
    // stdlib and spring-boot-starter-test are inherited from parent subprojects block
}

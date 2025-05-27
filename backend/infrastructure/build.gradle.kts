plugins {
    // Plugins are applied by the parent `backend/build.gradle.kts`
}

dependencies {
    implementation(project(":backend:shared"))
    implementation(project(":backend:domains:user"))    // For user repository interfaces
    implementation(project(":backend:domains:expense"))  // For expense repository interfaces

    // Add Spring Boot starter for data access.
    // Choose one or more based on actual persistence choice (e.g., JPA, JOOQ).
    // These aliases come from libs.versions.toml
    implementation(libs.spring.boot.starter.dataJpa) // Example if using JPA
    // implementation(libs.spring.boot.starter.jooq) // Example if using jOOQ

    // Runtime only database driver if using JPA/JDBC directly (example for PostgreSQL)
    // runtimeOnly("org.postgresql:postgresql") // Version for this would typically be managed by Spring Boot BOM

    // stdlib and spring-boot-starter-test are inherited from parent subprojects block
}

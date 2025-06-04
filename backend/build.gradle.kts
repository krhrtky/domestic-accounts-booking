plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
    implementation(project(":backend:presentation:api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

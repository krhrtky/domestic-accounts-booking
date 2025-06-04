plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
    implementation(project(":backend:domains:user"))
    implementation(project(":backend:domains:expense"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

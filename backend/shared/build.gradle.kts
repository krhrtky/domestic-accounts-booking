plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.5")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

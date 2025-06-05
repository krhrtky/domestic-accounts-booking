plugins {
    kotlin("jvm") version "2.1.20" apply false
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
}

allprojects {
    group = "com.example"
    version = "0.1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    repositories {
        mavenCentral()
    }
}

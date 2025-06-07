plugins {
    kotlin("jvm") version "2.0.21" apply false
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.8" apply false
}

allprojects {
    group = "com.example"
    version = "0.1.0-SNAPSHOT"
}

subprojects {
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "io.gitlab.arturbosch.detekt")
    repositories {
        mavenCentral()
    }

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(rootProject.file("detekt.yml"))
        buildUponDefaultConfig = true
        baseline = rootProject.file(".detekt-baseline.yml")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "21"
    }
}

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {

    apply(plugin = "java")
    apply(plugin = "kotlin")
    java.sourceCompatibility = JavaVersion.VERSION_17
    group = "net.krhrtky"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<ProcessResources> {
        duplicatesStrategy = DuplicatesStrategy.WARN
    }
}



subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        // Common dependencies will be configured here
        implementation(kotlin("stdlib"))
        implementation(kotlin("reflect"))

        // Test dependencies
        testImplementation(kotlin("test"))
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

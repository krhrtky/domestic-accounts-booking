plugins {
    alias(libs.plugins.kotlin.jvm) apply false // Use alias
    alias(libs.plugins.spring.boot) apply false // Use alias
    alias(libs.plugins.spring.dependency.management) apply false // Use alias
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = libs.plugins.kotlin.jvm.get().pluginId)
    apply(plugin = libs.plugins.spring.boot.get().pluginId)
    apply(plugin = libs.plugins.spring.dependency.management.get().pluginId)

    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    dependencies {
        implementation(libs.kotlin.stdlib) // Use alias (version managed by Spring Boot BOM or defined in catalog)
        // implementation(libs.spring.boot.starter) // Example if we add it here
        testImplementation(libs.spring.boot.starter.test) { // Use alias
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

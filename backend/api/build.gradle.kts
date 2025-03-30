plugins {
    id("org.jetbrains.kotlin.plugin.spring") version "2.1.10"
    id("io.spring.dependency-management") version "1.1.4"
}

dependencyManagement {
    imports {
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.6.0")
    }
}

val developmentOnly: Configuration by configurations.creating

configurations {
    developmentOnly
    runtimeClasspath {
        extendsFrom(configurations["developmentOnly"])
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.4.3")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.4.3")
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(project(":backend:domains"))
    implementation(project(":backend:infrastructure"))
    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    developmentOnly("org.springframework.boot:spring-boot-devtools:3.4.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.3") {
        exclude("org.junit.vintage:junit-vintage-engine")
        exclude(module = "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:4.0.2")
    testImplementation(kotlin("test"))
}



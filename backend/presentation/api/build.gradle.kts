plugins {
    kotlin("jvm")
    alias(libs.plugins.graphql.kotlin)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.2.5")
    implementation(libs.graphql.kotlin.spring.server)
    implementation(project(":backend:domains:user"))
    implementation(project(":backend:domains:expense"))
    implementation(project(":backend:shared"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

graphql {
    schema {
        packages = listOf("com.example.api.graphql")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

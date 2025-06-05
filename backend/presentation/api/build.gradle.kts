plugins {
    kotlin("jvm")
    alias(libs.plugins.graphql.kotlin)
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:3.2.5")
    implementation(libs.graphql.kotlin.spring.server)
    implementation(project(":backend:domains:user"))
    implementation(project(":backend:domains:expense"))
}

graphql {
    schema {
        packages = listOf("com.example.api.graphql")
    }
    client {
        schemaFile = file("${project.rootDir}/interface/schema.graphql")
        queryFileDirectory = "${project.rootDir}/interface/src/graphql"
        packageName = "com.example.api.generated"
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":backend:domains:user"))
    implementation(project(":backend:domains:expense"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

plugins {
    id("nu.studer.jooq") version "8.2"
    id("org.jetbrains.kotlin.plugin.spring") version "2.1.10"
}

dependencies {
    implementation(project(":backend:domains"))

    // JOOQ
    implementation("org.jooq:jooq:3.18.5")
    implementation("org.jooq:jooq-meta:3.18.5")
    implementation("org.jooq:jooq-kotlin:3.18.5")
    implementation("org.jooq:jooq-kotlin-coroutines:3.18.5")
    jooqGenerator("org.postgresql:postgresql:42.7.3")

    // Database
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("com.zaxxer:HikariCP:5.0.1")

    // Test dependencies
    testImplementation(project(":backend:domains"))
    implementation("org.springframework.boot:spring-boot-starter-jooq:3.4.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:2.1.10")

    // Spring Boot Test & TestContainers
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.4.3")
    testImplementation("org.springframework.boot:spring-boot-testcontainers:3.4.3")
    testImplementation("org.testcontainers:postgresql:1.18.3")
}

// JOOQ code generation configuration
jooq {
    version.set("3.18.5")

    configurations {
        create("main") {
            generateSchemaSourceOnCompilation.set(true)  // コンパイル時にコード生成を有効化

            jooqConfiguration.apply {
                logging = org.jooq.meta.jaxb.Logging.WARN

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:5432/app"  // DockerコンテナのDB名に合わせて更新
                    user = "root"
                    password = "password"
                }

                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        includes = ".*"
                        excludes = "flyway_schema_history"
                    }

                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }

                    target.apply {
                        packageName = "com.kurihara.takuya.domestic.accounts.book.infrastructure.jooq.generated"
                        directory = "build/generated-src/jooq/main"
                    }

                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

// 既存のタスクやその他の設定は変更なし...

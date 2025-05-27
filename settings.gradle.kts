dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Recommended practice
    repositories {
        mavenCentral()
        // You can add other repositories like Gradle Plugin Portal if needed for plugins not on mavenCentral
        // gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "domestic-accouts-booking"

include(
    "backend",
    "backend:presentation:api",
    "backend:domains:user",
    "backend:domains:expense",
    "backend:infrastructure",
    "backend:shared",
)

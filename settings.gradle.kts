pluginManagement { repositories { gradlePluginPortal(); mavenCentral() } }
rootProject.name = "domestic-accounts-booking"

include(
    "backend",
    "backend:presentation:api",
    "backend:domains:user",
    "backend:domains:expense",
    "backend:infrastructure",
    "backend:shared",
)

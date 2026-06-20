pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "ike-backend"

include("platform", "finance", "academic", "notification", "audit", "app")

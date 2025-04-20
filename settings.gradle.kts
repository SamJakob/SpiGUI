rootProject.name = "SpiGUI"

dependencyResolutionManagement {
    versionCatalogs {
        create("libraries") {
            library("annotations.spotbugs", "com.github.spotbugs:spotbugs-annotations:4.2.0")

            library("spigot_v1", "org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
            library("spigot_v2", "org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

            library("mockbukkit", "org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.34.1")
        }
    }
}

// SpiGUI Core APIs (cross-version)
include("core")

// SpiGUI release packages
include("spigui")
include("spigui-v2")

// SpiGUI testing packages
include("demo")
include("integration-tests")

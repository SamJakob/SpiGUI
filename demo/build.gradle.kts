val jarName = "SpiGUI-demo"

plugins {
    java
}

repositories {
    mavenCentral()

    // OSS Sonatype (Snapshots) (for bungeecord-chat, used by Spigot)
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")

    // Spigot Nexus (for Spigot API)
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    // Spigot API
    compileOnly(libraries.spigot.v2)

    // SpiGUI Core
    implementation(project(":core"))
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

tasks.withType<Jar> {
    from("src/test/resources") {
        include("**")
    }

    archiveBaseName.set(jarName)
}

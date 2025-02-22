repositories {
    // PaperMC Nexus (for MockBukkit)
    maven (
        url = "https://repo.papermc.io/repository/maven-public/"
    ) {
        content {
            includeGroup("org.mockbukkit")
            includeGroup("io.papermc.paper")
            includeGroup("com.mojang")
            includeGroup("net.md-5")
        }
    }
}

dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    // MockBukkit
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.33.5")
}

tasks.withType<Jar> {
    archiveBaseName.set("SpiGUI-core")
}

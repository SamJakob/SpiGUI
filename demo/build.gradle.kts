dependencies {
    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    // SpiGUI Core
    implementation(project(":core"))
}

tasks.withType<Jar> {
    from("src/test/resources") {
        include("**")
    }

    archiveBaseName.set("SpiGUIDemo")
}

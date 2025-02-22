java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    maven(
        url = "https://jitpack.io"
    ) {
        content {
            includeGroup("com.github.MockBukkit")
        }
    }
}

dependencies {
    // Spigot 1.8 API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // MockBukkit
    testImplementation("com.github.MockBukkit:MockBukkit:v1.8-SNAPSHOT")

    // SpiGUI Core
    implementation(project(":core"))
}

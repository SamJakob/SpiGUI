plugins {
    TestingPlugin
    Mockito5Plugin
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    // JitPack
    maven (url = "https://jitpack.io")

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
    testImplementation(project(":core"))

    // MockBukkit
    testImplementation(libraries.mockbukkit)
}

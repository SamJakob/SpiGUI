plugins {
    TestingPlugin
}

dependencies {
    // JavaX annotations (@Nonnull and @Nullable)
    compileOnly(libraries.annotations.spotbugs)

    // Spigot API
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    testImplementation("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }

    withJavadocJar()
}

val javadocSources = sourceSets.create("javadoc")

tasks.withType<Javadoc> {
    javadocTool.set(javaToolchains.javadocToolFor {
        languageVersion = JavaLanguageVersion.of(21)
    })

    doLast {
        copy {
            from(javadocSources.resources.srcDirs.map { it.path })
            into(outputs.files.asPath)
        }
    }
}

tasks.withType<Jar> {
    archiveBaseName.set("SpiGUI-core")
}

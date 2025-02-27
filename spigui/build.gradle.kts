plugins {
    TestingPlugin
}

dependencies {
    implementation(project(":core"))

    // JavaX annotations (@Nonnull and @Nullable)
    compileOnly(libraries.annotations.spotbugs)

    // Spigot API
    compileOnly(libraries.spigot.v1)
    testImplementation(libraries.spigot.v1)
}

val core = project(":core")

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }

    sourceSets {
        main {
            java {
                srcDirs(core.sourceSets.main.get().allJava.srcDirs)
            }
        }
    }
}

val javadocSourceSet = sourceSets.create("javadoc") {
    resources {
        srcDirs(core.sourceSets.findByName("javadoc")!!.resources.srcDirs)
    }
}

tasks.withType<Javadoc> {
    javadocTool.set(javaToolchains.javadocToolFor {
        languageVersion = JavaLanguageVersion.of(21)
    })
}


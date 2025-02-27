plugins {
    TestingPlugin
}

dependencies {
    implementation(project(":core"))

    // Spigot API
    compileOnly(libraries.spigot.v2)
    testImplementation(libraries.spigot.v2)
}

val core = project(":core")

java {
    sourceSets {
        main {
            java {
                srcDirs(core.sourceSets.main.get().allJava.srcDirs)
            }
        }

        create("javadoc") {
            resources {
                srcDirs(core.sourceSets.findByName("javadoc")!!.resources.srcDirs)
            }
        }
    }
}

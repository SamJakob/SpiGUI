import java.net.URI

plugins {
    java
    `maven-publish`
    signing

    // Code formatting
    id("com.diffplug.spotless") version "7.0.2"
}

/**
 * If true, Sonatype upload credentials have been defined in ~/.gradle/gradle.properties.
 */
val hasSonatypeCredentials = project.hasProperty("ossrhUsername") && project.hasProperty("ossrhPassword")

allprojects {
    group = "com.samjakob"
    version = "2.0.0"
    project.uri("https://github.com/SamJakob/SpiGUI")

    apply(plugin = "java")

    repositories {
        mavenCentral()

        // OSS Sonatype (Snapshots) (for bungeecord-chat used by Spigot)
        maven (
            url = "https://oss.sonatype.org/content/repositories/snapshots/"
        ) {
            content {
                includeGroup("net.md-5")
                includeGroup("org.spigotmc")
            }
        }

        // Spigot Nexus (for Spigot API)
        maven (
            url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/"
        ) {
            content {
                includeGroup("org.bukkit")
                includeGroup("org.spigotmc")
            }
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

        withJavadocJar()
        withSourcesJar()
    }
}

subprojects {
    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:all")
        options.compilerArgs.add("-Werror")
    }

    tasks.withType<Javadoc> {
        val options = options as StandardJavadocDocletOptions

        if (JavaVersion.current().isJava9Compatible) {
            options.addBooleanOption("html5", true)
        }

        options.addBooleanOption("Xdoclint:all", true)

        source = sourceSets.main.get().allJava
        classpath += configurations.compileClasspath.get()

        options.memberLevel = JavadocMemberLevel.PRIVATE
    }
}

spotless {
    format("misc") {
        target("*.gradle", ".gitignore")

        trimTrailingWhitespace()
        leadingSpacesToTabs()
        endWithNewline()
    }

    format("yaml") {
        target("*.yaml", "*.yml")

        trimTrailingWhitespace()
        leadingTabsToSpaces(2)
        endWithNewline()
    }

    java {
        target(allprojects.map { it.sourceSets.main.get().allJava })
        palantirJavaFormat().formatJavadoc(true)
    }
}

publishing {
    publications {
        create<MavenPublication>("SpiGUI") {
            // Build only the core into the SpiGUI distributable JAR.
            from(project(":core").components["java"])

            pom {
                name = "SpiGUI"
                description = "A comprehensive GUI API for Spigot with pages support."
                url = "https://github.com/SamJakob/SpiGUI"
                packaging = "jar"

                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }

                developers {
                    developer {
                        name = "SamJakob"
                        email = "me@samjakob.com"
                        organization = "SamJakob"
                        organizationUrl = "https://samjakob.com"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/SamJakob/SpiGUI.git"
                    developerConnection = "scm:git:ssh://github.com:SamJakob/SpiGUI.git"
                    url = "https://github.com/SamJakob/SpiGUI"
                }
            }
        }
    }

    if (hasSonatypeCredentials) {
        repositories {
            maven {
                url = if (version.toString().endsWith("SNAPSHOT"))
                        // If the version ends with SNAPSHOT, we're building a snapshot
                        // so deploy to a SNAPSHOT repository.
                        URI("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                        // Otherwise, deploy to a staging repository.
                        else URI("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

                credentials {
                    username = project.findProperty("ossrhUsername")!!.toString()
                    password = project.findProperty("ossrhPassword")!!.toString()
                }
            }
        }
    }
}

if (hasSonatypeCredentials) {
    signing {
        useGpgCmd()
        sign(publishing.publications["SpiGUI"])
    }
}

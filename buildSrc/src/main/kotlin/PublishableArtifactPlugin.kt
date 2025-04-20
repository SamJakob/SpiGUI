
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType

const val SOURCES_QUALIFIER = "sources"
const val JAVADOC_QUALIFIER = "javadoc"

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PublishableJarArtifact(
    val name: String,
    val qualifier: String?,
    val jar: Jar,
)

@Suppress("unused", "MemberVisibilityCanBePrivate")
class PublishableJarArtifactSet(
    val jar: PublishableJarArtifact,
    val sourcesJar: PublishableJarArtifact,
    val javadocJar: PublishableJarArtifact,
) {
    fun forEach(action: (PublishableJarArtifact) -> Unit) {
        arrayOf(jar, sourcesJar, javadocJar).forEach(action)
    }
}

fun Project.definePublishableArtifacts(name: String, projectName: String): PublishableJarArtifactSet {

    evaluationDependsOnChildren()

    if (!projectName.startsWith(":")) {
        throw IllegalArgumentException("Cannot definePublishableArtifacts from a non-relative project. Expected projectName to start with a colon: '%s'".format(projectName))
    }

    val project: Project = project(projectName)
    if (!project.pluginManager.hasPlugin("java")) {
        throw IllegalStateException("Project '%s' is missing required plugin: java".format(projectName))
    }

    val sourceSets = project.extensions.getByType<JavaPluginExtension>().sourceSets
    val mainSourceSet = sourceSets.findByName("main")!!

    // Custom - additional sources for the Javadoc (e.g., images).
    val javadocSourceSet = sourceSets.findByName("javadoc")
    if (javadocSourceSet != null) {
        project.tasks.withType<Javadoc> {
            doLast {
                copy {
                    from(javadocSourceSet.resources.srcDirs.map { it.path })
                    into(outputs.files.asPath)
                }
            }
        }
    }

    val jar = tasks.register<Jar>(name) {
        archiveBaseName.set(name)

        from(mainSourceSet.output) {
            include("**")
        }
    }

    val sourcesJarTaskName = "%s-%s".format(name, SOURCES_QUALIFIER)
    val sourcesJar = tasks.register<Jar>(sourcesJarTaskName) {
        archiveBaseName.set(name)
        archiveClassifier.set(SOURCES_QUALIFIER)

        from(mainSourceSet.allSource) {
            include("**")
        }

        if (javadocSourceSet != null) {
            from(javadocSourceSet.resources) {
                include("**")
            }
        }
    }

    val javadocJarTaskName = "%s-%s".format(name, JAVADOC_QUALIFIER)
    val javadocJar = tasks.register<Jar>(javadocJarTaskName) {
        archiveBaseName.set(name)
        archiveClassifier.set(JAVADOC_QUALIFIER)

        from(project.tasks.getByName("javadoc"))
    }

    return PublishableJarArtifactSet(
        PublishableJarArtifact(name, null, jar.get()),
        PublishableJarArtifact(sourcesJarTaskName, SOURCES_QUALIFIER, sourcesJar.get()),
        PublishableJarArtifact(javadocJarTaskName, JAVADOC_QUALIFIER, javadocJar.get()),
    )

}
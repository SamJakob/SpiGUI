val mockitoVersion = "5.15.2"

dependencies {
    "testImplementation"("org.mockito:mockito-core:$mockitoVersion")!! as ExternalModuleDependency
    "testImplementation"("org.mockito:mockito-junit-jupiter:$mockitoVersion")
}

// Locate the mockito-core JAR and disable classpath sharing.
// This needs to be done afterEvaluate as that's when Gradle has built the classpath (and therefore determined the
// locations of all the relevant files).
afterEvaluate {
    // Load the files associated with the testRuntimeClasspath and search them for the mockito-core JAR.
    val testRuntimeFiles = configurations.getByName("testRuntimeClasspath").files
    val mockitoCoreJar = testRuntimeFiles.find {
        it.toString().contains("mockito-core-$mockitoVersion.jar")
    }!!

    // Then, for each test task
    tasks.named<Test>("test") {
        jvmArgs(
            "-Xshare:off",
            "-javaagent:$mockitoCoreJar"
        )
    }
}

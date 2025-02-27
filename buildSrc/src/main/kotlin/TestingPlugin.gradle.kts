dependencies {
    "testImplementation"("org.junit.jupiter:junit-jupiter:5.12.0")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")

    "testImplementation"("org.mockito:mockito-core:5.15.2")
    "testImplementation"("org.mockito:mockito-junit-jupiter:5.15.2")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

dependencies {
    "testImplementation"("org.junit.jupiter:junit-jupiter:5.12.0")
    "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("passed")
    }
}

import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
}

repositories {
    jcenter()
}

dependencies {
    implementation("io.projectreactor", "reactor-core", properties["version.reactor"].toString())
    testImplementation("io.projectreactor", "reactor-test", properties["version.reactor"].toString())
    testImplementation("org.junit.jupiter", "junit-jupiter", properties["version.junit"].toString())
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.FAILED, TestLogEvent.SKIPPED)
    }
}

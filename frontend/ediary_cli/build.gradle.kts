plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "org.srd"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Для работы с HTTP
    implementation("io.ktor:ktor-client-core:2.3.3")
    implementation("io.ktor:ktor-client-cio:2.3.3")
    implementation("io.ktor:ktor-client-logging:2.3.3")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Для CLI
    implementation("com.github.ajalt.clikt:clikt:3.5.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
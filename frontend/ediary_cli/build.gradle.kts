plugins {
    kotlin("jvm") version "1.8.0"
    kotlin("plugin.serialization") version "1.9.10"
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
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.x.x")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Для CLI
    implementation("com.github.ajalt.clikt:clikt:3.5.0")

    // Логирование
    implementation("ch.qos.logback:logback-classic:1.4.11")

    // LocalDate
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

application {
    mainClass.set("org.srd.ediary_cli.MainKt")
    applicationDefaultJvmArgs = listOf("-Dfile.encoding=UTF-8")
}
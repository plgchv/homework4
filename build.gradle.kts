plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    kotlin("plugin.lombok") version "2.0.21"
    id("io.freefair.lombok") version "8.10"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(22)
}
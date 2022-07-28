import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
}

group = "ru.redgift"
version = appVersion
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    maven {
        url = uri("https://nexus.kiap.dev/repository/maven-central/")
    }
}

apply("kiaplib.gradle.kts")

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("com.codeborne:selenide:6.4.0")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
    testImplementation("org.testng:testng:7.5")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
//    testImplementation("com.codeborne:selenide:6.4.0")
//    testImplementation("org.awaitility:awaitility:4.2.0")
//    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
//    testImplementation("org.testng:testng:7.5")
}

tasks.test {
    //useJUnitPlatform()
    useTestNG()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
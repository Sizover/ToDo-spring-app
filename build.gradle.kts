import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
    id("io.qameta.allure") version "2.11.2"
}

group = "ru.redgift"
version = appVersion
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    maven {
        url = uri("https://nexus.kiap.dev/repository/maven-central/")
    }
    mavenCentral()
}

//group = "me.isizov"
//version = "1.0-SNAPSHOT"
//
//repositories {
//    mavenCentral()
//}

apply("kiaplib.gradle.kts")

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("com.codeborne:selenide:6.17.0")
    testImplementation("com.codeborne:selenide-proxy:6.17.0")
    //для скачивания файлов в папку на стороне селеноида
    // https://github.com/selenide/selenide/blob/main/modules/selenoid/README.md
    testImplementation("com.codeborne:selenide-selenoid:6.17.0")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
    testImplementation("org.testng:testng:7.7.1")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.8.3")
    testImplementation("com.opencsv:opencsv:5.7.1")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:%kotlinVersion%")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    testImplementation("org.seleniumhq.selenium:selenium-remote-driver:4.11.0")
//    testImplementation("io.qameta.allure:allure-testng:2.20.1")
//    testImplementation("io.qameta.allure:allure-java-commons:2.20.1")
//    testImplementation("io.qameta.allure:allure-commandline:2.20.1")
    testImplementation("io.qameta.allure:allure-selenide:2.20.1")




//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
//    testImplementation("com.codeborne:selenide:6.4.0")
//    testImplementation("org.awaitility:awaitility:4.2.0")
//    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
//    testImplementation("org.testng:testng:7.5")
}

tasks.test {
    val suite = project.properties.getOrDefault("suite", "testng.xml")
    useTestNG() {
        useDefaultListeners = true
        outputDirectory = file("$projectDir/build/reports/TestNG")
        suites("/src/test/${suite}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}
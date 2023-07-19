import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val appVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    application
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
    testImplementation("com.codeborne:selenide:6.12.3")
    testImplementation("org.awaitility:awaitility:4.2.0")
    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
    testImplementation("org.testng:testng:7.7.1")
    testImplementation("org.seleniumhq.selenium:selenium-java:4.8.1")
    testImplementation("com.opencsv:opencsv:5.7.1")
    testImplementation("com.codeborne:selenide-proxy:6.12.3")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect:%kotlinVersion%")
    testImplementation("org.seleniumhq.selenium:selenium-remote-driver:4.10.0")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
//    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
//    testImplementation("com.codeborne:selenide:6.4.0")
//    testImplementation("org.awaitility:awaitility:4.2.0")
//    testImplementation("org.apache.directory.studio:org.apache.commons.io:2.4")
//    testImplementation("org.testng:testng:7.5")
}

// testing:
val e2eProps: MutableMap<String, Any> = System.getProperties().mapKeys { it.key.toString() }.toMutableMap()
if (null != project.findProperty("firefox")) e2eProps["selenide.browser"] = "firefox"
if (null != project.findProperty("chrome")) e2eProps["selenide.browser"] = "chrome"
if (null != project.findProperty("remote")) {
    e2eProps["selenide.remote"] = "http://127.0.0.1:4444/wd/hub"
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
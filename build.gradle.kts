plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.15"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "progmeth_2025_1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.0"

// java {
//     toolchain {
//         languageVersion = JavaLanguageVersion.of(24)
//     }
// }

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("application")
    mainClass.set("application.Main")
}

javafx {
    version = "24.0.2"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.shadowJar {
    archiveBaseName.set("FinalAnimeSurvivor")
    archiveClassifier.set("")
    archiveVersion.set("")
    manifest {
        attributes["Main-Class"] = "application.Launcher"
    }
    // Exclude module-info files to avoid conflicts when merging JARs
    exclude("module-info.class")
    mergeServiceFiles()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
